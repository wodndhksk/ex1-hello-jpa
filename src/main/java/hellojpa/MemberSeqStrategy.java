package hellojpa;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBERSEQSTRATEGY_SEQ",  // 매핑할 데이터베이스 시퀀스명
        initialValue = 1, // 1씩 증가
        allocationSize = 50 //default = 50
)
public class MemberSeqStrategy {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBERSEQSTRATEGY_SEQ")
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    public MemberSeqStrategy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
