package facades;

import dto.PlayerDTO;
import dto.Lists.PlayerListDTO;
import dto.PlayerDTO;
import dto.Lists.PlayerListDTO;
import entities.*;
import entities.Player;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class PlayerFacadeTest {
    private static EntityManagerFactory emf;
    private static PlayerFacade facade;
    private static Player p1,p2,p3,p4;

    public PlayerFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PlayerFacade.getPlayerFacade(emf);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Player("Player1" ,"player1@email.com", "+4511111111", 10);
        p2 = new Player("Player2" ,"player2@email.com", "+4522222222", 20);
        p3 = new Player("Player3" ,"player3@email.com", "+4533333333", 30);
        p4 = new Player("Player4" ,"player4@email.com", "+4444444444", 40);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Player.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
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
            em.createNamedQuery("Player.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void getAllPlayersCorrectTest(){
        PlayerListDTO coachListDTO = facade.getPlayeres();
        Assertions.assertEquals(4, coachListDTO.getAll().size());
    }

    @Test
    public void addPlayerTest() throws AlreadyExistException, MissingInputException, NotFoundException {
        String name = "Player Add";
        String email = "playeradd@email.com";
        String phone = "+4529287722";
        int age = 10;

        Player player = new Player(name, email, phone, age);
        Assertions.assertEquals(name, facade.addPlayer( new PlayerDTO(player)).getName());
    }

    @Test
    public void addPlayerWithNoName() {
        String name = "";
        String email = "playeradd@email.com";
        String phone = "+4529287722";
        int age = 10;

        Player player = new Player(name, email, phone, age);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addPlayer(new PlayerDTO(player));
        });
    }

    @Test
    public void addPlayerWithNoEmail() {
        String name = "Player Add";
        String email = "";
        String phone = "+4529287722";
        int age = 10;

        Player player = new Player(name, email, phone, age);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addPlayer(new PlayerDTO(player));
        });
    }

    @Test
    public void addPlayerWithNoPhone() {
        String name = "Player Add";
        String email = "playeradd@email.com";
        String phone = "";
        int age = 10;

        Player player = new Player(name, email, phone, age);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addPlayer(new PlayerDTO(player));
        });
    }

    @Test
    public void addPlayerWithWrongAge() {
        String name = "Player Add";
        String email = "playeradd@email.com";
        String phone = "";
        int age = 0;

        Player player = new Player(name, email, phone, age);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.addPlayer(new PlayerDTO(player));
        });
    }

    @Test
    public void addPlayerAlreadyExist() {
        Player player = new Player(p2.getName(), p2.getEmail(), p2.getPhone(), p2.getAge());

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.addPlayer(new PlayerDTO(player));
        });
    }

    @Test
    public void deletePlayerTest() throws NotFoundException {
        facade.deletePlayer(p3.getId());
        PlayerListDTO playerListDTO = facade.getPlayeres();
        Assertions.assertEquals(3, playerListDTO.getAll().size());
    }

    @Test
    public void deletePlayerWrongIDTest() throws NotFoundException {

        Assertions.assertThrows(NotFoundException.class, () -> {
            facade.deletePlayer((long) 0);
        });
    }

    @Test
    public void updatePlayerTest() throws AlreadyExistException, MissingInputException, NotFoundException {
        String updatedName = "New Player Test Name";

        PlayerDTO playerDTO = new PlayerDTO(p2);
        playerDTO.setName(updatedName);

        Assertions.assertEquals(updatedName, facade.updatePlayer(playerDTO).getName());
    }

    @Test
    public void updatePlayerWithNoName() {
        PlayerDTO playerDTO = new PlayerDTO(p4);
        playerDTO.setName("");

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updatePlayer(playerDTO);
        });
    }

    @Test
    public void updatePlayerWrongId() {
        PlayerDTO playerDTO = new PlayerDTO(p1);
        playerDTO.setId((long) 0);

        Assertions.assertThrows(MissingInputException.class, () -> {
            facade.updatePlayer(playerDTO);
        });
    }

    @Test
    public void updatePlayerPhoneTaken() {
        PlayerDTO playerDTO = new PlayerDTO(p1);
        playerDTO.setPhone(p2.getPhone());

        Assertions.assertThrows(AlreadyExistException.class, () -> {
            facade.updatePlayer(playerDTO);
        });
    }
}
