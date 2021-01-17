/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import dto.Lists.CoachListDTO;
import dto.Lists.MemberInfoListDTO;
import entities.Coach;
import entities.MemberInfo;
import entities.Sport;
import entities.SportTeam;
import java.util.List;


public class SportTeamDTO extends GenericDTO{
    Long id;
    private double pricePerYear;
    private String teamName;
    private int minAge;
    private int maxAge;
    private CoachListDTO coachList;
    private SportDTO sport;
    private MemberInfoListDTO memberInfoList;

    public SportTeamDTO(Long id, double pricePerYear,String teamName,int minAge,int maxAge,List<Coach> coachList,Sport sport,List<MemberInfo> memberInfoList) {
        this.id = id;
        this.pricePerYear = pricePerYear;
        this.teamName = teamName;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.coachList = new CoachListDTO(coachList);
        this.sport = new SportDTO(sport);
        this.memberInfoList = new MemberInfoListDTO(memberInfoList);
    }

    //Without entities
    public SportTeamDTO(SportTeam sportTeam) {
        this.id = sportTeam.getId();
        this.pricePerYear = sportTeam.getPricePerYear();
        this.teamName = sportTeam.getTeamName();
        this.minAge = sportTeam.getMinAge();
        this.maxAge = sportTeam.getMaxAge();
    }
    
}
