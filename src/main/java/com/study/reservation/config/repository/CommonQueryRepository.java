package com.study.reservation.config.repository;

import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.reservation.config.etc.SearchCondition;
import com.study.reservation.order.dto.OrderDto;
import com.study.reservation.room.entity.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.study.reservation.order.entity.QOrder.order;
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
                                .where(order.startDate.loe(condition.getEndDate()))
                                .where(order.endDate.goe(condition.getStartDate()))
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

}
