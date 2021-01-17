package facades;

import dto.Lists.SportListDTO;
import dto.SportDTO;
import entities.Sport;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class SportFacade {

    private static SportFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private SportFacade() {}

    public static SportFacade getSportFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SportFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public SportListDTO getSports(){
        EntityManager em = emf.createEntityManager();
        try{
            List<Sport> sports = em.createQuery("SELECT s FROM Sport s").getResultList();
            return new SportListDTO(sports);
        }finally{  
            em.close();
        }

    }

    public SportDTO addSport(SportDTO sportDTO) throws MissingInputException, AlreadyExistException {
        if (sportDTO.getName() == null || sportDTO.getName().isEmpty() || sportDTO.getDescription() == null || sportDTO.getDescription().isEmpty()) {
            throw new MissingInputException("Name or description is missing");
        }


        //Check if sport already exist
        EntityManager em = getEntityManager();
        Query query = em.createQuery("Select count(s) FROM Sport s WHERE s.name = :name");
        query.setParameter("name", sportDTO.getName());

        Long sportExist = (Long) query.getSingleResult();

        if (sportExist > 0) {
            throw new AlreadyExistException("Sport already exist");
        }

        Sport sport = new Sport(sportDTO.getName(), sportDTO.getDescription());
        try {
            em.getTransaction().begin();
            em.persist(sport);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SportDTO(sport);
    }

    public SportDTO updateSport(SportDTO sportDTO) throws MissingInputException, NotFoundException, AlreadyExistException {

        if (sportDTO.getId() < 1) {
            throw new MissingInputException("Sport id is missing or less than 0, id: " + sportDTO.getId());
        }

        if (sportDTO.getName() == null || sportDTO.getName().isEmpty() || sportDTO.getDescription() == null || sportDTO.getDescription().isEmpty()) {
            throw new MissingInputException("Name or description is missing");
        }

        //Check if sport already exist
        EntityManager em = getEntityManager();
        //Check if sport exist
        Sport sport = em.find(Sport.class, sportDTO.getId());

        if (sport == null) {
            //Sport does not exist
            throw new NotFoundException("Sport could not be found with id: " + sportDTO.getId());
        }

        //Check if sport name already exist
        Query query = em.createQuery("Select count(s) FROM Sport s WHERE s.name = :name and s.id != :id");
        query.setParameter("name", sportDTO.getName());
        query.setParameter("id", sportDTO.getId());

        Long sportExist = (Long) query.getSingleResult();

        if (sportExist > 0) {
            throw new AlreadyExistException("Sport name already exist, name: " + sportDTO.getName());
        }

        try {
            em.getTransaction().begin();
            sport.setName(sportDTO.getName());
            sport.setDescription(sportDTO.getDescription());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SportDTO(sport);
    }



    public SportDTO deleteSport(Long id) throws NotFoundException, MissingInputException {
        EntityManager em = getEntityManager();
        Sport sport = em.find(Sport.class, id);
        if (sport == null) {
                throw new NotFoundException("Could not delete, provided sport id does not exist, id: " + id);
        }

        //Check if sport is used in a team
        Query query = em.createQuery("Select count(s) FROM SportTeam s WHERE s.sport.id = :id");
        query.setParameter("id", id);

        Long sportTeamExist = (Long) query.getSingleResult();

        if (sportTeamExist > 0) {
            throw new MissingInputException("Sport is used in a team and cannot be deleted");
        }


        try {
            em.getTransaction().begin();
            em.remove(sport);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new SportDTO(sport);
    }
}
