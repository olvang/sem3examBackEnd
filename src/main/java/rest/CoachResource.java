package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CoachDTO;
import dto.Lists.CoachListDTO;
import dto.Lists.SportListDTO;
import dto.SportDTO;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import facades.CoachFacade;
import facades.SportFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("coach")
public class CoachResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final CoachFacade FACADE =  CoachFacade.getCoachFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String getCoaches() {
        CoachListDTO coaches = FACADE.getCoaches();
        return GSON.toJson(coaches.getAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String addCoach(String coachJson) throws MissingInputException, AlreadyExistException {
        CoachDTO coachDTO = GSON.fromJson(coachJson, CoachDTO.class);
        coachDTO = FACADE.addCoach(coachDTO);
        return GSON.toJson(coachDTO);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String updateCoach(String coachJson) throws MissingInputException, NotFoundException, AlreadyExistException {
        CoachDTO coachDTO = GSON.fromJson(coachJson, CoachDTO.class);
        coachDTO = FACADE.updateCoach(coachDTO);
        return GSON.toJson(coachDTO);
    }

    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deleteCoach(@PathParam("id") Long id) throws NotFoundException {
        CoachDTO coachDTO = FACADE.deleteCoach(id);
        return GSON.toJson(coachDTO);
    }
}
