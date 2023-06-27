package com.example.common.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {
    @Test
    void 회원_생성_성공() {
        Member member = Member.builder()
                .name("고객")
                .point(100)
                .address(Address.builder()
                        .city("대구시")
                        .street("달서구")
                        .zipcode("신당동")
                        .build())
                .build();

        assertThat(member.getName()).isNotBlank();
        assertThat(member.getPoint()).isGreaterThan(0);
        assertThat(member.getAddress()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 회원_생성_실패__이름이_공백_혹은_널(String name) {
        assertThatThrownBy(() -> Member.builder()
                .name(name)
                .point(100)
                .address(Address.builder()
                        .city("대구시")
                        .street("달서구")
                        .zipcode("신당동")
                        .build())
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 회원_생성_실패__포인트가_0보다_작음() {
        assertThatThrownBy(() -> Member.builder()
                .name("회원")
                .point(-1)
                .address(Address.builder()
                        .city("대구시")
                        .street("달서구")
                        .zipcode("신당동")
                        .build())
                .build()).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 회원_생성_실패__주소가_널() {
        assertThatThrownBy(() -> Member.builder()
                .name("회원")
                .point(100)
                .address(null)
                .build()).isInstanceOf(IllegalArgumentException.class);
    }
}