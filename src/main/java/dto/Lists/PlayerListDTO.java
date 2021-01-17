package dto.Lists;

import dto.PlayerDTO;
import dto.SportDTO;
import entities.Player;
import entities.Sport;

import java.util.ArrayList;
import java.util.List;

public class PlayerListDTO {
    List<PlayerDTO> all = new ArrayList();

    public PlayerListDTO(List<Player> playerList) {
        playerList.forEach((p) -> {
            all.add(new PlayerDTO(p));
        });
    }

    public List<PlayerDTO> getAll() {
        return all;
    }
}
