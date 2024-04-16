package com.study.reservation.credit.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.credit.dto.CreditCancelDto;
import com.study.reservation.credit.dto.CreditReadyDto;
import com.study.reservation.credit.dto.CreditRequestDto;
import com.study.reservation.credit.dto.CreditResponseDto;
import com.study.reservation.credit.entity.Credit;
import com.study.reservation.credit.etc.CreditStatus;
import com.study.reservation.credit.payment.dto.KakaoApproveResponseDto;
import com.study.reservation.credit.payment.dto.KakaoCancelResponseDto;
import com.study.reservation.credit.payment.dto.KakaoReadyResponseDto;
import com.study.reservation.credit.payment.service.KakaoPayService;
import com.study.reservation.credit.repository.CreditRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.order.entity.Order;
import com.study.reservation.order.etc.OrderStatus;
import com.study.reservation.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final OrderRepository orderRepository;
    private final CreditRepository creditRepository;
    private final MemberRepository memberRepository;
    private final KakaoPayService kakaoPayService;

    @Transactional
    public KakaoReadyResponseDto readyToCredit(Long orderId, String email, CreditRequestDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        //주문한 회원과 로그인한 회원이 다른 경우
        if (member.getId() != order.getMember().getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        //사용하려는 포인트가 보유 포인트보다 적은 경우
        if (member.getPoint() < dto.getUsePoint()) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_POINT);
        }

        CreditReadyDto readyDto = new CreditReadyDto();
        readyDto.setProductName(order.getRoom().getProduct().getProductName());
        readyDto.setMemberId(member.getId());
        readyDto.setRoomId(order.getRoom().getId());
        readyDto.setTotalPrice(order.getPrice() - dto.getUsePoint());
        readyDto.setOrderId(order.getId());

        Credit readyCredit = Credit.builder()
                .order(order)
                .totalPrice(order.getPrice() - dto.getUsePoint())
                .usePoint(dto.getUsePoint())
                .build();

        creditRepository.save(readyCredit);

        //카카오페이 서비스 준비
        KakaoReadyResponseDto kakaoReadyResponseDto = kakaoPayService.readyToKakaoPay(readyDto);

        return kakaoReadyResponseDto;
    }

    @Transactional
    public CreditResponseDto creditSuccess(KakaoApproveResponseDto dto) {
        Long orderId = Long.valueOf(dto.getPartner_order_id());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Credit credit = creditRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CREDIT));

        //예약한 회원
        Member member = order.getMember();

        //결제한 회원
        Long memberId = Long.valueOf(dto.getPartner_user_id());
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        //결제하는 회원과 예약한 회원이 다르면 throw
        if (member.getId() != findMember.getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        //예약 완료로 변경
        order.setOrderStatus(OrderStatus.CONFIRM_ORDER);

        //사용한 포인트 차감
        member.usePoint(credit.getUsePoint());

        //적립할 포인트 계산
        double pointRate = member.getMembership().getPointRate();
        Integer savePoint = (int) Math.round(pointRate * order.getPrice());

        //결제 시간 및 결제 상태, 포인트 update
        credit.updateCreditDate();
        credit.updateCreditStatus(CreditStatus.CREDIT_FINISH);
        credit.updateSavePoint(savePoint);

        //포인트 적립
        member.savePoint(savePoint);

        //포인트 적립 시 등급 상승할 수 있으니
        member.updateMembership();

        CreditResponseDto creditResponseDto = new CreditResponseDto();

        //credit 정보 set
        creditResponseDto.setCreditId(credit.getId());
        creditResponseDto.setCreditPrice(credit.getTotalPrice());
        creditResponseDto.setSavePoint(savePoint);
        creditResponseDto.setUsePoint(credit.getUsePoint());

        //order 정보 set
        creditResponseDto.setOrderId(orderId);
        creditResponseDto.setProductName(order.getRoom().getProduct().getProductName());
        creditResponseDto.setLocation(order.getRoom().getProduct().getLocation());
        creditResponseDto.setRoomNum(order.getRoom().getRoomNum());
        creditResponseDto.setStartDate(order.getStartDate());
        creditResponseDto.setEndDate(order.getEndDate());

        return creditResponseDto;
    }

    @Transactional
    public KakaoCancelResponseDto refundPay(Long orderId, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Credit credit = creditRepository.findByOrder_Id(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CREDIT));

        //주문한 회원과 로그인한 회원이 다른 경우
        if (member.getId() != order.getMember().getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        //예약 상태가 예약완료(결제완료)가 아닌 경우
        if (order.getOrderStatus() != OrderStatus.CONFIRM_ORDER) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        String nowDate = getNowDateFormatStr();

        //이미 기간이 종료되거나 당일인 경우 불가능
        if (order.getStartDate().compareTo(nowDate) >= 0) {
            throw new CustomException(ErrorCode.NOT_VALID_REFUND_DAY);
        }

        //dto 생성
        CreditCancelDto creditCancelDto = new CreditCancelDto();
        creditCancelDto.setTid(credit.getTid());
        creditCancelDto.setCancelAmount(String.valueOf(credit.getTotalPrice()));

        //카카오페이 환불 api 호출
        KakaoCancelResponseDto kakaoCancelResponseDto = kakaoPayService.refundToKakaoPay(creditCancelDto);

        //회원의 적립된 포인트 차감 및 등급 조정, 회원이 사용한 포인트 환불
        member.refundPoint(credit.getUsePoint());
        member.cancelSavePoint(credit.getSavePoint());
        member.updateMembership();

        //주문 취소로 상태 변경
        order.setOrderStatus(OrderStatus.CANCEL_ORDER);

        //결제 환불로 상태 변경
        credit.updateCreditStatus(CreditStatus.CREDIT_REFUND);

        return kakaoCancelResponseDto;
    }

    private String getNowDateFormatStr() {
        LocalDate date = LocalDate.now();

        DateTimeFormatter strDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String strNowDate = date.format(strDateFormat);

        return strNowDate;
    }

}
