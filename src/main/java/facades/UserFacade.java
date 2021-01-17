package facades;

import dto.UserCredentialsDTO;
import entities.Role;
import entities.User;
import errorhandling.LoginInvalidException;
import errorhandling.UsernameTakenException;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(UserCredentialsDTO uc) throws LoginInvalidException, UsernameTakenException {
        EntityManager em = emf.createEntityManager();
        if ("".equals(uc.getUsername()) || "".equals(uc.getPassword())) {
            throw new LoginInvalidException("No username or password entered.");
        }
        User existingUser = em.find(User.class, uc.getUsername());
        if (existingUser != null) {
            throw new UsernameTakenException(uc.getUsername());
        } else {

            em.getTransaction().begin();
            User user = new User(uc.getUsername(), uc.getPassword());

            Role role = em.find(Role.class, "user");
            if (role == null) {
                role = new Role("user");
                em.persist(role);
            }
            user.addRole(role);
            em.persist(user);

            em.getTransaction().commit();

            return user;
        }
    }

}
