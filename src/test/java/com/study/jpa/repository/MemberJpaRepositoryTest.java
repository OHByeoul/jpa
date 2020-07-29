package com.study.jpa.repository;

import com.study.jpa.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest  {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        Member member = new Member("Membmer");
        Member savedMember = memberJpaRepository.save(member);

        Member getMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(savedMember.getId()).isEqualTo(getMember.getId());
        Assertions.assertThat(savedMember.getUsername()).isEqualTo(getMember.getUsername());
        Assertions.assertThat(savedMember).isEqualTo(member);
    }
}