package com.study.jpa.dto;

import com.study.jpa.entity.Member;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private String name;


    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.name = teamName;
    }

    public MemberDto(Member member){ //dto는 엔티티를 바라봐도 괜찮다. 의존해도 괜찮음 다만 엔티티를 dto의 멤버변수에 넣으면 안된다.
        this.id = member.getId();
        this.username = member.getUsername();
    }




}
