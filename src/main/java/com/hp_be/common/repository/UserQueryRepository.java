package com.hp_be.common.repository;

import com.hp_be.common.dao.QUser;
import com.hp_be.common.dao.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;


    // 이메일 중복 체크
    public boolean existsByEmail(String email) {
        return queryFactory
                .selectFrom(user)
                .where(
                        user.provider.isNull(),
                        user.emailVerified.isTrue(),
                        user.userId.isNotNull(), // 이메일 인증만 했을 경우 대비
                        user.email.eq(email))
                .fetchFirst() != null;
    }

    // 이메일 발송 전, 토큰 업데이트를 위해 가져오기
    public Optional<User> findUnverifiedByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(user)
                        .where(
                                user.provider.isNull(),
                                user.emailVerified.isFalse(),
                                user.email.eq(email))
                        .fetchFirst()
        );
    }

    // 인증 완료 확인
    public Optional<User> findVerifiedByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(user)
                        .where(
                                user.provider.isNull(),
                                user.emailVerified.isTrue(),
                                user.userId.isNull(),
                                user.email.eq(email)
                        )
                        .fetchOne()
        );
    }
}
