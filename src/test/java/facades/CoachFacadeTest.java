package facades;

import dto.Lists.CoachListDTO;
import dto.CoachDTO;
import entities.Coach;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CoachFacadeTest {
    private static EntityManagerFactory emf;
    private static CoachFacade facade;
    private static Coach c1,c2,c3,c4;

    public CoachFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CoachFacade.getCoachFacade(emf);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        c1 = new Coach("Coach1" ,"coach1@email.com", "+4511111111");
        c2 = new Coach("Coach2" ,"coach2@email.com", "+4522222222");
        c3 = new Coach("Coach3" ,"coach3@email.com", "+4533333333");
        c4 = new Coach("Coach4" ,"coach4@email.com", "+4444444444");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Coach.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            em.persist(c4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void afterEach() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Coach.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getAllCoachsCorrectTest(){
        CoachListDTO coachListDTO = facade.getCoaches();
        Assertions.assertEquals(4, coachListDTO.getAll().size());
    }

    @Test
    public void addCoachTest() throws AlreadyExistException, MissingInputException {
        String name = "Coach Add";
        String email = "coachadd@email.com";
        String phone = "+4529287722";

        Coach coach = new Coach(name, email, phone);
        Assertions.assertEquals(name, facade.addCoach( new CoachDTO(coach)).getName());
    }

    @Test
    public void addCoachWithNoName() {
        String name = "";
        String email = "coachadd@email.com";
        String phone = "+4529287722";

        Coach coach = new Coach(name, email, phone);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addCoach(new CoachDTO(coach));
        });
    }

    @Test
    public void addCoachWithNoEmail() {
        String name = "Coach Add";
        String email = "";
        String phone = "+4529287722";

        Coach coach = new Coach(name, email, phone);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addCoach(new CoachDTO(coach));
        });
    }

    @Test
    public void addCoachWithNoPhone() {
        String name = "Coach Add";
        String email = "coachadd@email.com";
        String phone = "";

        Coach coach = new Coach(name, email, phone);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addCoach(new CoachDTO(coach));
        });
    }

    @Test
    public void addCoachAlreadyExist() {

        Coach coach = new Coach(c2.getName(), c2.getEmail(), c2.getPhone());

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.addCoach(new CoachDTO(coach));
        });
    }

    @Test
    public void deleteCoachTest() throws NotFoundException {
        facade.deleteCoach(c3.getId());
        CoachListDTO coachListDTO = facade.getCoaches();
        Assertions.assertEquals(3, coachListDTO.getAll().size());
    }

    @Test
    public void deleteCoachWrongIDTest() throws NotFoundException {

        Assertions.assertThrows(NotFoundException.class, () -> {
            facade.deleteCoach((long) 0);
        });
    }

    @Test
    public void updateCoachTest() throws AlreadyExistException, MissingInputException, NotFoundException {
        String updatedName = "New Coach Test Name";

        CoachDTO coachDTO = new CoachDTO(c1);
        coachDTO.setName(updatedName);

        Assertions.assertEquals(updatedName, facade.updateCoach( coachDTO).getName());
    }

    @Test
    public void updateCoachWithNoName() {
        CoachDTO coachDTO = new CoachDTO(c3);
        coachDTO.setName("");

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updateCoach(coachDTO);
        });
    }

    @Test
    public void updateCoachWrongId() {
        CoachDTO coachDTO = new CoachDTO(c4);
        coachDTO.setId((long) 0);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updateCoach(coachDTO);
        });
    }

    @Test
    public void updateCoachPhoneTaken() {
        CoachDTO coachDTO = new CoachDTO(c1);
        coachDTO.setPhone(c2.getPhone());

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.updateCoach(coachDTO);
        });
    }
}
