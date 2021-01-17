package dto.Lists;

import dto.CoachDTO;
import dto.SportTeamDTO;
import entities.Coach;
import entities.SportTeam;

import java.util.ArrayList;
import java.util.List;

public class SportTeamListDTO {
    List<SportTeamDTO> all = new ArrayList();

    public SportTeamListDTO(List<SportTeam> sportTeamList) {
        sportTeamList.forEach((s) -> {
            all.add(new SportTeamDTO(s));
        });
    }

    public List<SportTeamDTO> getAll() {
        return all;
    }
}
