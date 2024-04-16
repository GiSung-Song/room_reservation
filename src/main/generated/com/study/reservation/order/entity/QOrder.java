package com.study.reservation.order.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = 1299441271L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final StringPath endDate = createString("endDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.study.reservation.member.entity.QMember member;

    public final EnumPath<com.study.reservation.order.etc.OrderStatus> orderStatus = createEnum("orderStatus", com.study.reservation.order.etc.OrderStatus.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.study.reservation.room.entity.QRoom room;

    public final StringPath startDate = createString("startDate");

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.study.reservation.member.entity.QMember(forProperty("member")) : null;
        this.room = inits.isInitialized("room") ? new com.study.reservation.room.entity.QRoom(forProperty("room"), inits.get("room")) : null;
    }

}

