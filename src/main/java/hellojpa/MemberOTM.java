package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class MemberOTM {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "USER_NAME")
    private String username;

}
