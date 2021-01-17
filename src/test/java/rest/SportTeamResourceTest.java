package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Role;
import entities.Sport;
import entities.SportTeam;
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

public class SportTeamResourceTest {

    private static Sport s1,s2,s3,s4;
    private static SportTeam st1,st2,st3,st4;
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

            s1 = new Sport("Sport1" ,"Sport1Desc");
            s2 = new Sport("Sport2" ,"Sport2Desc");
            s3 = new Sport("Sport3" ,"Sport3Desc");
            s4 = new Sport("Sport4" ,"Sport4Desc");

            st1 = new SportTeam(1,"SportTeam1", 10,20, s1);
            st2 = new SportTeam(2,"SportTeam2", 20,30, s2);
            st3 = new SportTeam(3,"SportTeam3", 30,40, s3);
            st4 = new SportTeam(4,"SportTeam4", 40,50, s4);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
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
            em.createQuery("DELETE FROM SportTeam").executeUpdate();
            em.createQuery("DELETE FROM Sport").executeUpdate();
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
    public void getSportTeams() {
        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .when()
                .get("/sportteams").then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("size()", equalTo(4));
    }

    @Test
    public void testAddSportTeam() {
        login("admin", "admin");

        int pricePerYear = 100;
        String teamName = "Test Team Name";
        int minAge = 20;
        int maxAge = 40;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("pricePerYear", pricePerYear);
        jsonAsMap.put("teamName", teamName);
        jsonAsMap.put("minAge", minAge);
        jsonAsMap.put("maxAge", maxAge);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/sportteams/sport/" + s2.getId()).then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("teamName", equalTo(teamName));
    }

    @Test
    public void testAddSportTeamEmptyName() {
        login("admin", "admin");

        int pricePerYear = 100;
        String teamName = "";
        int minAge = 20;
        int maxAge = 40;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("pricePerYear", pricePerYear);
        jsonAsMap.put("teamName", teamName);
        jsonAsMap.put("minAge", minAge);
        jsonAsMap.put("maxAge", maxAge);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/sportteams/sport/" + s2.getId()).then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Team Name is missing"));
    }

    @Test
    public void testAddSportTeamNoMaxAge() {
        login("admin", "admin");

        int pricePerYear = 100;
        String teamName = "Test Team Name";
        int minAge = 20;

        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("pricePerYear", pricePerYear);
        jsonAsMap.put("teamName", teamName);
        jsonAsMap.put("minAge", minAge);

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(jsonAsMap)
                .when()
                .post("/sportteams/sport/" + s2.getId()).then()
                .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
                .body("message", equalTo("Price, min age or max age is less than one"));
    }

    @Test
    public void deleteSport() {
        login("admin", "admin");

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/sportteams/" + st1.getId()).then()
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }

    @Test
    public void deleteSportWrongID() {
        login("admin", "admin");

        given()
                .contentType("application/json")
                .accept(ContentType.JSON)
                .header("x-access-token", securityToken)
                .when()
                .delete("/sportteams/0").then()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("Could not delete, provided team sport id does not exist, id: 0"));
    }

}
