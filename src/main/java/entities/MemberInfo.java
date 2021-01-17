package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@NamedQuery(name = "MemberInfo.deleteAllRows", query = "DELETE from MemberInfo")
public class MemberInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public MemberInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @NotNull
    private boolean payed;

    @Basic(optional = false)
    @NotNull
    private Date datePayed;

    @ManyToOne
    private SportTeam sportTeam;

    @ManyToOne
    private Player player;

    public MemberInfo(boolean payed, Date datePayed, SportTeam sportTeam, Player player) {
        this.payed = payed;
        this.datePayed = datePayed;
        this.sportTeam = sportTeam;
        this.player = player;
    }

    public boolean isPayed() {
        return payed;
    }

    public SportTeam getSportTeam() {
        return sportTeam;
    }

    public void setSportTeam(SportTeam sportTeam) {
        this.sportTeam = sportTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
}
