package com.study.jpa.repository;

import com.study.jpa.dto.MemberDto;
import com.study.jpa.entity.Member;
import com.study.jpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}