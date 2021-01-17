package dto.Lists;

import dto.SportDTO;
import entities.Sport;

import java.util.ArrayList;
import java.util.List;

public class SportListDTO {
    List<SportDTO> all = new ArrayList();

    public SportListDTO(List<Sport> sportList) {
        sportList.forEach((s) -> {
            all.add(new SportDTO(s));
        });
    }

    public List<SportDTO> getAll() {
        return all;
    }
}
