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
            List<Member> resultList = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();

            for(Member member : resultList){
                System.out.println("member.name = " + member.getName());
            }


            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }


        em.close();
        emf.close();
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
