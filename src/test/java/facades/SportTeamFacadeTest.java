package facades;

import dto.Lists.SportListDTO;
import dto.Lists.SportTeamListDTO;
import dto.SportDTO;
import dto.SportTeamDTO;
import entities.Coach;
import entities.MemberInfo;
import entities.Sport;
import entities.SportTeam;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SportTeamFacadeTest {
    private static EntityManagerFactory emf;
    private static SportTeamFacade facade;
    private static Sport s1,s2,s3,s4;
    private static SportTeam st1,st2,st3,st4;

    public SportTeamFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = SportTeamFacade.getSportTeamFacade(emf);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        s1 = new Sport("Sport1" ,"Sport1Desc");
        s2 = new Sport("Sport2" ,"Sport2Desc");
        s3 = new Sport("Sport3" ,"Sport3Desc");
        s4 = new Sport("Sport4" ,"Sport4Desc");

        st1 = new SportTeam(1,"SportTeam1", 10,20, s1);
        st2 = new SportTeam(2,"SportTeam2", 20,30, s2);
        st3 = new SportTeam(3,"SportTeam3", 30,40, s3);
        st4 = new SportTeam(4,"SportTeam4", 40,50, s4);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("SportTeam.deleteAllRows").executeUpdate();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
            em.persist(st1);
            em.persist(st2);
            em.persist(st3);
            em.persist(st4);
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
            em.createNamedQuery("SportTeam.deleteAllRows").executeUpdate();
            em.createNamedQuery("Sport.deleteAllRows").executeUpdate();
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getAllSportTeamsTest(){
        SportTeamListDTO sportTeamListDTO = facade.getSportTeams();
        Assertions.assertEquals(4, sportTeamListDTO.getAll().size());
    }

    @Test
    public void addSportTeamTest() throws AlreadyExistException, MissingInputException, NotFoundException {
        int pricePerYear = 100;
        String teamName = "Test Team Name";
        int minAge = 20;
        int maxAge = 40;

        SportTeam sportTeam = new SportTeam(pricePerYear,teamName,minAge,maxAge, s2);
        SportTeamDTO sportTeamDTO = new SportTeamDTO(sportTeam);

        Assertions.assertEquals(maxAge, facade.addSportTeam( sportTeamDTO, s2.getId()).getMaxAge());
    }

    @Test
    public void addSportTeamWithNoTeamName() {
        int pricePerYear = 100;
        String teamName = "";
        int minAge = 20;
        int maxAge = 40;

        SportTeam sportTeam = new SportTeam(pricePerYear,teamName,minAge,maxAge, s2);
        SportTeamDTO sportTeamDTO = new SportTeamDTO(sportTeam);

        Assertions.assertThrows(MissingInputException.class, () -> {
            Assertions.assertEquals(maxAge, facade.addSportTeam( sportTeamDTO, s2.getId()).getMaxAge());
        });
    }

    @Test
    public void addSportTeamWithWrongSportID() {
        int pricePerYear = 100;
        String teamName = "Test Team Name";
        int minAge = 20;
        int maxAge = 40;

        SportTeam sportTeam = new SportTeam(pricePerYear,teamName,minAge,maxAge, s2);
        SportTeamDTO sportTeamDTO = new SportTeamDTO(sportTeam);

        Assertions.assertThrows(NotFoundException.class, () -> {
            Assertions.assertEquals(maxAge, facade.addSportTeam( sportTeamDTO, 0).getMaxAge());
        });
    }

    @Test
    public void addSportTeamAlreadyExist() {
        SportTeamDTO sportTeamDTO = new SportTeamDTO(st3);

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.addSportTeam( sportTeamDTO, s3.getId());
        });
    }

    @Test
    public void deleteSportTeamTest() throws NotFoundException {
        facade.deleteSportTeam(st4.getId());
        SportTeamListDTO sportTeamListDTO = facade.getSportTeams();
        Assertions.assertEquals(3, sportTeamListDTO.getAll().size());
    }

    @Test
    public void deleteSportTeamWrongIDTest() throws NotFoundException {

        Assertions.assertThrows(NotFoundException.class, () -> {
            facade.deleteSportTeam((long) 0);
        });
    }

    @Test
    public void updateSportTeamTest() throws AlreadyExistException, MissingInputException, NotFoundException {
        SportTeamDTO sportTeamDTO = new SportTeamDTO(st1);
        sportTeamDTO.setSport(new SportDTO(s4));

        Assertions.assertEquals(s4.getName(), facade.updateSportTeam( sportTeamDTO).getSport().getName());
    }

    @Test
    public void updateSportTeamWithNoTeamName() {
        SportTeamDTO sportTeamDTO = new SportTeamDTO(st1);
        sportTeamDTO.setTeamName("");

        Assertions.assertThrows(MissingInputException.class, () -> {
           facade.updateSportTeam(sportTeamDTO);
        });
    }

    @Test
    public void updateSportTeamWrongMinAge() {
        SportTeamDTO sportTeamDTO = new SportTeamDTO(st1);
        sportTeamDTO.setMinAge(0);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updateSportTeam(sportTeamDTO);
        });
    }
}
