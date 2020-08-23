package com.study.jpa.repository;

import com.study.jpa.dto.MemberDto;
import com.study.jpa.entity.Member;
import com.study.jpa.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("mem");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());

    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //조회 1개
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        //카운트
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletecnt = memberRepository.count();
        assertThat(deletecnt).isEqualTo(0);
    }

    @Test
    public void 상위5개받아오기(){
        List<Member> top5By = memberRepository.findTop5By();
    }


    @Test
    public void testQuery(){
        Member member1 = new Member("member1",20);
        Member member2 = new Member("member2",10);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findUser("member2", 10);
        assertThat(findMember.get(0).getUsername()).isEqualTo(member2.getUsername());
    }

    @Test
    public void testMemberDto(){
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        Member member1 = new Member("member1",20);
        member1.setTeam(team1);
        memberRepository.save(member1);

        List<MemberDto> userByMemberDto = memberRepository.findUserByMemberDto();
        for (MemberDto memberDto : userByMemberDto) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    public void testCollection(){
        Member member = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        memberRepository.save(member);
        memberRepository.save(member2);

        List<Member> userByName = memberRepository.findUserByName(Arrays.asList("mem1", "mem2"));
        assertThat(userByName.get(0)).isEqualTo(member);
        assertThat(userByName.get(1)).isEqualTo(member2);

    }

    @DisplayName("단건조회")
    @Test
    public void testSingleResult(){
        Member member = new Member("mem1", 10);
        memberRepository.save(member);

        Member mem = memberRepository.findMemberByName("mem1");
        assertThat(mem.getUsername()).isEqualTo(member.getUsername());
    }

    @DisplayName("여러건조회")
    @Test
    public void testManyResult(){
        Member member = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        memberRepository.save(member);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.findMemberListByAge(10);
        assertThat(memberList.get(0)).isEqualTo(member);
    }

    @DisplayName("Optional조회")
    @Test
    public void testOptionalResult(){
        Member member = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        memberRepository.save(member);
        memberRepository.save(member2);

        Optional<Member> findMember = memberRepository.findOptionalMemberByName("mem1");
        System.out.println("member = " + findMember);
    }
/*
    @DisplayName("jpaPaing테스트")
    @Test
    public void findByAge(){
        Member member = new Member("mem1", 20);
        Member member2 = new Member("mem2", 20);
        Member member3 = new Member("mem3", 20);
        Member member4 = new Member("mem4", 20);
        Member member5 = new Member("mem5", 20);

        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age = 20;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

   //     Page<Member> page = memberRepository.findByAge(20, pageRequest);

        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();


    }
*/

    @DisplayName("jpa Slice 테스트")
    @Test
    public void findByName(){
        Member member = new Member("mem1", 20);
        Member member2 = new Member("mem2", 20);
        Member member3 = new Member("mem3", 20);
        Member member4 = new Member("mem4", 20);
        Member member5 = new Member("mem5", 20);

        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        int age = 20;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "age"));

        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

    }

    @DisplayName("jpa dto paging 테스트")
    @Test
    public void findUserForPageByName(){
        Member member = new Member("mem1", 20);
        Member member2 = new Member("mem1", 30);
        Member member3 = new Member("mem1", 40);
        Member member4 = new Member("mem1", 50);
        Member member5 = new Member("mem1", 60);

        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        String username = "mem1";

        PageRequest pageRequest = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "age"));

        Page<Member> userForPageByName = memberRepository.findUserForPageByUsername(username, pageRequest);

        Page<MemberDto> memDto = userForPageByName.map(mem -> new MemberDto(mem.getId(), mem.getUsername(), null));


        List<MemberDto> content = memDto.getContent();

        assertThat(content.size()).isEqualTo(4);
        assertThat(memDto.getNumber()).isEqualTo(0);
        assertThat(memDto.isFirst()).isTrue();
        assertThat(memDto.hasNext()).isTrue();

    }

    @DisplayName("data jpa 벌크업데이트")
    @Test
    public void bulkUpdate(){
        Member member = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        Member member3 = new Member("mem3", 30);

        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);

        int resultCount = memberRepository.bulkUpdate(20);
       // em.flush();
      //  em.clear();

        Member updatedMem = memberRepository.findMemberByName("mem2");
        Member updatedMem2 = memberRepository.findMemberByName("mem3");

        assertThat(updatedMem.getAge()).isEqualTo(21);
        assertThat(updatedMem2.getAge()).isEqualTo(31);

        assertThat(resultCount).isEqualTo(2);
    }

    @DisplayName("data jpa 엔티티 그래프 테스트")
    @Test
    public void findAllEntitiGraphTest(){
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member = new Member("mem1", 10);
        Member member2 = new Member("mem2", 20);
        member.setTeam(teamA);
        member.setTeam(teamB);
        memberRepository.save(member);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for (Member member1 : members) {
            System.out.println("member1 = " + member1);
            System.out.println("member1.team = " + member1.getTeam());
        }

        assertThat(members.get(0).getTeam().getClass()).isEqualTo(Team.class);
    }

    @DisplayName("쿼리힌트 테스트")
    @Test
    public void queryHint(){
        Member member1 = new Member("mem1", 12);
        Member member2 = new Member("mem1", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.clear();
        
        String username = "mem1";
        List<Member> memberByUsername = memberRepository.findMemberByUsername(username);
        for (Member member : memberByUsername) {
            member.setUsername("mem2");
        }

        em.flush();
        em.clear();

        List<Member> memberByUsername2 = memberRepository.findMemberByUsername(username);
        assertThat(memberByUsername2.get(0).getUsername()).isEqualTo("mem1");
        assertThat(memberByUsername2.get(1).getUsername()).isEqualTo("mem1");

    }


    @DisplayName("락 테스트")
    @Test
    public void testLock(){
        Member member1 = new Member("mem1", 12);
        Member member2 = new Member("mem1", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);
        em.flush();
        em.clear();

        String username = "mem1";
        List<Member> memberByUsername = memberRepository.findMemberLockByUsername(username);

    }

    @DisplayName("커스텀 테스트")
    @Test
    public void testCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @DisplayName("프로젝션 테스트")
    @Test
    public void testProejction(){
        Team team1 = new Team("team1");
        teamRepository.save(team1);

        Member member = new Member("mem1", 12, team1);
        Member member2 = new Member("mem2", 22,team1);
        memberRepository.save(member);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        String n2 = "mem1";
        List<UsernameOnly> projectionsByUsername = memberRepository.findProjectionsByUsername(n2);

        for (UsernameOnly usernameOnly : projectionsByUsername) {
            System.out.println("usernameOnly = " + usernameOnly);
        }


    }
}