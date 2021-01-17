package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.CoachDTO;
import dto.Lists.CoachListDTO;
import dto.Lists.MemberInfoListDTO;
import dto.Lists.PlayerListDTO;
import dto.MemberInfoDTO;
import dto.PlayerDTO;
import entities.Player;
import errorhandling.AlreadyExistException;
import errorhandling.MissingInputException;
import errorhandling.NotFoundException;
import facades.CoachFacade;
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

    @Path("/{playerid}/team")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @RolesAllowed("admin")
    public String getTeamsFromPlayer(@PathParam("playerid") Long playerId) throws NotFoundException, AlreadyExistException {
        MemberInfoListDTO memberInfoListDTO = FACADE.getTeamsFromPlayer(playerId);
        return GSON.toJson(memberInfoListDTO.getAll());
    }
}
