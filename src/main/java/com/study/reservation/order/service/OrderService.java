package com.study.reservation.order.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.order.dto.OrderDto;
import com.study.reservation.order.entity.Order;
import com.study.reservation.order.repository.OrderRepository;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CommonQueryRepository queryRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void order(Long roomId, String email, OrderDto orderDto) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        //방의 수용인원보다 더 많은 경우
        if (room.getHeadCount() < orderDto.getHeadCount()) {
            throw new CustomException(ErrorCode.NOT_VALID_REQUEST_HEAD_COUNT);
        }

        boolean isRoomOrdered = queryRepository.isRoomOrder(roomId, orderDto);

        //이미 해당 날짜의 예약이 되어있는 경우
        if (isRoomOrdered) {
            throw new CustomException(ErrorCode.NOT_VALID_REQUEST_ROOM_ORDER);
        }

        Order order = Order.builder()
                .startDate(orderDto.getStartDate())
                .endDate(orderDto.getEndDate())
                .price(room.getPrice())
                .build();

        member.addOrder(order);
        room.addOrder(order);

        orderRepository.save(order);
    }

}
