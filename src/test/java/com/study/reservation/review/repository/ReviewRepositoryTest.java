package com.study.reservation.review.repository;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.member.entity.Member;
import com.study.reservation.member.repository.MemberRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.review.entity.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ReviewRepository reviewRepository;

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

    Review makeReview(Member member, Product product) {
        Review review = Review.builder()
                .comment("12345")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product)
                .build();

        return review;
    }

    @Test
    @DisplayName("리뷰 저장 테스트")
    void 저장_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        adminRepository.save(admin);
        productRepository.save(product);
        memberRepository.save(member);

        Review review = makeReview(member, product);

        Review savedReview = reviewRepository.save(review);

        assertEquals(review.getComment(), savedReview.getComment());
        assertEquals(review.getRating(), savedReview.getRating());
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void 삭제_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        adminRepository.save(admin);
        productRepository.save(product);
        memberRepository.save(member);

        Review review = makeReview(member, product);

        Review savedReview = reviewRepository.save(review);
        reviewRepository.delete(savedReview);

        Optional<Review> deleteReview = reviewRepository.findById(savedReview.getId());

        Assertions.assertThat(deleteReview).isEmpty();
    }

    @Test
    @DisplayName("ID로 조회 테스트")
    void 조회_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        adminRepository.save(admin);
        productRepository.save(product);
        memberRepository.save(member);

        Review review = makeReview(member, product);

        Long savedId = reviewRepository.save(review).getId();

        Review findReview = reviewRepository.findById(savedId).get();

        assertEquals(findReview.getRating(), review.getRating());
        assertEquals(findReview.getComment(), review.getComment());
    }

    @Test
    @DisplayName("회원 ID로 조회")
    void 회원_ID로_조회_테스트() {
        Admin admin = makeAdmin();
        Product product = makeProduct(admin);
        Member member = makeMember();

        adminRepository.save(admin);
        productRepository.save(product);
        memberRepository.save(member);

        List<Review> reviewList = new ArrayList<>();

        Review review = Review.builder()
                .comment("1")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product)
                .build();

        Review review2 = Review.builder()
                .comment("2")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product)
                .build();

        Review review3 = Review.builder()
                .comment("3")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product)
                .build();

        reviewList.add(review);
        reviewList.add(review2);
        reviewList.add(review3);

        reviewRepository.save(review);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        List<Review> findByMemberId = reviewRepository.findByMemberId(member.getId());

        assertEquals(3, findByMemberId.size());
    }

    @Test
    @DisplayName("상품 ID로 조회")
    void 상품_ID로_조회_테스트() {
        Admin admin = makeAdmin();
        Admin admin2 = Admin.builder()
                .password("1234")
                .phoneNumber("01012341236")
                .owner("테스터")
                .accountNumber("12341234123415")
                .openDate("20230101")
                .companyNumber("1234123416")
                .build();

        adminRepository.save(admin);
        adminRepository.save(admin2);

        Product product = makeProduct(admin);
        Product product2 = Product.builder()
                .email("test2@test.com")
                .description("테스트 설명입니다.")
                .phoneNumber("01012341254")
                .productName("테스트 숙소")
                .productType(ProductType.HOTEL)
                .location("테스트시 테스트구 테스트동 41-23")
                .admin(admin2)
                .isOperate(true)
                .build();

        productRepository.save(product);
        productRepository.save(product2);

        Member member = makeMember();
        memberRepository.save(member);

        List<Review> reviewList = new ArrayList<>();

        Review review = Review.builder()
                .comment("1")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product)
                .build();

        Review review2 = Review.builder()
                .comment("2")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product2)
                .build();

        Review review3 = Review.builder()
                .comment("3")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product2)
                .build();

        reviewList.add(review);
        reviewList.add(review2);
        reviewList.add(review3);

        reviewRepository.save(review);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        List<Review> findProduct = reviewRepository.findByProductId(product.getId());
        List<Review> findProduct2 = reviewRepository.findByProductId(product2.getId());

        assertEquals(1, findProduct.size());
        assertEquals(2, findProduct2.size());
    }

    @Test
    @DisplayName("상품 ID, 회원ID로 조회")
    void 상품_ID_회원_ID로_조회_테스트() {
        Admin admin = makeAdmin();
        Admin admin2 = Admin.builder()
                .password("1234")
                .phoneNumber("01012341236")
                .owner("테스터")
                .accountNumber("12341234123415")
                .openDate("20230101")
                .companyNumber("1234123416")
                .build();

        adminRepository.save(admin);
        adminRepository.save(admin2);

        Product product = makeProduct(admin);
        Product product2 = Product.builder()
                .email("test2@test.com")
                .description("테스트 설명입니다.")
                .phoneNumber("01012341254")
                .productName("테스트 숙소")
                .productType(ProductType.HOTEL)
                .location("테스트시 테스트구 테스트동 41-23")
                .admin(admin2)
                .isOperate(true)
                .build();

        productRepository.save(product);
        productRepository.save(product2);

        Member member = makeMember();
        Member member2 = Member.builder()
                .email("test2@test.com")
                .phoneNumber("01012345679")
                .name("테스터")
                .nickname("테스트2")
                .password("test1234")
                .build();

        memberRepository.save(member);
        memberRepository.save(member2);

        List<Review> reviewList = new ArrayList<>();

        Review review = Review.builder()
                .comment("1")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product)
                .build();

        Review review2 = Review.builder()
                .comment("2")
                .rating(BigDecimal.valueOf(2.7))
                .member(member)
                .product(product2)
                .build();

        Review review3 = Review.builder()
                .comment("3")
                .rating(BigDecimal.valueOf(2.7))
                .member(member2)
                .product(product2)
                .build();

        reviewList.add(review);
        reviewList.add(review2);
        reviewList.add(review3);

        reviewRepository.save(review);
        reviewRepository.save(review2);
        reviewRepository.save(review3);

        Review find2 = reviewRepository.findByMemberIdAndProductId(member2.getId(), product2.getId());

        assertEquals(BigDecimal.valueOf(2.7), find2.getRating());
    }

}