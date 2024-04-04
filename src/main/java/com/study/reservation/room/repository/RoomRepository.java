package com.study.reservation.room.repository;

import com.study.reservation.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<List<Room>> findByProduct_Id(Long productId);
}
