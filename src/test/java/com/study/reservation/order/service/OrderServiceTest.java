package com.study.reservation.order.service;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.member.dto.MemberSignUpDto;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.order.dto.OrderDto;
import com.study.reservation.order.dto.OrderResDto;
import com.study.reservation.order.entity.Order;
import com.study.reservation.order.etc.OrderStatus;
import com.study.reservation.order.repository.OrderRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.etc.RoomType;
import com.study.reservation.room.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommonQueryRepository queryRepository;

    Admin makeAdmin() {
        Admin admin = Admin.builder()
                .password("1234")
                .phoneNumber("01012341234")
                .owner("테스터")
                .accountNumber("12341234123412")
                .openDate("20230103")
                .companyNumber("1234123412")
                .build();

        return admin;
    }

    Product makeProduct(Admin admin) {
        return Product.builder()
                .email("test@test.com")
                .description("테스트 설명입니다.")
                .phoneNumber("01012341234")
                .productName("테스트 숙소")
                .productType(ProductType.RESORT)
                .location("테스트시 테스트구 테스트동 41-23")
                .admin(admin)
                .isOperate(true)
                .build();
    }

    Room makeRoom() {
        return Room.builder()
                .roomNum("1024")
                .description("테스트 설명입니다.")
                .roomType(RoomType.TWIN_ROOM)
                .price(200000)
                .headCount(8)
                .build();
    }

    private Member makeMember() {
        Member member = Member.builder()
                .email("test@test.com")
                .phoneNumber("01012345678")
                .name("테스터")
                .nickname("테스트")
                .password("test1234")
                .build();

        return member;
    }

    private Order makeOrder() {
        return Order.builder()
                .startDate("20240401")
                .endDate("20240401")
                .price(200000)
                .build();
    }

    @Test
    @DisplayName("예약 성공 테스트")
    void 예약_성공_테스트() {
        Room room = makeRoom();
        Long roomId = 0L;

        Member member = makeMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setStartDate("20241010");
        orderDto.setEndDate("20241012");
        orderDto.setHeadCount(2);
        orderDto.setRoomId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.ofNullable(room));
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));
        when(queryRepository.isRoomOrder(roomId, orderDto)).thenReturn(false);

        assertDoesNotThrow(() -> orderService.order(member.getEmail(), orderDto));

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("예약 실패 테스트")
    void 예약_실패_테스트() {
        Room room = makeRoom();
        Long roomId = 0L;

        Member member = makeMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setStartDate("20241010");
        orderDto.setEndDate("20241012");
        orderDto.setHeadCount(10);
        orderDto.setRoomId(roomId);

        when(roomRepository.findById(roomId)).thenReturn(Optional.ofNullable(room));
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));

        assertThrows(CustomException.class, () -> orderService.order(member.getEmail(), orderDto));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("예약 조회 성공 테스트")
    void 예약_조회_성공_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();
        Room room = makeRoom();
        product.addRoom(room);
        Order order = makeOrder();
        order.setMember(member);
        order.setRoom(room);
        Long fakeId = 1L;

        when(orderRepository.findById(fakeId)).thenReturn(Optional.ofNullable(order));
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));

        OrderResDto orderInfo = orderService.getOrderInfo(fakeId, member.getEmail());

        assertNotNull(orderInfo);

        assertEquals(orderInfo.getPrice(), room.getPrice());
    }

    @Test
    @DisplayName("예약 조회 실패 테스트")
    void 예약_조회_실패_테스트1() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();
        Room room = makeRoom();
        product.addRoom(room);
        Order order = makeOrder();
        order.setMember(member);
        order.setRoom(room);
        Long fakeId = 1L;

        when(orderRepository.findById(fakeId)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> orderService.getOrderInfo(fakeId, member.getEmail()));
    }

    @Test
    @DisplayName("예약 조회 실패 테스트")
    void 예약_조회_실패_테스트2() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();
        Room room = makeRoom();
        product.addRoom(room);
        Order order = makeOrder();
        member.addOrder(order);
        room.addOrder(order);
        Long fakeId = 1L;

        when(orderRepository.findById(fakeId)).thenReturn(Optional.ofNullable(order));
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> orderService.getOrderInfo(fakeId, member.getEmail()));
    }

    @Test
    @DisplayName("예약 내역 조회 테스트")
    void 예약_내역_조회_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();
        Room room = makeRoom();
        product.addRoom(room);

        Order order = makeOrder();
        member.addOrder(order);
        room.addOrder(order);

        Order order2 = makeOrder();
        member.addOrder(order2);
        room.addOrder(order2);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));

        List<OrderResDto> orderList = orderService.getOrderList(member.getEmail());

        assertNotNull(orderList);
        assertEquals(2, orderList.size());
        assertEquals(order.getPrice(), orderList.get(0).getPrice());
        assertEquals(order2.getPrice(), orderList.get(1).getPrice());
    }

    @Test
    @DisplayName("예약 취소 테스트")
    void 예약_취소_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();
        Room room = makeRoom();
        product.addRoom(room);

        Order order = makeOrder();
        member.addOrder(order);
        room.addOrder(order);

        Long fakeId = 1L;
        ReflectionTestUtils.setField(order, "id", fakeId);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));
        when(orderRepository.findById(fakeId)).thenReturn(Optional.ofNullable(order));

        OrderResDto orderResDto = orderService.cancelOrder(fakeId, member.getEmail());

        assertEquals(orderResDto.getProductName(), order.getRoom().getProduct().getProductName());
        assertEquals(orderResDto.getOrderStatus(), OrderStatus.CANCEL_ORDER);
    }
}