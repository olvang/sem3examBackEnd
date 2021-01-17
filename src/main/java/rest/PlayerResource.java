package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PlayerDTO;
import dto.Lists.PlayerListDTO;
import dto.Lists.MemberInfoListDTO;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import facades.PlayerFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("player")
public class PlayerResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
       
    private static final PlayerFacade FACADE =  PlayerFacade.getPlayerFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String getPlayers() {
        PlayerListDTO players = FACADE.getPlayeres();
        return GSON.toJson(players.getAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String addPlayer(String playerJson) throws MissingInputException, AlreadyExistException, NotFoundException {
        PlayerDTO playerDTO = GSON.fromJson(playerJson, PlayerDTO.class);
        playerDTO = FACADE.addPlayer(playerDTO);
        return GSON.toJson(playerDTO);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String updatePlayer(String playerJson) throws MissingInputException, NotFoundException, AlreadyExistException {
        PlayerDTO playerDTO = GSON.fromJson(playerJson, PlayerDTO.class);
        playerDTO = FACADE.updatePlayer(playerDTO);
        return GSON.toJson(playerDTO);
    }

    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String deletePlayer(@PathParam("id") Long id) throws NotFoundException {
        PlayerDTO playerDTO = FACADE.deletePlayer(id);
        return GSON.toJson(playerDTO);
    }

    @Path("/{playerid}/team")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String getTeamsFromPlayer(@PathParam("playerid") Long playerId) throws NotFoundException, AlreadyExistException {
        MemberInfoListDTO memberInfoListDTO = FACADE.getTeamsFromPlayer(playerId);
        return GSON.toJson(memberInfoListDTO.getAll());
    }
}
