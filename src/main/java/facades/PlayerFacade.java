package facades;

import dto.Lists.MemberInfoListDTO;
import dto.Lists.PlayerListDTO;
import dto.MemberInfoDTO;
import dto.PlayerDTO;
import entities.MemberInfo;
import entities.Player;
import entities.SportTeam;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class PlayerFacade {
    private static PlayerFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PlayerFacade() {}

    public static PlayerFacade getPlayerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PlayerFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PlayerListDTO getPlayeres(){
        EntityManager em = emf.createEntityManager();
        try{
            List<Player> players = em.createQuery("SELECT p FROM Player p").getResultList();
            return new PlayerListDTO(players);
        }finally{
            em.close();
        }

    }

    public PlayerDTO addPlayer(PlayerDTO playerDTO) throws MissingInputException, AlreadyExistException, NotFoundException {
        if (playerDTO.getName() == null || playerDTO.getName().isEmpty() || playerDTO.getEmail() == null || playerDTO.getEmail().isEmpty() || playerDTO.getPhone() == null || playerDTO.getPhone().isEmpty()) {
            throw new MissingInputException("Name, email, phone or age is missing");
        }

        if (playerDTO.getAge() < 1) {
            throw new MissingInputException("Age cannot be less than 1, age: " + playerDTO.getAge());
        }

        //Check if player already exist
        EntityManager em = getEntityManager();
        Query query = em.createQuery("Select count(c) FROM Player c WHERE c.phone = :phone");
        query.setParameter("phone", playerDTO.getPhone());

        Long coachExist = (Long) query.getSingleResult();

        if (coachExist > 0) {
            throw new AlreadyExistException("Player already exist with phone: " + playerDTO.getPhone());
        }


        Player player = new Player(playerDTO.getName(), playerDTO.getEmail(), playerDTO.getPhone(), playerDTO.getAge());

        /*//Check if user exist
        if (playerDTO.getUser().getUsername() != null || !playerDTO.getName().isEmpty()) {
            User user = em.find(User.class, playerDTO.getUser().getUsername());

            if (user == null) {
                //User does not exist
                throw new NotFoundException("User could not be found with username: " + playerDTO.getUser().getUsername());
            }
            //player.setUser(user);
        }*/

        try {
            em.getTransaction().begin();
            em.persist(player);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PlayerDTO(player);
    }

    public PlayerDTO updatePlayer(PlayerDTO playerDTO) throws MissingInputException, AlreadyExistException, NotFoundException {
        if (playerDTO.getId() < 1) {
            throw new MissingInputException("Player id is missing or less than 0, id: " + playerDTO.getId());
        }

        if (playerDTO.getName() == null || playerDTO.getName().isEmpty() || playerDTO.getEmail() == null || playerDTO.getEmail().isEmpty() || playerDTO.getPhone() == null || playerDTO.getPhone().isEmpty()) {
            throw new MissingInputException("Name, email, phone or age is missing");
        }

        /*if (playerDTO.getUser().getUsername() == null || playerDTO.getUser().getUsername().isEmpty()) {
            throw new MissingInputException("Username is missing");
        }*/

        if (playerDTO.getAge() < 1) {
            throw new MissingInputException("Age cannot be less than 1: " + playerDTO.getAge());
        }


        EntityManager em = getEntityManager();

        /*//Check if user exist
        User user = em.find(User.class, playerDTO.getUser().getUsername());

        if (user == null) {
                //User does not exist
                throw new NotFoundException("User could not be found with username: " + playerDTO.getUser().getUsername());
        }*/

        //Check if player exist
        Player player = em.find(Player.class, playerDTO.getId());

        if (player == null) {
            //Player does not exist
            throw new NotFoundException("Player could not be found with id: " + playerDTO.getId());
        }

        //Check if phone already exist
        Query query = em.createQuery("Select count(p) FROM Player p WHERE p.phone = :phone and p.id != :id");
        query.setParameter("phone", playerDTO.getPhone());
        query.setParameter("id", playerDTO.getId());

        Long playerExist = (Long) query.getSingleResult();

        if (playerExist > 0) {
            throw new AlreadyExistException("Player already exist with phone: " + playerDTO.getPhone());
        }


        try {
            em.getTransaction().begin();
            player.setName(playerDTO.getName());
            player.setEmail(playerDTO.getEmail());
            player.setPhone(playerDTO.getPhone());
            player.setAge(playerDTO.getAge());
            //player.setUser(user);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PlayerDTO(player);
    }

    public PlayerDTO deletePlayer(Long id) throws NotFoundException {
        EntityManager em = getEntityManager();
        Player player = em.find(Player.class, id);
        if (player == null) {
            throw new NotFoundException("Could not delete, provided player id does not exist, id: " + id);
        }
        try {
            em.getTransaction().begin();
            em.remove(player);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PlayerDTO(player);
    }

    public MemberInfoDTO addPlayerToTeam(Long playerId, Long teamId) throws NotFoundException, AlreadyExistException {
        EntityManager em = getEntityManager();


        Player player = em.find(Player.class, playerId);
        if (player == null) {
            throw new NotFoundException("Could find player with id: " + playerId);
        }

        SportTeam team = em.find(SportTeam.class, teamId);
        if (team == null) {
            throw new NotFoundException("Could find sport team with id: " + teamId);
        }

        //check if memberInfo already exist
        Query query = em.createQuery("Select count(m) FROM MemberInfo m WHERE m.player.id = :playerId and m.sportTeam.id = :teamId");
        query.setParameter("playerId", playerId);
        query.setParameter("teamId", teamId);

        Long memberInfoExist = (Long) query.getSingleResult();

        if (memberInfoExist > 0) {
            throw new AlreadyExistException("Player has already been added to the team");
        }

        MemberInfo memberInfo = new MemberInfo(true, new Date(), team, player);

        try {
            em.getTransaction().begin();
            em.persist(memberInfo);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new MemberInfoDTO(memberInfo);
    }

    public MemberInfoListDTO getTeamsFromPlayer(Long playerId){
        EntityManager em = emf.createEntityManager();
        try{
            Query query = em.createQuery("Select m FROM MemberInfo m WHERE m.player.id = :playerId");
            query.setParameter("playerId", playerId);

            return new MemberInfoListDTO(query.getResultList());
        }finally{
            em.close();
        }

    }
}
