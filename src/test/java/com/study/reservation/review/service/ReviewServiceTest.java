package com.study.reservation.review.service;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.review.dto.MemberReviewDto;
import com.study.reservation.review.dto.ProductReviewDto;
import com.study.reservation.review.dto.WriteReviewDto;
import com.study.reservation.review.entity.Review;
import com.study.reservation.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    CommonQueryRepository commonQueryRepository;

    @InjectMocks
    ReviewService reviewService;


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

    @Test
    @DisplayName("상품 리뷰 조회 테스트")
    void 상품_리뷰_조회_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        Long productId = 1L;
        ReflectionTestUtils.setField(product, "id", 1L);

        Review review1 = Review.builder()
                .id(1L)
                .comment("리뷰1")
                .member(member)
                .product(product)
                .build();

        Review review2 = Review.builder()
                .id(2L)
                .comment("리뷰2")
                .rating(BigDecimal.valueOf(2.5))
                .member(member)
                .product(product)
                .build();

        Review review3 = Review.builder()
                .id(3L)
                .rating(BigDecimal.valueOf(2.5))
                .member(member)
                .product(product)
                .build();

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);
        reviewList.add(review3);

        when(reviewRepository.findByProductId(productId)).thenReturn(reviewList);

        ProductReviewDto result = reviewService.getProductReviewList(productId);

        assertNotNull(result);
        assertEquals(3, result.getProductReviewDetailDtoList().size());
        assertEquals(2, result.getReviewCount());
        assertEquals(BigDecimal.valueOf(2.5), result.getRatingAvg());
    }

    @Test
    @DisplayName("자신이 쓴 상품 리뷰 조회 테스트")
    void 자신이_쓴_리뷰_조회_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        Long memberId = 1L;
        ReflectionTestUtils.setField(member, "id", 1L);

        Review review1 = Review.builder()
                .id(1L)
                .comment("리뷰1")
                .member(member)
                .product(product)
                .build();

        Review review2 = Review.builder()
                .id(2L)
                .comment("리뷰2")
                .rating(BigDecimal.valueOf(2.5))
                .member(member)
                .product(product)
                .build();

        Review review3 = Review.builder()
                .id(3L)
                .rating(BigDecimal.valueOf(2.5))
                .member(member)
                .product(product)
                .build();

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);
        reviewList.add(review3);

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));
        when(reviewRepository.findByMemberId(memberId)).thenReturn(reviewList);

        List<MemberReviewDto> writeReviewList = reviewService.getWriteReviewList(member.getEmail());

        assertNotNull(writeReviewList);
        assertEquals(3, writeReviewList.size());
    }

    @Test
    @DisplayName("리뷰 달기 테스트1")
    void 리뷰_달기_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        Long memberId = 1L;
        Long productId = 2L;
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(product, "id", 2L);

        WriteReviewDto reviewDto = new WriteReviewDto();
        reviewDto.setComment("1234");

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));

        // commonQueryRepository mock 설정
        when(commonQueryRepository.isPossibleWriteReview(anyLong(), anyLong())).thenReturn(true);

        // reviewRepository mock 설정
        when(reviewRepository.findByMemberIdAndProductId(anyLong(), anyLong())).thenReturn(null);

        reviewService.writeReview(productId, member.getEmail(), reviewDto);

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 수정 테스트1")
    void 리뷰_수정_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        Review review = Review.builder()
                .comment("리뷰1")
                .rating(BigDecimal.valueOf(1.2))
                .member(member)
                .product(product)
                .build();

        Long memberId = 1L;
        Long productId = 2L;
        Long reviewId = 3L;
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(product, "id", 2L);
        ReflectionTestUtils.setField(review, "id", 2L);

        WriteReviewDto reviewDto = new WriteReviewDto();
        reviewDto.setComment("1234");

        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.ofNullable(member));
        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(product));

        // commonQueryRepository mock 설정
        when(commonQueryRepository.isPossibleWriteReview(anyLong(), anyLong())).thenReturn(true);

        // reviewRepository mock 설정
        when(reviewRepository.findByMemberIdAndProductId(anyLong(), anyLong())).thenReturn(review);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.ofNullable(review));
        reviewService.writeReview(productId, member.getEmail(), reviewDto);

        Review findReview = reviewRepository.findById(reviewId).get();

        assertEquals("1234", findReview.getComment());
    }

}