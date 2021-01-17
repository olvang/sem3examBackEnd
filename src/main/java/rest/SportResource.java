package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Lists.SportListDTO;
import dto.SportDTO;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import facades.SportFacade;
import utils.EMF_Creator;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("sports")
public class SportResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final SportFacade FACADE =  SportFacade.getSportFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getSports() {
        SportListDTO sports = FACADE.getSports();
        return GSON.toJson(sports.getAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String addSport(String sportJson) throws MissingInputException, AlreadyExistException {
        SportDTO sportDTO = GSON.fromJson(sportJson, SportDTO.class);
        sportDTO = FACADE.addSport(sportDTO);
        return GSON.toJson(sportDTO);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String updateSport(String sportJson) throws MissingInputException, NotFoundException, AlreadyExistException {
        SportDTO sportDTO = GSON.fromJson(sportJson, SportDTO.class);
        sportDTO = FACADE.updateSport(sportDTO);
        return GSON.toJson(sportDTO);
    }

    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deleteSport(@PathParam("id") Long id) throws NotFoundException, MissingInputException {
        SportDTO sportDTO = FACADE.deleteSport(id);
        return GSON.toJson(sportDTO);
    }
}
