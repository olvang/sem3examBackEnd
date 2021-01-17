package facades;

import dto.Lists.MemberInfoListDTO;
import dto.Lists.SportTeamListDTO;
import dto.SportTeamDTO;
import entities.Sport;
import entities.SportTeam;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class SportTeamFacade {

    private static SportTeamFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private SportTeamFacade() {}

    public static SportTeamFacade getSportTeamFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SportTeamFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public SportTeamListDTO getSportTeams(){
        EntityManager em = emf.createEntityManager();
        try{
            List<SportTeam> sportsTeams = em.createQuery("SELECT s FROM SportTeam s").getResultList();
            return new SportTeamListDTO(sportsTeams);
        }finally{  
            em.close();
        }

    }

    public SportTeamDTO addSportTeam(SportTeamDTO sportTeamDTO, long sportId) throws MissingInputException, AlreadyExistException, NotFoundException {
        if (sportTeamDTO.getTeamName() == null || sportTeamDTO.getTeamName().isEmpty()) {
            throw new MissingInputException("Team Name is missing");
        }

        if (sportTeamDTO.getMaxAge() < 1 || sportTeamDTO.getMinAge() < 1 || sportTeamDTO.getPricePerYear() < 1) {
            throw new MissingInputException("Price, min age or max age is less than one");
        }

        //Check if sportTeam already exist
        EntityManager em = getEntityManager();
        Query query = em.createQuery("Select count(s) FROM SportTeam s WHERE s.teamName = :teamName");
        query.setParameter("teamName", sportTeamDTO.getTeamName());

        Long sportTeamExist = (Long) query.getSingleResult();

        if (sportTeamExist > 0) {
            throw new AlreadyExistException("SportTeam already exist with team name: " + sportTeamDTO.getTeamName());
        }

        //Check if sport exist
        Sport sport = em.find(Sport.class, sportId);

        if (sport == null) {
            //Sport does not exist
            throw new NotFoundException("Sport could not be found with id: " + sportId);
        }

        SportTeam sportTeam = new SportTeam(sportTeamDTO.getPricePerYear(), sportTeamDTO.getTeamName(), sportTeamDTO.getMinAge(), sportTeamDTO.getMaxAge(), sport);
        try {
            em.getTransaction().begin();
            em.persist(sportTeam);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SportTeamDTO(sportTeam);
    }

    public SportTeamDTO updateSportTeam(SportTeamDTO sportTeamDTO) throws MissingInputException, NotFoundException {
        if (sportTeamDTO.getId() < 1) {
            throw new MissingInputException("SportTeam id less than 0, id: " + sportTeamDTO.getId());
        }

        if (sportTeamDTO.getTeamName() == null || sportTeamDTO.getTeamName().isEmpty()) {
            throw new MissingInputException("Team Name is missing");
        }

        if (sportTeamDTO.getMaxAge() < 1 || sportTeamDTO.getMinAge() < 1 || sportTeamDTO.getPricePerYear() < 1) {
            throw new MissingInputException("Price, min age or max age is less than one");
        }

        EntityManager em = getEntityManager();

        //Check if sport team  exist
        SportTeam sportTeam = em.find(SportTeam.class, sportTeamDTO.getId());

        if (sportTeam == null) {
            throw new NotFoundException("SportTeam could not be found with id: " + sportTeamDTO.getId());
        }

        //Check if sport exist
        Sport sport = em.find(Sport.class, sportTeamDTO.getSport().getId());

        if (sport == null) {
            //Sport does not exist
            throw new NotFoundException("Sport could not be found with id: " + sportTeamDTO.getSport().getId());
        }

        try {
            em.getTransaction().begin();
            sportTeam.setTeamName(sportTeamDTO.getTeamName());
            sportTeam.setPricePerYear(sportTeamDTO.getPricePerYear());
            sportTeam.setMinAge(sportTeamDTO.getMinAge());
            sportTeam.setMaxAge(sportTeamDTO.getMaxAge());
            sportTeam.setSport(sport);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SportTeamDTO(sportTeam);
    }

    public SportTeamDTO deleteSportTeam(Long id) throws NotFoundException {
        EntityManager em = getEntityManager();
        SportTeam sportTeam = em.find(SportTeam.class, id);
        if (sportTeam == null) {
                throw new NotFoundException("Could not delete, provided team sport id does not exist, id: " + id);
        }
        try {
            em.getTransaction().begin();
            em.remove(sportTeam);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SportTeamDTO(sportTeam);
    }

    public MemberInfoListDTO getPlayersFromTeam(Long teamId){
        EntityManager em = emf.createEntityManager();
        try{
            Query query = em.createQuery("Select m FROM MemberInfo m WHERE m.sportTeam.id = :teamId");
            query.setParameter("teamId", teamId);

            return new MemberInfoListDTO(query.getResultList());
        }finally{
            em.close();
        }

    }
}
