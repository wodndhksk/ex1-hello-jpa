package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Member1 {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "USER_NAME")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY) // LAZY => 지연로딩, EAGER => 즉시로딩
    @JoinColumn
    private Team team;
}
