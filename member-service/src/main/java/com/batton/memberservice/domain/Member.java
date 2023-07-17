package com.batton.memberservice.domain;


import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private String profileImage;

    private Status status;

    @Builder
    public Member(Long id, String email, String nickname, String password, Authority authority, String profileImage, Status status) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
        this.profileImage = profileImage;
        this.status = status;
    }
}