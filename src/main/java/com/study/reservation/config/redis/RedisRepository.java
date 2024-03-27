package com.study.reservation.config.redis;

import com.study.reservation.config.jwt.filter.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RefreshToken, String> {
}
