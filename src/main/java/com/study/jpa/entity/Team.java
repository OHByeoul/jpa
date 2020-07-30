package com.study.jpa.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue
    @Column(name="team_id")
    private Long id;
    private String name;

    @OneToMany
    private List<Member> members = new ArrayList<>();
}