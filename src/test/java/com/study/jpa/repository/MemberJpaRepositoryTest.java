package com.study.jpa.repository;

import com.study.jpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //테스트 일때, 끝난 뒤 롤백처리
@Rollback(false) // 롤백안하고 변경사항 커밋
class MemberJpaRepositoryTest  {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        Member member = new Member("Membmer");
        Member savedMember = memberJpaRepository.save(member);

        Member getMember = memberJpaRepository.find(savedMember.getId());

        assertThat(savedMember.getId()).isEqualTo(getMember.getId());
        assertThat(savedMember.getUsername()).isEqualTo(getMember.getUsername());
        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //조회 1개
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회
        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //카운트
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletecnt = memberJpaRepository.count();
        assertThat(deletecnt).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("mem1",10);
        Member m2 = new Member("mem2",20);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("mem2", 13);

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.get(0).getUsername()).isEqualTo("mem2");
        assertThat(members.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void 네임드쿼리테스트(){
        Member member1 = new Member("마마");
        Member member2 = new Member("바바");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMember = memberJpaRepository.namedQuery("마마");

        assertThat(findMember.get(0).getUsername()).isEqualTo(member1.getUsername());
    }

    @DisplayName("순수jpa페이징 테스트")
    @Test
    public void findByPaging(){
        Member member = new Member("mem1", 10);
        Member member2= new Member("mem2", 10);
        Member member3= new Member("mem3", 10);
        Member member4= new Member("mem4", 10);
        Member member5= new Member("mem5", 10);

        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);
        memberJpaRepository.save(member5);

        List<Member> byPage = memberJpaRepository.findByPage(10, 0, 4);
        Long totalCount = memberJpaRepository.totalCount(10);

        assertThat(byPage.size()).isEqualTo(4);
        assertThat(totalCount).isEqualTo(5);
    }

    @DisplayName("순수jpa 벌크업데이트")
    @Test
    public void bulkUpdate(){
        Member member = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        Member member3 = new Member("mem3", 30);

        memberJpaRepository.save(member);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);

        int resultCount = memberJpaRepository.bulkUpdate(20);
      //  List<Member> updatedMem = memberJpaRepository.findByUsername("mem2");
       // List<Member> updatedMem2 = memberJpaRepository.findByUsername("mem3");

        //System.out.println("result = " + result);
          assertThat(resultCount).isEqualTo(2);
          /*
        if(result > 0){
            assertThat(updatedMem.get(0).getAge()).isEqualTo(21);
            assertThat(updatedMem2.get(0).getAge()).isEqualTo(31);
        }*/
    }
}