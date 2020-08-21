package com.study.jpa.controller;

import com.study.jpa.dto.MemberDto;
import com.study.jpa.entity.Member;
import com.study.jpa.repository.MemberRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/member/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member user = memberRepository.findUserById(id);
        return user.getUsername();
    }

    @GetMapping("/member2/{id}")
    public String findMember2(@PathVariable("id") Member mem){
        return mem.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> findMembers(Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping("/members2")
    public Page<MemberDto> findMembers2(Pageable pageable){
        Page<Member> page = memberRepository.findAll(pageable); //엔티티로 값을 반환하는 것은 좋지 않다.
        //Page<MemberDto> dtoPage = page.map(member -> new MemberDto(member.getId(),member.getUsername(),null)); //프론트에 내려줄때는 엔티티로 하지말고 dto로 할것
        Page<MemberDto> dtoPage = page.map(MemberDto::new);
        return dtoPage;
    }

    @PostConstruct
    public void init(){
    //    memberRepository.save(new Member("mem1"));
        for (int i = 0; i<100; i++) {
            memberRepository.save(new Member("mem"+i,i));
        }
    }




}
