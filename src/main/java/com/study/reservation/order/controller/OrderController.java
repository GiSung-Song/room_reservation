package com.study.reservation.order.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.config.util.CommonUtils;
import com.study.reservation.order.dto.OrderDto;
import com.study.reservation.order.dto.OrderResDto;
import com.study.reservation.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order", description = "예약 API Document")
public class OrderController {

    private final OrderService orderService;
    private final CommonUtils commonUtils;
    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    @Operation(summary = "객실 예약(주문)", description = "객실 예약(주문)을 한다.")
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<String>> orderRoom(OrderDto orderDto) {
        String memberEmail = commonUtils.getAuthUsername();

        orderService.order(memberEmail, orderDto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "객실 예약을 성공했습니다."));
    }

    @Operation(summary = "객실 예약(주문) 조회", description = "객실 예약(주문)을 조회한다.")
    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderResDto>> orderInfo(@PathVariable("id") Long orderId) {
        String memberEmail = commonUtils.getAuthUsername();

        OrderResDto orderInfo = orderService.getOrderInfo(orderId, memberEmail);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "객실 예약 정보를 가져왔습니다.", orderInfo));
    }

    @Operation(summary = "예약 내역 조회", description = "예약 내역을 모두 조회한다.")
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResDto>>> orderList() {
        String memberEmail = commonUtils.getAuthUsername();

        List<OrderResDto> orderList = orderService.getOrderList(memberEmail);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "예약 내역을 조회했습니다.", orderList));
    }

}
