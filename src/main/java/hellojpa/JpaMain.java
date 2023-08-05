package hellojpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
            Member1 member = new Member1();
            member.setUsername("memberAA");
            member.setHomeAddress(new Address("city" ,"street", "zipcode"));
            member.setWorkPeriod(new Period());

            em.persist(member);


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        em.close();
        emf.close();
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
