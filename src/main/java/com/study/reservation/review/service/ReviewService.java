package com.study.reservation.review.service;

import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.order.repository.OrderRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.review.dto.MemberReviewDto;
import com.study.reservation.review.dto.ProductReviewDetailDto;
import com.study.reservation.review.dto.ProductReviewDto;
import com.study.reservation.review.dto.WriteReviewDto;
import com.study.reservation.review.entity.Review;
import com.study.reservation.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CommonQueryRepository commonQueryRepository;

    @Transactional(readOnly = true)
    public ProductReviewDto getProductReviewList(Long productId) {
        List<Review> reviewList = reviewRepository.findByProductId(productId);

        ProductReviewDto productReviewDto = new ProductReviewDto();
        List<ProductReviewDetailDto> dtoList = new ArrayList<>();

        for (Review review : reviewList) {
            ProductReviewDetailDto productReviewDetailDto = new ProductReviewDetailDto();
            productReviewDetailDto.setReviewId(review.getId());
            productReviewDetailDto.setNickname(review.getMember().getNickname());
            productReviewDetailDto.setComment(review.getComment());
            productReviewDetailDto.setRating(review.getRating());
            productReviewDetailDto.setTime(review.getTime());

            dtoList.add(productReviewDetailDto);
        }

        int reviewCount = (int) reviewList.stream()
                .filter(review -> review.getComment() != null)
                .count();

        BigDecimal sum = reviewList.stream()
                .map(Review::getRating)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int ratingCount = (int) reviewList.stream()
                .filter(rating -> rating.getRating() != null)
                .count();

        BigDecimal ratingAvg = BigDecimal.ZERO;

        if (ratingCount > 0) {
            ratingAvg = sum.divide(BigDecimal.valueOf(ratingCount), 1, RoundingMode.HALF_UP);
        }

        //리뷰 개수
        productReviewDto.setReviewCount(reviewCount);
        //리뷰 리스트
        productReviewDto.setProductReviewDetailDtoList(dtoList);
        //평균 평점
        productReviewDto.setRatingAvg(ratingAvg);

        return productReviewDto;
    }

    @Transactional(readOnly = true)
    public List<MemberReviewDto> getWriteReviewList(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        List<Review> reviewList = reviewRepository.findByMemberId(member.getId());

        List<MemberReviewDto> writeList = new ArrayList<>();

        for (Review review : reviewList) {
            MemberReviewDto memberReviewDto = new MemberReviewDto();

            memberReviewDto.setProductName(review.getProduct().getProductName());
            memberReviewDto.setComment(review.getComment());
            memberReviewDto.setRating(review.getRating());
            memberReviewDto.setTime(review.getTime());

            writeList.add(memberReviewDto);
        }

        return writeList;
    }

    @Transactional
    public void writeReview(Long productId, String email, WriteReviewDto dto) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        boolean possibleWriteReview = commonQueryRepository.isPossibleWriteReview(member.getId(), productId);

        if (possibleWriteReview == false) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_WRITE_REVIEW);
        }

        if (!StringUtils.hasText(dto.getComment()) && dto.getRating() == null) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        Review findReview = reviewRepository.findByMemberIdAndProductId(member.getId(), productId);

        //한 번도 해당 상품에 대해서 리뷰를 남겨본 적이 없는 경우
        if (findReview == null) {
            //별점이랑 리뷰를 둘 다 남긴 경우
            if (StringUtils.hasText(dto.getComment()) && dto.getRating() != null) {
                findReview = Review.builder()
                        .rating(dto.getRating())
                        .comment(dto.getComment())
                        .member(member)
                        .product(product)
                        .build();
            } else if (StringUtils.hasText(dto.getComment())) { // 리뷰만 있는 경우
                findReview = Review.builder()
                        .comment(dto.getComment())
                        .member(member)
                        .product(product)
                        .build();
            } else if (dto.getRating() != null) { // 별점만 남긴 경우
                findReview = Review.builder()
                        .rating(dto.getRating())
                        .member(member)
                        .product(product)
                        .build();
            }
            //경우에 대해서 저장 (새로 생긴 리뷰)
            reviewRepository.save(findReview);
        } else { //상품에 대한 리뷰가 있는 경우에는 update
            if (StringUtils.hasText(dto.getComment()) && dto.getRating() != null) {
                findReview.updateComment(dto.getComment());
                findReview.updateRating(dto.getRating());
                findReview.updateTime();
            } else if (StringUtils.hasText(dto.getComment())) {
                findReview.updateComment(dto.getComment());
                findReview.updateTime();;
            } else if (dto.getRating() != null) {
                findReview.updateRating(dto.getRating());
                findReview.updateTime();
            }
        }

    }

    @Transactional
    public void deleteReview(Long reviewId, String email) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        Product product = productRepository.findById(review.getProduct().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        //해당 리뷰 작성한 사람이 아니면
        if (review.getMember().getId() != member.getId()) {
            throw new CustomException(ErrorCode.NOT_ACCEPT_GET_INFO_MEMBER);
        }

        Review findReview = reviewRepository.findByMemberIdAndProductId(member.getId(), product.getId());

        if (findReview == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_REVIEW);
        }

        reviewRepository.delete(findReview);
    }
}
