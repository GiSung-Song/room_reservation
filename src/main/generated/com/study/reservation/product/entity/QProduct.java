package com.study.reservation.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 335224087L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.study.reservation.admin.entity.QAdmin admin;

    public final StringPath description = createString("description");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isOperate = createBoolean("isOperate");

    public final StringPath location = createString("location");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final StringPath productName = createString("productName");

    public final EnumPath<com.study.reservation.product.etc.ProductType> productType = createEnum("productType", com.study.reservation.product.etc.ProductType.class);

    public final ListPath<com.study.reservation.room.entity.Room, com.study.reservation.room.entity.QRoom> rooms = this.<com.study.reservation.room.entity.Room, com.study.reservation.room.entity.QRoom>createList("rooms", com.study.reservation.room.entity.Room.class, com.study.reservation.room.entity.QRoom.class, PathInits.DIRECT2);

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new com.study.reservation.admin.entity.QAdmin(forProperty("admin")) : null;
    }

}

