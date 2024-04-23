package com.study.reservation.credit.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.credit.dto.CreditReadyDto;
import com.study.reservation.credit.dto.CreditSuccessDto;
import com.study.reservation.credit.entity.Credit;
import com.study.reservation.credit.etc.CreditStatus;
import com.study.reservation.credit.repository.CreditRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.order.entity.Order;
import com.study.reservation.order.etc.OrderStatus;
import com.study.reservation.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final OrderRepository orderRepository;
    private final CreditRepository creditRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public CreditReadyDto readyToCredit(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        //주문한 회원과 로그인한 회원이 다른 경우
        if (member.getId() != order.getMember().getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        if (order.getOrderStatus() != OrderStatus.READY_CREDIT) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        CreditReadyDto readyDto = new CreditReadyDto();
        readyDto.setOrderId(order.getId());
        readyDto.setMemberName(member.getName());
        readyDto.setPhoneNumber(member.getPhoneNumber());
        readyDto.setStartDate(order.getStartDate());
        readyDto.setEndDate(order.getEndDate());
        readyDto.setProductName(order.getRoom().getProduct().getProductName());
        readyDto.setRoomNum(order.getRoom().getRoomNum());
        readyDto.setPrice(order.getPrice());
        readyDto.setPoint(member.getPoint());

        return readyDto;
    }

    @Transactional
    public void successCredit(CreditSuccessDto dto, String email) {
        Member member = memberRepository.findByNameAndPhoneNumber(dto.getMemberName(), dto.getPhoneNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Member loginMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        //로그인 한 회원과 결제를 하는 회원이 다른 경우
        if (member.getId() != loginMember.getId()) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        //로그인 한 회원과 결제를 하는 회원이 다른 경우
        if (loginMember.getId() != order.getMember().getId()) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        //결제 완료 -> order 예약 완료
        order.setOrderStatus(OrderStatus.CONFIRM_ORDER);

        //포인트 차감
        member.usePoint(dto.getUsedPoint());

        //포인트 계산 및 적립 후 등급 조정
        int savedPoint = (int) Math.round(order.getPrice() * member.getMembership().getPointRate());
        member.savePoint(savedPoint);
        member.updateMembership();

        Credit credit = Credit.builder()
                .creditDate(LocalDateTime.now())
                .creditStatus(CreditStatus.CREDIT_FINISH)
                .savePoint(savedPoint)
                .impUid(dto.getImpUid())
                .order(order)
                .usePoint(dto.getUsedPoint())
                .totalPrice(dto.getTotalPrice())
                .build();

        creditRepository.save(credit);
    }

}
