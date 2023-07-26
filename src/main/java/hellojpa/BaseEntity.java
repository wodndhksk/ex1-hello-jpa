package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    private String createBy1;
    private LocalDateTime createdDate1;
    private String lastModifiedBy1;
    private String lastModifiedDate1;


}
