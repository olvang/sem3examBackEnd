/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import dto.Lists.SportTeamListDTO;
import entities.Sport;
import entities.SportTeam;
import java.util.List;

public class SportDTO extends GenericDTO{
    private Long id;
    private String name;
    private String description;
    private SportTeamListDTO sportTeamList;

    public SportDTO(Long id, String name, String description, List<SportTeam> sportTeam) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sportTeamList = new SportTeamListDTO(sportTeam);

    }

    //Without entities
    public SportDTO(Sport sport) {
        this.id = sport.getId();
        this.name = sport.getName();
        this.description = sport.getDescription();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SportTeamListDTO getSportTeamList() {
        return sportTeamList;
    }

    public void setSportTeamList(SportTeamListDTO sportTeamList) {
        this.sportTeamList = sportTeamList;
    }
}
