package hellojpa;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

//@ToString // 양방 매핑시 무한루프 주의! (MemberOwner 와 Team 에 둘다 선언시 무한루프) StackOverflowError 발생
@Getter
@Setter
@Entity
public class MemberOwner {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "USER_NAME")
    private String username;
//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;


}
