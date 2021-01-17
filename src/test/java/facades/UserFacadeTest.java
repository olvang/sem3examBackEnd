/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.UserCredentials;
import entities.User;
import errorhandling.LoginInvalidException;
import errorhandling.UsernameTakenException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author gamma
 */
public class UserFacadeTest {

    private static EntityManagerFactory emf;
    private static UserFacade facade;

    public UserFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = UserFacade.getUserFacade(emf);
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testCreateUserSuccessNewRole() throws LoginInvalidException, UsernameTakenException {
        //Arrange
        String expRole = "user";
        String uname = "person1";

        //Act
        UserCredentials uc = new UserCredentials(uname, "pass23");
        User user = facade.createUser(uc);

        assertEquals(uname, user.getUserName());
        assertEquals(expRole, user.getRoleList().get(0).getRoleName());
    }

    @Test
    public void testCreateUserSuccessExistingRole() throws LoginInvalidException, UsernameTakenException {
        //Arrange
        String expRole = "user";
        String uname = "person2";

        //Act
        UserCredentials uc = new UserCredentials(uname, "pass232");
        User user = facade.createUser(uc);

        assertEquals(uname, user.getUserName());
        assertEquals(expRole, user.getRoleList().get(0).getRoleName());
    }

    @Test
    public void testCreateUserExistingUser() throws LoginInvalidException, UsernameTakenException {
        UsernameTakenException assertThrows;

        //Act
        UserCredentials uc = new UserCredentials("person3", "pass23s2");
        User userA = facade.createUser(uc);

        assertThrows = Assertions.assertThrows(UsernameTakenException.class, () -> {
            User userB = facade.createUser(uc);
        });
        Assertions.assertNotNull(assertThrows);
    }

    @Test
    public void testCreateUserNoName() throws LoginInvalidException {
        //Arrange
        LoginInvalidException assertThrows;

        //Act
        UserCredentials uc = new UserCredentials("", "pass23");

        assertThrows = Assertions.assertThrows(LoginInvalidException.class, () -> {
            User user = facade.createUser(uc);
        });
        Assertions.assertNotNull(assertThrows);
    }

    @Test
    public void testCreateUserNoPass() throws LoginInvalidException {
        //Arrange
        LoginInvalidException assertThrows;

        //Act
        UserCredentials uc = new UserCredentials("person1", "");

        assertThrows = Assertions.assertThrows(LoginInvalidException.class, () -> {
            User user = facade.createUser(uc);
        });
        Assertions.assertNotNull(assertThrows);
    }

    @Test
    public void testCreateUserNameNull() throws LoginInvalidException {
        //Arrange
        LoginInvalidException assertThrows;

        //Act
        UserCredentials uc = new UserCredentials(null, "pass23");

        assertThrows = Assertions.assertThrows(LoginInvalidException.class, () -> {
            User user = facade.createUser(uc);
        });
        Assertions.assertNotNull(assertThrows);
    }

    @Test
    public void testCreateUserPassNull() throws LoginInvalidException {
        //Arrange
        LoginInvalidException assertThrows;

        //Act
        UserCredentials uc = new UserCredentials("person1", null);

        assertThrows = Assertions.assertThrows(LoginInvalidException.class, () -> {
            User user = facade.createUser(uc);
        });
        Assertions.assertNotNull(assertThrows);
    }

}
