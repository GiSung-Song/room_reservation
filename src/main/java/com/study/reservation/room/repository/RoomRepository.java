package com.study.reservation.room.repository;

import com.study.reservation.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByProduct_Id(Long productId);
}
