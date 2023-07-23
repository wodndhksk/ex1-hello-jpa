package hellojpa;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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

            MemberOTM member = new MemberOTM();
            member.setUsername("member1");

            em.persist(member);

            TeamOTM team = new TeamOTM();
            team.setName("teamA");
            team.getMembers().add(member);

            em.persist(team);

            System.out.println("===========================");

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        em.close();
        emf.close();
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

        for(MemberOwner m : members){
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

        for(MemberOwner m : members){
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
