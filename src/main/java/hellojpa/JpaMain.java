package hellojpa;

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

            Member member = new Member();
            //비영속
            member.setId(102L);
            member.setName("TestJpa");

            //영속
            em.persist(member);

            // 영속성 컨텍스트의 1차 캐시에서 데이터 조회 (DB에서 조회 X)
            Member findMember = em.find(Member.class, 102L);
            System.out.println("findMember = "+ findMember.getName());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        em.close();
        emf.close();
    }

    private static void entityManaged(EntityManager em) {
        //비영속
        Member member = em.find(Member.class, 1L);
        member.setId(100L);
        member.setName("TestJpa");

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
            System.out.println("member.name = " + member.getName());
        }
    }

    private static void updateMember(EntityManager em) {
        Member member = em.find(Member.class, 1L);
        member.setName("helloJpa");
    }

    private static void findMember(EntityManager em) {
        Member member = em.find(Member.class, 1L);
        System.out.println("findMember.id = " + member.getId());
        System.out.println("findMember.name = " + member.getName());
    }

    private static void insertMember(EntityManager em) {
        Member member = new Member();
        member.setId(1L);
        member.setName("HelloA");

        em.persist(member);
    }
}
