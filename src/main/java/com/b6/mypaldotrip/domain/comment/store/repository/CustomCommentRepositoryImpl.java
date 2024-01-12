package com.b6.mypaldotrip.domain.comment.store.repository;

import static com.b6.mypaldotrip.domain.comment.store.entity.QCommentEntity.commentEntity;

import com.b6.mypaldotrip.domain.comment.exception.CommentErrorCode;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentEntity;
import com.b6.mypaldotrip.domain.comment.store.entity.CommentSort;
import com.b6.mypaldotrip.domain.user.store.entity.UserEntity;
import com.b6.mypaldotrip.global.exception.GlobalException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<CommentEntity> getCommentListByDynamicConditions(
            Long courseId, Pageable pageable, CommentSort commentSort, UserEntity userEntity) {

        return jpaQueryFactory
                .selectFrom(commentEntity)
                .where(courseIdEq(courseId), isFollowing(userEntity))
                .orderBy(commentSort(commentSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression courseIdEq(Long courseId) {
        return courseId != null ? commentEntity.courseEntity.courseId.eq(courseId) : null;
    }

    private BooleanExpression isFollowing(UserEntity userEntity) {
        return userEntity != null
                ? commentEntity.userEntity.followerList.any().followedUser.eq(userEntity)
                : null;
    }

    public OrderSpecifier<?> commentSort(CommentSort commentSort) {
        switch (commentSort) {
            case MODIFIED -> {
                return commentEntity.modifiedAt.desc();
            }
            case LEVEL -> {
                return commentEntity.userEntity.level.desc();
            }
            default -> throw new GlobalException(CommentErrorCode.WRONG_COMMENT_SORT);
        }
    }
}
