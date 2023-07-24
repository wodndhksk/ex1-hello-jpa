package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // 부모 객체의 테이블 생성 X, 자식 객체(테이블)만 생성 및 부모 컬럼을 각각 중복으로 포함 [지양]
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 테이블 하나만 생성 [성능 향샹 및 테이블 단순화]
@Inheritance(strategy = InheritanceType.JOINED) //테이블 여러개 생성, select 시 조인 [정석 방식]
@DiscriminatorColumn // 부모 클래스에 선언한다. 하위 클래스를 구분하는 용도의 컬럼(ex = DTYPE), name 설정 가능
public abstract class Item { // 추상 클래스로 사용해야 TABLE_PER_CLASS 전략시 Item 테이블이 생성되지 않는다.

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;
}
