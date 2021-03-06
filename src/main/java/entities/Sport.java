package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@NamedQuery(name = "Sport.deleteAllRows", query = "DELETE from Sport")
public class Sport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public Sport() {
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic(optional = false)
    @NotNull
    private String name;

    @Basic(optional = false)
    @NotNull
    private String description;

    @OneToMany(mappedBy = "sport", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<SportTeam> sportTeamList;

    public Sport(String name, String description) {
        this.name = name;
        this.description = description;
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

    public List<SportTeam> getSportTeamList() {
        return sportTeamList;
    }
}
