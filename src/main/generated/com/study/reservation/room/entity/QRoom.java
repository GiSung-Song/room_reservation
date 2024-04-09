package com.study.reservation.room.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoom is a Querydsl query type for Room
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoom extends EntityPathBase<Room> {

    private static final long serialVersionUID = 1760089863L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoom room = new QRoom("room");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> headCount = createNumber("headCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOperate = createBoolean("isOperate");

    public final ListPath<com.study.reservation.order.entity.Order, com.study.reservation.order.entity.QOrder> orders = this.<com.study.reservation.order.entity.Order, com.study.reservation.order.entity.QOrder>createList("orders", com.study.reservation.order.entity.Order.class, com.study.reservation.order.entity.QOrder.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.study.reservation.product.entity.QProduct product;

    public final StringPath roomNum = createString("roomNum");

    public final EnumPath<com.study.reservation.room.etc.RoomType> roomType = createEnum("roomType", com.study.reservation.room.etc.RoomType.class);

    public QRoom(String variable) {
        this(Room.class, forVariable(variable), INITS);
    }

    public QRoom(Path<? extends Room> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoom(PathMetadata metadata, PathInits inits) {
        this(Room.class, metadata, inits);
    }

    public QRoom(Class<? extends Room> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.study.reservation.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

