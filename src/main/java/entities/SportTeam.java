package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@NamedQuery(name = "SportTeam.deleteAllRows", query = "DELETE from SportTeam")
public class SportTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SportTeam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @NotNull
    private int pricePerYear;

    @Basic(optional = false)
    @NotNull
    private String teamName;

    @Basic(optional = false)
    @NotNull
    private int minAge;

    @Basic(optional = false)
    @NotNull
    private int maxAge;

    @ManyToMany(mappedBy = "sportTeamList", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Coach> coachList;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Sport sport;

    @OneToMany(mappedBy = "sportTeam", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MemberInfo> memberInfoList;

    public SportTeam(int pricePerYear, String teamName, int minAge, int maxAge, Sport sport) {
        this.pricePerYear = pricePerYear;
        this.teamName = teamName;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.sport = sport;
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

    public List<Coach> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<Coach> coachList) {
        this.coachList = coachList;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public List<MemberInfo> getMemberInfoList() {
        return memberInfoList;
    }

    public void setMemberInfoList(List<MemberInfo> memberInfoList) {
        this.memberInfoList = memberInfoList;
    }
}
