package com.study.reservation.config.redis;

import com.study.reservation.config.jwt.filter.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface RedisRepository extends CrudRepository<RefreshToken, String> {
}
