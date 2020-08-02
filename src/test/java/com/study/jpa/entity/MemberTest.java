package com.study.jpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 없으면 current thread에러남
class MemberTest {
    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("nam1", 18, teamA);
        Member member2 = new Member("nam2", 19, teamA);
        Member member3 = new Member("nam3", 20, teamB);
        Member member4 = new Member("nam4", 21, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();
        em.clear(); //영속성컨텍스트의 캐시를 다 날림

        List<Member> resultList = em.createQuery("select a from Member a", Member.class).getResultList();

        for (Member member : resultList) {
            System.out.println("member = " + member);
            System.out.println("member.team = " + member.getTeam());
        }
    }
}