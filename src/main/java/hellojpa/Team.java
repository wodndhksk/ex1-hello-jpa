package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    @Column(name = "USER_NAME")
    private String name;

    @OneToMany(mappedBy = "team")
//    @JoinColumn(name = "TEAM_ID")
    private List<MemberOwner> members = new ArrayList<>();
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MemberOwner> getMembers() {
        return members;
    }

    public void setMembers(List<MemberOwner> teams) {
        this.members = teams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
