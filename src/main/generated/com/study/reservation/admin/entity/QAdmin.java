package com.study.reservation.admin.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = -243585001L;

    public static final QAdmin admin = new QAdmin("admin");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath companyNumber = createString("companyNumber");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath openDate = createString("openDate");

    public final StringPath owner = createString("owner");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final EnumPath<com.study.reservation.config.etc.Role> role = createEnum("role", com.study.reservation.config.etc.Role.class);

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

