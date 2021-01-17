/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import dto.Lists.SportTeamListDTO;
import entities.Coach;
import entities.SportTeam;
import java.util.List;

public class CoachDTO extends GenericDTO{
    private Long id;
    private String name;
    private String email;
    private String phone;
    private SportTeamListDTO sportTeamList;
    
    public CoachDTO(Long id, String name, String email, String phone, List<SportTeam> sportTeamList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.sportTeamList = new SportTeamListDTO(sportTeamList);
    }

    //Without entities
    public CoachDTO(Coach coach) {
        this.id = coach.getId();
        this.name = coach.getName();
        this.email = coach.getEmail();
        this.phone = coach.getEmail();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SportTeamListDTO getSportTeamList() {
        return sportTeamList;
    }

    public void setSportTeamList(SportTeamListDTO sportTeamList) {
        this.sportTeamList = sportTeamList;
    }
}
