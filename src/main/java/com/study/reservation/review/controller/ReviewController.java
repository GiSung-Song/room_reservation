package com.study.reservation.review.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.config.util.CommonUtils;
import com.study.reservation.review.dto.ProductReviewDto;
import com.study.reservation.review.dto.WriteReviewDto;
import com.study.reservation.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 API Document")
public class ReviewController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final CommonUtils commonUtils;
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "리뷰를 등록한다.")
    @PostMapping("/product/{id}/review")
    public ResponseEntity<ApiResponse<String>> writeReview(@PathVariable("id") Long productId, WriteReviewDto dto) {
        String email = commonUtils.getAuthUsername();


        reviewService.writeReview(productId, email, dto);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "리뷰를 등록했습니다."));
    }

    @Operation(summary = "리뷰 조회", description = "상품 리뷰를 조회한다.")
    @GetMapping("/product/{id}/review")
    public ResponseEntity<ApiResponse<ProductReviewDto>> getProductReviewList(@PathVariable("id") Long productId) {
        ProductReviewDto productReviewList = reviewService.getProductReviewList(productId);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "리뷰를 조회했습니다.", productReviewList));
    }

    @Operation(summary = "리뷰 삭제", description = "상품 리뷰를 삭제한다.")
    @DeleteMapping("/product/{id}/review")
    public ResponseEntity<ApiResponse<String>> deleteProductReview(@PathVariable("id") Long productId, Long reviewId) {
        String email = commonUtils.getAuthUsername();

        reviewService.deleteReview(reviewId, email);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "리뷰를 삭제했습니다."));
    }
}
