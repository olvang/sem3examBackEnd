/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.MemberInfo;
import entities.Player;
import entities.SportTeam;
import java.util.Date;


public class MemberInfoDTO extends GenericDTO{
    private Long id;
    private boolean payed;
    private Date datePayed;
    private SportTeamDTO sportTeam;
    private PlayerDTO player;

    public MemberInfoDTO(Long id,  boolean payed, Date datePayed, SportTeam sportTeam, Player player) {
        this.id = id;
        this.payed = payed;
        this.datePayed = datePayed;
        this.sportTeam = new SportTeamDTO(sportTeam);
        this.player = new PlayerDTO(player);
    }

    public MemberInfoDTO(MemberInfo memberInfo) {
        this.id = memberInfo.getId();
        this.payed = memberInfo.isPayed();
        this.datePayed = memberInfo.getDatePayed();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    public void setDatePayed(Date datePayed) {
        this.datePayed = datePayed;
    }

    public SportTeamDTO getSportTeam() {
        return sportTeam;
    }

    public void setSportTeam(SportTeamDTO sportTeam) {
        this.sportTeam = sportTeam;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}
