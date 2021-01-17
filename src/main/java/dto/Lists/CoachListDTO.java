package dto.Lists;

import dto.CoachDTO;
import entities.Coach;
import java.util.ArrayList;
import java.util.List;

public class CoachListDTO {
    List<CoachDTO> all = new ArrayList();

    public CoachListDTO(List<Coach> coachList) {
        coachList.forEach((c) -> {
            all.add(new CoachDTO(c));
        });
    }

    public List<CoachDTO> getAll() {
        return all;
    }
}
