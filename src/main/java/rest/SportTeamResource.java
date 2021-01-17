package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Lists.MemberInfoListDTO;
import dto.Lists.SportTeamListDTO;
import dto.SportDTO;
import dto.SportTeamDTO;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import facades.SportTeamFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("sportteams")
public class SportTeamResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final SportTeamFacade FACADE = SportTeamFacade.getSportTeamFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getSportTeams() {
        SportTeamListDTO sportsTeams = FACADE.getSportTeams();
        return GSON.toJson(sportsTeams.getAll());
    }

    @Path("/sport/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String addSportTeam(String sportTeamJson, @PathParam("id") long sportId) throws AlreadyExistException, NotFoundException, MissingInputException {
        SportTeamDTO sportTeamDTO = GSON.fromJson(sportTeamJson, SportTeamDTO.class);
        sportTeamDTO = FACADE.addSportTeam(sportTeamDTO, sportId);
        return GSON.toJson(sportTeamDTO);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String updateSportTeam(String sportTeamJson) throws AlreadyExistException, NotFoundException, MissingInputException {
        SportTeamDTO sportTeamDTO = GSON.fromJson(sportTeamJson, SportTeamDTO.class);
        sportTeamDTO = FACADE.updateSportTeam(sportTeamDTO);
        return GSON.toJson(sportTeamDTO);
    }

    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deleteSportTeam(@PathParam("id") Long id) throws NotFoundException {
        SportTeamDTO sportTeamDTO = FACADE.deleteSportTeam(id);
        return GSON.toJson(sportTeamDTO);
    }

    @Path("/{teamid}/players")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String getPlayersFromTeam(@PathParam("teamid") Long teamid) throws NotFoundException, AlreadyExistException {
        MemberInfoListDTO memberInfoListDTO = FACADE.getPlayersFromTeam(teamid);
        return GSON.toJson(memberInfoListDTO.getAll());
    }
}
