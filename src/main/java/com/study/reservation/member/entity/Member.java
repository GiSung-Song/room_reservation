package com.study.reservation.member.entity;

import com.study.reservation.config.etc.Role;
import com.study.reservation.member.etc.Membership;
import jakarta.persistence.*;
import lombok.*;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 10)
    private String nickname;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Membership membership = Membership.BRONZE;

    @Builder.Default()
    private int point = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_MEMBER;
}