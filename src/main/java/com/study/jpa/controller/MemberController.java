package com.study.jpa.controller;

import com.study.jpa.entity.Member;
import com.study.jpa.repository.MemberRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    @PostConstruct
    public void init(){
        memberRepository.save(new Member("mem1"));

    }




}