package facades;

import dto.CoachDTO;
import dto.Lists.CoachListDTO;
import dto.SportDTO;
import entities.Coach;
import entities.Sport;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class CoachFacade {
    private static CoachFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private CoachFacade() {}

    public static CoachFacade getCoachFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CoachFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CoachListDTO getCoaches(){
        EntityManager em = emf.createEntityManager();
        try{
            List<Coach> coaches = em.createQuery("SELECT c FROM Coach c").getResultList();
            return new CoachListDTO(coaches);
        }finally{
            em.close();
        }

    }

    public CoachDTO addCoach(CoachDTO coachDTO) throws MissingInputException, AlreadyExistException {
        if (coachDTO.getName() == null || coachDTO.getName().isEmpty() || coachDTO.getEmail() == null || coachDTO.getEmail().isEmpty() || coachDTO.getPhone() == null || coachDTO.getPhone().isEmpty()) {
            throw new MissingInputException("Name, email or phone is missing");
        }

        //Check if coach already exist
        EntityManager em = getEntityManager();
        Query query = em.createQuery("Select count(c) FROM Coach c WHERE c.phone = :phone");
        query.setParameter("phone", coachDTO.getPhone());

        Long coachExist = (Long) query.getSingleResult();

        if (coachExist > 0) {
            throw new AlreadyExistException("Coach already exist with phone: " + coachDTO.getPhone());
        }

        Coach coach = new Coach(coachDTO.getName(), coachDTO.getEmail(), coachDTO.getPhone());
        try {
            em.getTransaction().begin();
            em.persist(coach);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new CoachDTO(coach);
    }

    public CoachDTO updateCoach(CoachDTO coachDTO) throws MissingInputException, AlreadyExistException, NotFoundException {
        if (coachDTO.getId() < 1) {
            throw new MissingInputException("Coach id is missing or less than 0, id: " + coachDTO.getId());
        }

        if (coachDTO.getName() == null || coachDTO.getName().isEmpty() || coachDTO.getEmail() == null || coachDTO.getEmail().isEmpty() || coachDTO.getPhone() == null || coachDTO.getPhone().isEmpty()) {
            throw new MissingInputException("Name, email or phone is missing");
        }


        EntityManager em = getEntityManager();

        //Check if coach exist
        Coach coach = em.find(Coach.class, coachDTO.getId());

        if (coach == null) {
            //Coach does not exist
            throw new NotFoundException("Coach could not be found with id: " + coachDTO.getId());
        }

        //Check if phone already exist
        Query query = em.createQuery("Select count(c) FROM Coach c WHERE c.phone = :phone and c.id != :id");
        query.setParameter("phone", coachDTO.getPhone());
        query.setParameter("id", coachDTO.getId());

        Long coachExist = (Long) query.getSingleResult();

        if (coachExist > 0) {
            throw new AlreadyExistException("Coach already exist with phone: " + coachDTO.getPhone());
        }


        try {
            em.getTransaction().begin();
            coach.setName(coachDTO.getName());
            coach.setEmail(coachDTO.getEmail());
            coach.setPhone(coachDTO.getPhone());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new CoachDTO(coach);
    }

    public CoachDTO deleteCoach(Long id) throws NotFoundException {
        EntityManager em = getEntityManager();
        Coach coach = em.find(Coach.class, id);
        if (coach == null) {
            throw new NotFoundException("Could not delete, provided coach id does not exist, id: " + id);
        }
        try {
            em.getTransaction().begin();
            em.remove(coach);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new CoachDTO(coach);
    }
}
