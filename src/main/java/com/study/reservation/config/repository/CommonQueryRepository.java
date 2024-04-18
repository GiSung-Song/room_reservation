package com.study.reservation.config.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.reservation.config.etc.SearchCondition;
import com.study.reservation.member.entity.QMember;
import com.study.reservation.order.dto.OrderDto;
import com.study.reservation.order.etc.OrderStatus;
import com.study.reservation.product.entity.QProduct;
import com.study.reservation.room.entity.Room;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

import static com.study.reservation.member.entity.QMember.*;
import static com.study.reservation.order.entity.QOrder.order;
import static com.study.reservation.product.entity.QProduct.*;
import static com.study.reservation.room.entity.QRoom.room;

@Repository
public class CommonQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public CommonQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Room> searchRoom(SearchCondition condition) {
        SubQueryExpression<Integer> subQuery = JPAExpressions
                .select(room.price.min())
                .from(room)
                .where(room.product.id.eq(room.product.id));

        return jpaQueryFactory
                .select(room)
                .from(room)
                .leftJoin(room.product).fetchJoin()
                .where(
                        room.id.notIn(
                                JPAExpressions.select(order.room.id)
                                        .from(order)
                                        .where(
                                                order.startDate.between(condition.getStartDate(), condition.getEndDate())
                                                        .or(order.endDate.between(condition.getStartDate(), condition.getEndDate()))
                                        )
                                        .where(
                                                order.orderStatus.eq(OrderStatus.CANCEL_ORDER)
                                                        .or(order.orderStatus.eq(OrderStatus.EMPTY_ORDER))
                                        )
                        ),
                        room.headCount.goe(condition.getCount()),
                        room.price.between(condition.getLowPrice(), condition.getHighPrice()),
                        room.price.eq(subQuery),
                        room.product.productName.like("%" + condition.getProductName() + "%"),
                        room.product.location.like("%" + condition.getLocation() + "%")
                )
                .groupBy(room.product.id)
                .orderBy(room.product.id.asc())
                .fetch();
    }

    public boolean isRoomOrder(Long roomId, OrderDto orderDto) {
        Long count = jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(order.room.id.eq(roomId)
                        .and(order.startDate.loe(orderDto.getEndDate()))
                        .and(order.endDate.goe(orderDto.getStartDate())))
                .fetchOne();

        return count > 0;
    }

    public boolean isPossibleWriteReview(Long memberId, Long productId) {
        Long count = jpaQueryFactory
                .select(order.count())
                .from(order)
                .join(order.room.product, product)
                .join(order.member, member)
                .where(
                        member.id.eq(memberId),
                        product.id.eq(productId),
                        loeEndDate(),
                        order.orderStatus.eq(OrderStatus.CONFIRM_ORDER)
                )
                .fetchOne();

        return count > 0;
    }

    private BooleanBuilder loeEndDate() {
        LocalDateTime date = LocalDateTime.now();

        DateTimeFormatter strDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strNowDate = date.format(strDateFormat);

        return nullSafeBuilder(() -> order.endDate.loe(strNowDate));
    }

    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }
}
