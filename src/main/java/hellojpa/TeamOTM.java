package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@ToString // 양방 매핑시 무한루프 주의! (MemberOwner 와 Team 에 둘다 선언시 무한루프) StackOverflowError 발생
@Getter
@Setter
@Entity
public class TeamOTM {
    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID")
    private List<MemberOTM> members = new ArrayList<>();

}
