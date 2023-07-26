package hellojpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@ToString // 양방 매핑시 무한루프 주의! (MemberOwner 와 Team 에 둘다 선언시 무한루프) StackOverflowError 발생
@Getter
@Setter
@Entity
public class Team extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    @Column(name = "USER_NAME")
    private String name;

    @OneToMany(mappedBy = "team")
//    @JoinColumn(name = "TEAM_ID")
    private List<MemberOwner> members = new ArrayList<>();

    public void addMember(MemberOwner member) {
        member.setTeam(this);
        members.add(member);
    }
}
