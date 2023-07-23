package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemberOTO {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    private TeamOTM team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

}
