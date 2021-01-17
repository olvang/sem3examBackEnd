package dto.Lists;

import dto.CoachDTO;
import dto.MemberInfoDTO;
import entities.Coach;
import entities.MemberInfo;

import java.util.ArrayList;
import java.util.List;

public class MemberInfoListDTO {
    List<MemberInfoDTO> all = new ArrayList();

    public MemberInfoListDTO(List<MemberInfo> memberInfos) {
        memberInfos.forEach((m) -> {
            all.add(new MemberInfoDTO(m));
        });
    }

    public List<MemberInfoDTO> getAll() {
        return all;
    }
}
