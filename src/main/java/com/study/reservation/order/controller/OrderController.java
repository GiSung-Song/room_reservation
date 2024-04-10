package com.study.reservation.order.controller;

import com.study.reservation.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order", description = "예약 API Document")
public class OrderController {

    private final OrderService orderService;
    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

}
