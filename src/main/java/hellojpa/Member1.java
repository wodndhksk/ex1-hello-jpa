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

    // 기간 Period
    @Embedded
    private Period workPeriod;

    // 주소
    @Embedded
    private Address homeAddress;

    /**
     * 한 엔티티 안에서 같은 값 타입(Address)을 사용하는 방법
     */
    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
    )
    private Address workAddress;
}
