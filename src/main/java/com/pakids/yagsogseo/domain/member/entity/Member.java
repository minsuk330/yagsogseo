package com.pakids.yagsogseo.domain.member.entity;

import com.pakids.yagsogseo.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column
    private Provider provider;

    @Column
    private String providerId;

    @Builder
    private Member(String email, String password, String name, Role role, Provider provider, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static Member createLocal(String email, String password, String name) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.ROLE_USER)
                .provider(Provider.LOCAL)
                .build();
    }

    public static Member createOAuth(String email, String name, Provider provider, String providerId) {
        return Member.builder()
                .email(email)
                .name(name)
                .role(Role.ROLE_USER)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }
}
