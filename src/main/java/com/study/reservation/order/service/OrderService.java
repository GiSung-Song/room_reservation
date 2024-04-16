package com.study.reservation.order.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.order.dto.OrderDto;
import com.study.reservation.order.dto.OrderResDto;
import com.study.reservation.order.entity.Order;
import com.study.reservation.order.etc.OrderStatus;
import com.study.reservation.order.repository.OrderRepository;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CommonQueryRepository queryRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void order(String email, OrderDto orderDto) {
        Room room = roomRepository.findById(orderDto.getRoomId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        //방의 수용인원보다 더 많은 경우
        if (room.getHeadCount() < orderDto.getHeadCount()) {
            throw new CustomException(ErrorCode.NOT_VALID_REQUEST_HEAD_COUNT);
        }

        boolean isRoomOrdered = queryRepository.isRoomOrder(orderDto.getRoomId(), orderDto);

        //이미 해당 날짜의 예약이 되어있는 경우
        if (isRoomOrdered) {
            throw new CustomException(ErrorCode.NOT_VALID_REQUEST_ROOM_ORDER);
        }

        Order order = Order.builder()
                .startDate(orderDto.getStartDate())
                .endDate(orderDto.getEndDate())
                .price(room.getPrice())
                .orderStatus(OrderStatus.READY_CREDIT)
                .build();

        //연관관계 세팅
        member.addOrder(order);
        room.addOrder(order);

        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public OrderResDto getOrderInfo(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        if (order.getMember().getId() != member.getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        return OrderResDto.toDto(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResDto> getOrderList(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<Order> orders = member.getOrders();

        List<OrderResDto> orderList = new ArrayList<>();

        for (Order order : orders) {
            orderList.add(OrderResDto.toDto(order));
        }

        return orderList;
    }

    @Transactional
    public OrderResDto cancelOrder(Long orderId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        if (member.getId() != order.getMember().getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        order.setOrderStatus(OrderStatus.CANCEL_ORDER);

        return OrderResDto.toDto(order);
    }

}
