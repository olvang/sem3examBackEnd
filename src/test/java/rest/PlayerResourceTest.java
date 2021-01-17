package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Coach;
import entities.Player;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlayerResourceTest {
    private static Player p1,p2,p3,p4;
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @BeforeEach
    public void setup() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");

            User admin = new User("admin", "admin");
            admin.addRole(adminRole);

            User user = new User("user", "user");
            user.addRole(userRole);

            p1 = new Player("Player1" ,"player1@email.com", "+4511111111", 10);
            p2 = new Player("Player2" ,"player2@email.com", "+4522222222", 20);
            p3 = new Player("Player3" ,"player3@email.com", "+4533333333", 30);
            p4 = new Player("Player4" ,"player4@email.com", "+4444444444", 40);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @AfterEach
    public void afterEach() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Player").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/user/login")
                .then()
                .extract().path("token");
    }

    @Test
    public void getPlayers() {
        login("admin", "admin");

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .get("/player").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(4));
    }

    @Test
    public void testAddCoach() {
        login("admin", "admin");

        String name = "Player Add";
        String email = "playeradd@email.com";
        String phone = "+4529287722";
        int age = 10;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", name);
        jsonAsMap.put("email", email);
        jsonAsMap.put("phone", phone);
        jsonAsMap.put("age", age);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/player").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("email", equalTo(email));
    }

    @Test
    public void testAddSportEmptyName() {
        login("admin", "admin");

        String name = "";
        String email = "playeradd@email.com";
        String phone = "+4529287722";
        int age = 10;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", name);
        jsonAsMap.put("email", email);
        jsonAsMap.put("phone", phone);
        jsonAsMap.put("age", age);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/player").then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Name, email, phone or age is missing"));
    }

    @Test
    public void testAddSportEmptyEmail() {
        login("admin", "admin");

        String name = "Player Add";
        String email = "";
        String phone = "+4529287722";
        int age = 10;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", name);
        jsonAsMap.put("email", email);
        jsonAsMap.put("phone", phone);
        jsonAsMap.put("age", age);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/player").then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Name, email, phone or age is missing"));
    }

    @Test
    public void testAddSportEmptyPhone() {
        login("admin", "admin");

        String name = "Player Add";
        String email = "playeradd@email.com";
        String phone = "";
        int age = 10;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", name);
        jsonAsMap.put("email", email);
        jsonAsMap.put("phone", phone);
        jsonAsMap.put("age", age);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/player").then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Name, email, phone or age is missing"));
    }

    @Test
    public void testAddSportWrongAge() {
        login("admin", "admin");

        String name = "Player Add";
        String email = "playeradd@email.com";
        String phone = "+4529287722";
        int age = 0;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", name);
        jsonAsMap.put("email", email);
        jsonAsMap.put("phone", phone);
        jsonAsMap.put("age", age);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/player").then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Age cannot be less than 1, age: 0"));
    }

    @Test
    public void deletePlayer() {
        login("admin", "admin");

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/player/" + p2.getId()).then()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

    @Test
    public void deletePlayerWrongID() {
        login("admin", "admin");

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/player/0").then()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("Could not delete, provided player id does not exist, id: 0"));
    }
}
