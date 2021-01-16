package rest;

import com.google.gson.Gson;
import dto.*;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.HttpUtils;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    Gson GSON = new Gson();
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is status
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("select u from User u", entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("fetch")
    public Response fetchFromServer() throws InterruptedException, ExecutionException {
        ArrayList<String> URLS = new ArrayList();
        URLS.add("https://api.chucknorris.io/jokes/random");
        URLS.add("https://icanhazdadjoke.com");
        URLS.add("https://xkcd.com/info.0.json");
        URLS.add("https://dog.ceo/api/breeds/image/random");
        URLS.add("https://api.ipify.org/?format=json");

        ExecutorService es = Executors.newCachedThreadPool();
        List<Future<GenericDTO>> futures = new ArrayList();
        for (int i = 0; i < URLS.size(); i++) {
            Callable<GenericDTO> fetcher = new FetchTask(URLS.get(i), i);
            Future future = es.submit(fetcher);
            futures.add(future);
        }

        FullDTO dto = new FullDTO();
        for (Future<GenericDTO> future : futures) {
            if (future.get() instanceof ChuckDTO) {
                dto.setChuckDTO((ChuckDTO) future.get()); 
            } else if (future.get() instanceof DadDTO) {
                dto.setDadDTO((DadDTO) future.get()); 
            } else if (future.get() instanceof XkcdDTO) {
                dto.setXkcdDTO((XkcdDTO) future.get()); 
            } else if (future.get() instanceof DogDTO) {
                dto.setDogDTO((DogDTO) future.get()); 
            }else if (future.get() instanceof IpDTO) {
                dto.setIpDTO((IpDTO) future.get());
            }
        }

        return Response.ok().entity(GSON.toJson(dto)).build();
    }
}

class FetchTask implements Callable<GenericDTO> {

    String URL;
    int index;

    Gson GSON = new Gson();

    public FetchTask(String url, int index) {
        this.URL = url;
        this.index = index;
    }

    @Override
    public GenericDTO call() throws Exception {
        String data = HttpUtils.fetchData(URL);
        System.out.println(data);
        switch (index) {
            case 0:
                ChuckDTO chuck = GSON.fromJson(data, ChuckDTO.class);
                return chuck;
            case 1:
                DadDTO dad = GSON.fromJson(data, DadDTO.class);
                return dad;
            case 2:
                XkcdDTO xkcd = GSON.fromJson(data, XkcdDTO.class);
                return xkcd;
            case 3:
                DogDTO dog = GSON.fromJson(data, DogDTO.class);
                return dog;
            case 4:
                IpDTO zen = GSON.fromJson(data, IpDTO.class);
                return zen;
            default:
                return null;
        }
    }

}
