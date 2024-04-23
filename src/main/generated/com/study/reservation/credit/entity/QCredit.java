package com.study.reservation.credit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCredit is a Querydsl query type for Credit
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCredit extends EntityPathBase<Credit> {

    private static final long serialVersionUID = 873100995L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCredit credit = new QCredit("credit");

    public final DateTimePath<java.time.LocalDateTime> creditDate = createDateTime("creditDate", java.time.LocalDateTime.class);

    public final EnumPath<com.study.reservation.credit.etc.CreditStatus> creditStatus = createEnum("creditStatus", com.study.reservation.credit.etc.CreditStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath impUid = createString("impUid");

    public final com.study.reservation.order.entity.QOrder order;

    public final NumberPath<Integer> savePoint = createNumber("savePoint", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public final NumberPath<Integer> usePoint = createNumber("usePoint", Integer.class);

    public QCredit(String variable) {
        this(Credit.class, forVariable(variable), INITS);
    }

    public QCredit(Path<? extends Credit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCredit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCredit(PathMetadata metadata, PathInits inits) {
        this(Credit.class, metadata, inits);
    }

    public QCredit(Class<? extends Credit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new com.study.reservation.order.entity.QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

