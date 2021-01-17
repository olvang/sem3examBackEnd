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
    private int pricePerYear;
    private String teamName;
    private int minAge;
    private int maxAge;
    private CoachListDTO coachList;
    private SportDTO sport;
    private MemberInfoListDTO memberInfoList;

    public SportTeamDTO(Long id, int pricePerYear,String teamName,int minAge,int maxAge,List<Coach> coachList,Sport sport,List<MemberInfo> memberInfoList) {
        this.id = id;
        this.pricePerYear = pricePerYear;
        this.teamName = teamName;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.coachList = new CoachListDTO(coachList);
        this.sport = new SportDTO(sport);
        this.memberInfoList = new MemberInfoListDTO(memberInfoList);
    }

    //Without entities list
    public SportTeamDTO(SportTeam sportTeam) {
        this.id = sportTeam.getId();
        this.pricePerYear = sportTeam.getPricePerYear();
        this.teamName = sportTeam.getTeamName();
        this.minAge = sportTeam.getMinAge();
        this.maxAge = sportTeam.getMaxAge();
        this.sport = new SportDTO(sportTeam.getSport());
        if(sportTeam.getCoachList() != null ) {
            this.coachList = new CoachListDTO(sportTeam.getCoachList());
        }
    }

    //Without lists
    public SportTeamDTO(Long id, int pricePerYear,String teamName,int minAge,int maxAge) {
        this.id = id;
        this.pricePerYear = pricePerYear;
        this.teamName = teamName;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPricePerYear() {
        return pricePerYear;
    }

    public void setPricePerYear(int pricePerYear) {
        this.pricePerYear = pricePerYear;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public CoachListDTO getCoachList() {
        return coachList;
    }

    public void setCoachList(CoachListDTO coachList) {
        this.coachList = coachList;
    }

    public SportDTO getSport() {
        return sport;
    }

    public void setSport(SportDTO sport) {
        this.sport = sport;
    }

    public MemberInfoListDTO getMemberInfoList() {
        return memberInfoList;
    }

    public void setMemberInfoList(MemberInfoListDTO memberInfoList) {
        this.memberInfoList = memberInfoList;
    }
}
