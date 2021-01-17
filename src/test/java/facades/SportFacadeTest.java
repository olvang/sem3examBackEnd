package facades;

import dto.Lists.SportListDTO;
import dto.SportDTO;
import dto.SportTeamDTO;
import entities.Sport;
import entities.SportTeam;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class SportFacadeTest {
    private static EntityManagerFactory emf;
    private static SportFacade facade;
    private static Sport s1,s2,s3,s4;

    public SportFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = SportFacade.getSportFacade(emf);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        s1 = new Sport("Sport1" ,"Sport1Desc");
        s2 = new Sport("Sport2" ,"Sport2Desc");
        s3 = new Sport("Sport3" ,"Sport3Desc");
        s4 = new Sport("Sport4" ,"Sport4Desc");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
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
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getAllSportsCorrectTest(){
        SportListDTO sportListDTO = facade.getSports();
        Assertions.assertEquals(4, sportListDTO.getAll().size());
    }

    @Test
    public void addSportTest() throws AlreadyExistException, MissingInputException {
        String name = "Name Test";
        String description = "Desc Test";

        Sport sport = new Sport(name, description);
        Assertions.assertEquals(name, facade.addSport( new SportDTO(sport)).getName());
    }

    @Test
    public void addSportWithNoName() {
        String name = "";
        String description = "Desc Test";

        Sport sport = new Sport(name, description);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addSport(new SportDTO(sport));
        });
    }

    @Test
    public void addSportWithNoDescription() {
        String name = "Desc Test";
        String description = "";

        Sport sport = new Sport(name, description);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addSport(new SportDTO(sport));
        });
    }

    @Test
    public void addSportAlreadyExist() {

        Sport sport = new Sport(s2.getName(), s2.getDescription());

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.addSport(new SportDTO(sport));
        });
    }

    @Test
    public void deleteSportTest() throws NotFoundException, MissingInputException {
        facade.deleteSport(s1.getId());
        SportListDTO sportListDTO = facade.getSports();
        Assertions.assertEquals(3, sportListDTO.getAll().size());
    }

    @Test
    public void deleteSportWrongIDTest() throws NotFoundException {

        Assertions.assertThrows(NotFoundException.class, () -> {
            facade.deleteSport((long) 0);
        });
    }

    @Test
    public void updateSportTest() throws AlreadyExistException, MissingInputException, NotFoundException {
        String updatedName = "New Test Name";

        SportDTO sportDTO = new SportDTO(s1);
        sportDTO.setName(updatedName);

        Assertions.assertEquals(updatedName, facade.updateSport( sportDTO).getName());
    }

    @Test
    public void updateSportWithNoName() {
        SportDTO sportDTO = new SportDTO(s1);
        sportDTO.setName("");

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updateSport(sportDTO);
        });
    }

    @Test
    public void updateSportWrongId() {
        SportDTO sportDTO = new SportDTO(s1);
        sportDTO.setId((long) 0);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updateSport(sportDTO);
        });
    }

    @Test
    public void updateSportNameTaken() {
        SportDTO sportDTO = new SportDTO(s1);
        sportDTO.setName(s2.getName());

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.updateSport(sportDTO);
        });
    }
}
