package hellojpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            insertMember(em);
//            findMember(em);
//            updateMember(em);
//            findResultList(em);
//            entityManaged(em);
//            entityManagerCash1Test1(em);
//            entityManagerCash1Test2(em);
//            updateMember2(em);
//            flushTest1(em);
//            detachAndClearTest(em);
//            columnMappingTest1(em);
//            generationTypeIdentityStrategy(em);
//            generationTypeTableStrategy(em);
//            seqStrategy(em);
//            getTeamInMember(em);
//            setOwnerOfRelationship(em);
//            relatedEntityAndMappedBy(em);
//            memberOneToMany(em);
//            inheritanceTypeAndDiscriminatorColumnTest(em);
//            mappingSuperClass(em);
//            initProxyTest(emf, em);
//            fetchTypeLazyTest(em);
//            cascadeTest(em);
//            immutableObject(em);
//            immutableObjectRefactor(em);

            List<Member1> result = em.createQuery("select m from Member1 m where m.username like '%kim'",
                            Member1.class)
                    .getResultList();

            for (Member1 m : result) {
                System.out.println("member = " + m);
            }



            /*
            List<Address> addressHistory = findMember.getAddressHistory();
            for (Address address : addressHistory) {
                System.out.println("address = " + address.getCity());
            }


            // ===== 수정
            // 주소 변경
            Address findAddr = findMember.getHomeAddress();
            member.setHomeAddress(new Address("newCity", findAddr.getStreet(), findAddr.getZipcode()));

            // 치킨 -> 한식으로 변경
            findMember.getFavoriteFood().remove("치킨");
            findMember.getFavoriteFood().add("한식");

            // addr history 변경

            //ADDRESS 테이블의 데이터 전부 delete 후 삭제되지 않은 데이터와 변경된 데이터를 insert(즉, insert 쿼리 2번 실행됨)
            //이러한 경우에는 값 타입 컬렉션 대신 엔티 일대다 관계를 고려.

            findMember.getAddressHistory().remove(new Address("old1", findAddr.getStreet(), findAddr.getZipcode()));
            findMember.getAddressHistory().add(new Address("newCity1", findAddr.getStreet(), findAddr.getZipcode()));

            */

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }





        em.close();
        emf.close();
    }

    private static void immutableObjectRefactor(EntityManager em) {
        Member1 member = new Member1();
        member.setUsername("memberA");
        member.setHomeAddress(new Address("city1", "street", "10000"));

        member.getFavoriteFood().add("치킨");
        member.getFavoriteFood().add("족발");
        member.getFavoriteFood().add("피자");

        // Address 대신 AddressEntity entity 를 생성하여 사용 (delete insert 가 아닌 update 쿼리 발생 (최적화) )
        member.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
        member.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

        em.persist(member);

        em.flush();
        em.clear();

        /**
         * 값 타입 컬렉션은 기본적으로 지연로딩임을 확인할 수 있다.
         * 또한 Cascade, 고아객체 제거 기능을 필수로 가지고 있다.
         */
        // ====== 조회
        Member1 findMember = em.find(Member1.class, member.getId());
        System.out.println("member = " + findMember);

        System.out.println("=============================");

        Set<String> favoriteFood = findMember.getFavoriteFood();
        for (String food : favoriteFood) {
            System.out.println("food = " + food);
        }
    }

    private static void immutableObject(EntityManager em) {
        /**
         * 값 타입 복사
         */
        Address address = new Address("city" ,"street", "zipcode");

        Member1 member = new Member1();
        member.setUsername("memberA");
        member.setHomeAddress(address);
//            member.setWorkPeriod(new Period());
        em.persist(member);


        // 값 타입을 setter를 사용하여 수정하는것은 지양해야한다. (임베디드는 객체타입이다. 여러곳에서 해당 임베디드를 공유하여 사용할때
        // 객체의 공유참조로 인하여 참조 값이 수정되면 참조하고 있는 모든곳에서 수정이 된다. 따라서 임베디드 타입에서 setter를 사용하지 않음으로 써
        // 수정을 불가하게(불변 객체) 만들어서 객체 타입의 한계를 사전예방을 하는 효과를 볼 수 있다.  다만 값의 수정이 필요할 경우 생성자를 통해 변경시마다
        // 새로운 객체를 생성하여 생성자 파라미터 값으로 수정하는 방법을 사용할 수 있다.
        // ****** 불변이라는 작은 제약으로 부작용이라는 큰 재앙을 막을 수 있다. ******

        Address newAddress = new Address("new City", address.getStreet(), address.getZipcode());
        member.setHomeAddress(newAddress);


        // setter를 사용하여 수정하는 부분은 주석처리
//            Address newAddress = new Address(address.getCity() , address.getStreet(), address.getZipcode());
//
//            Member1 member2 = new Member1();
//            member2.setUsername("memberB");
//            member2.setHomeAddress(newAddress);
//            em.persist(member2);
//
//            member.getHomeAddress().setCity("new city"); // 첫번째 member 주소 수정
    }

    private static void cascadeTest(EntityManager em) {
        Child child1 = new Child();
        Child child2 = new Child();

        Parent parent = new Parent();
        parent.addChild(child1);
        parent.addChild(child2);

        em.persist(parent);
        //Parent 엔티티의 Child 값에 CascadeType.ALL 설정으로 em.persist(parent) 만 해도 적용됨.
//            em.persist(child1);
//            em.persist(child1);

        em.flush();
        em.clear();

        Parent findParent = em.find(Parent.class, parent.getId());
        findParent.getChildList().remove(0);
    }

    private static void fetchTypeLazyTest(EntityManager em) {
        Team team = new Team();
        team.setName("teamA");
        em.persist(team);

        Team teamB = new Team();
        teamB.setName("teamB");
        em.persist(teamB);


        Member1 member = new Member1();
        member.setUsername("member1");
        member.setTeam(team);
        em.persist(member);

        Member1 member2 = new Member1();
        member2.setUsername("member2");
        member2.setTeam(teamB);
        em.persist(member2);

        em.flush();
        em.clear();

        // 1)
//            Member1 m = em.find(Member1.class, member.getId());
//            System.out.println("m = " + m.getId().getClass());
//            System.out.println("===========================");
//            m.getTeam().getName(); //초기화 발생
//            System.out.println("===========================");

        // 2)
        /**
         * 지연 로딩이지만(Member1 에서 Team FetchType.LAZY 상태) join fetch 로 한방 쿼리가 나가는것을 확인할 수 있다.
         * (지연로딩으로 설정되어 있다면 Member 와 연관된 team 엔티 에 대한 값을 사용할때만 select 쿼리가 실행이 된다. (프록시 초기화 하면서 쿼리 실행) )
         */
        List<Member1> members = em.createQuery("select m from Member1 m join fetch m.team", Member1.class)
                .getResultList();
    }

    private static void initProxyTest(EntityManagerFactory emf, EntityManager em) {
        MemberOTM member = new MemberOTM();
        member.setUsername("hello");
        em.persist(member);

        em.flush();
        em.clear();

//            Member findMember = em.find(Member.class, member.getId());
        Member refMember = em.getReference(Member.class, member.getId());
        System.out.println(refMember.getClass());
        Hibernate.initialize(refMember); // 프록시 강제 초기화 (select 쿼리 발생)
        System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));
    }

    private static void mappingSuperClass(EntityManager em) {
        Member member = new Member();
        member.setUsername("user1");
        member.setCreateBy1("dd");
        member.setCreatedDate1(LocalDateTime.now());
        em.persist(member);
    }

    private static void inheritanceTypeAndDiscriminatorColumnTest(EntityManager em) {
        Movie movie = new Movie();
        movie.setActor("aaaa");
        movie.setActor("bbbb");
        movie.setName("바람과 함께 사라지다.");
        movie.setPrice(10000);

        em.persist(movie);

        em.flush();
        em.clear();

//            Movie findMovie = em.find(Movie.class, movie.getId());

        // 만약 TABLE_PER_CLASS 전략에서 Item 의 Id를 찾는다면 Movie, Album, Book 을 모두 union 하여 select 한다는 단점이 존재
        Item item = em.find(Item.class, movie.getId());
        System.out.println("findMovie = " + item);
    }

    private static void memberOneToMany(EntityManager em) {
        MemberOTM member = new MemberOTM();
        member.setUsername("member1");

        em.persist(member);

        TeamOTM team = new TeamOTM();
        team.setName("teamA");
        team.getMembers().add(member);

        em.persist(team);
    }

    private static void relatedEntityAndMappedBy(EntityManager em) {
        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        MemberOwner member = new MemberOwner();
        member.setUsername("member1");
        member.setTeam(team);
        em.persist(member);

        team.addMember(member);

        em.flush();
        em.clear();

        System.out.println("===========================");
        Team findTeam = em.find(Team.class, team.getId());
        List<MemberOwner> members = findTeam.getMembers();

        for (MemberOwner m : members) {
            System.out.println("m = " + m.getUsername());
        }
    }

    private static void setOwnerOfRelationship(EntityManager em) {
        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        MemberOwner member = new MemberOwner();
        member.setUsername("member1");
//            member.setTeamId(team.getId());
        member.setTeam(team);
        em.persist(member);

        em.flush();
        em.clear();

        MemberOwner finMember = em.find(MemberOwner.class, member.getId());
        List<MemberOwner> members = finMember.getTeam().getMembers();

        for (MemberOwner m : members) {
            System.out.println("memberOwner = " + m.getUsername());
        }
    }

    private static void getTeamInMember(EntityManager em) {
        Team team = new Team();
        team.setName("TeamA");
        em.persist(team);

        MemberOwner member = new MemberOwner();
        member.setUsername("member1");
//            member.setTeamId(team.getId());
        member.setTeam(team);
        em.persist(member);

        MemberOwner findMember = em.find(MemberOwner.class, member.getId());
//            Long findTeamId = findMember.getTeamId();
//            Team findTeam = em.find(Team.class, findTeamId);
        Team findTeam = findMember.getTeam();
        System.out.println("findTeam.getName() = " + findTeam.getName());
    }

    private static void seqStrategy(EntityManager em) {
        MemberSeqStrategy mb1 = new MemberSeqStrategy();
        mb1.setUsername("A");

        MemberSeqStrategy mb2 = new MemberSeqStrategy();
        mb2.setUsername("B");

        MemberSeqStrategy mb3 = new MemberSeqStrategy();
        mb3.setUsername("C");

        System.out.println("===========================");

        em.persist(mb1);
        em.persist(mb2);
        em.persist(mb3);

        System.out.println("mb1 = " + mb1.getId());
        System.out.println("mb2 = " + mb2.getId());
        System.out.println("mb3 = " + mb3.getId());
    }

    private static void generationTypeTableStrategy(EntityManager em) {
        MemberTableStrategy mb = new MemberTableStrategy();
        mb.setUsername("F");

        em.persist(mb);
    }

    private static void generationTypeIdentityStrategy(EntityManager em) {
        Member member = new Member();
        member.setUsername("C");
        em.persist(member);
    }

    private static void columnMappingTest1(EntityManager em) {
        Member member = new Member();
        member.setId(4L);
        member.setUsername("D");
        member.setRoleType(RoleType.ADMIN);

        em.persist(member);
    }

    private static void detachAndClearTest(EntityManager em) {
        //영속성 컨텍스트
        Member member = em.find(Member.class, 150L);
//            member.setName("AAAA");

        em.detach(member); // 준영속성 컨텍스트 (영속성 컨텍스트가 관리하지 않는 상태)
//            em.clear(); // 영속성 컨텍스트 초기화
    }

    private static void flushTest1(EntityManager em) {
//        Member member = new Member(200L, "member200");
//        em.persist(member);

        // 영속성 컨텍스트를 비우지 않음
        em.flush(); // 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화
        System.out.println("==================");
    }

    private static void updateMember2(EntityManager em) {
        //영속성
        Member member = em.find(Member.class, 150L);
//        member.setName("ZZZZZ"); // update 쿼리 무조건 발생
    }

    private static void entityManagerCash1Test2(EntityManager em) {
        //비영속
//        Member member1 = new Member(150L, "A");
//        Member member2 = new Member(160L, "B");

        //영속
//        em.persist(member1);
//        em.persist(member2);
        System.out.println("=================");
    }

    private static void entityManagerCash1Test1(EntityManager em) {
        Member member = new Member();
        //비영속
//        member.setId(102L);
//        member.setName("TestJpa");

        //영속
        em.persist(member);

        // 영속성 컨텍스트의 1차 캐시에서 데이터 조회 (DB에서 조회 X)
        Member findMember = em.find(Member.class, 102L);
//        System.out.println("findMember = "+ findMember.getName());
    }

    private static void entityManaged(EntityManager em) {
        //비영속
        Member member = em.find(Member.class, 1L);
//        member.setId(100L);
//        member.setName("TestJpa");

        //영속
        em.persist(member);

        // 엔티티를 영속성 컨텍스트에서 분리해 준영속 상태로 만든다.
//        em.detach(member);
        // 영속성 콘텍스트를 비워도 관리되던 엔티티는 준영속 상태가 된다.
//        em.claer();
        // 영속성 콘텍스트를 종료해도 관리되던 엔티티는 준영속 상태가 된다.
//        em.close();
    }

    private static void findResultList(EntityManager em) {
        List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
                .getResultList();

        for (Member member : resultList) {
//            System.out.println("member.name = " + member.getName());
        }
    }

    private static void updateMember(EntityManager em) {
        Member member = em.find(Member.class, 1L);
//        member.setName("helloJpa");
    }

    private static void findMember(EntityManager em) {
        Member member = em.find(Member.class, 1L);
//        System.out.println("findMember.id = " + member.getId());
//        System.out.println("findMember.name = " + member.getName());
    }

    private static void insertMember(EntityManager em) {
        Member member = new Member();
//        member.setId(1L);
//        member.setName("HelloA");

        em.persist(member);
    }
}
