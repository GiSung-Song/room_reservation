package com.study.reservation.product.controller;

import com.study.reservation.config.etc.SearchCondition;
import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.product.dto.ProductDto;
import com.study.reservation.product.dto.ProductListDto;
import com.study.reservation.product.dto.ProductRegisterDto;
import com.study.reservation.product.dto.ProductUpdateDto;
import com.study.reservation.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Product", description = "숙소 API Document")
public class ProductController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final ProductService productService;

    @Operation(summary = "숙소 검색", description = "숙소를 검색한다.")
    @GetMapping("/product")
    public ResponseEntity<ApiResponse<List<ProductListDto>>> getProductList(
            @RequestParam(value = "startDate",   required = false, defaultValue = ""         ) String startDate,
            @RequestParam(value = "endDate",     required = false, defaultValue = ""         ) String endDate,
            @RequestParam(value = "location",    required = false, defaultValue = ""         ) String location,
            @RequestParam(value = "count",       required = false, defaultValue = "2"        ) String count,
            @RequestParam(value = "productName", required = false, defaultValue = ""         ) String productName,
            @RequestParam(value = "lowPrice",    required = false, defaultValue = "1"        ) String lowPrice,
            @RequestParam(value = "highPrice",   required = false, defaultValue = "999999999") String highPrice
    ) {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setStartDate(startDate);
        searchCondition.setEndDate(endDate);
        searchCondition.setLocation(location);
        searchCondition.setCount(Integer.parseInt(count));
        searchCondition.setProductName(productName);
        searchCondition.setLowPrice(Integer.parseInt(lowPrice));
        searchCondition.setHighPrice(Integer.parseInt(highPrice));

        List<ProductListDto> productList = productService.getProductList(searchCondition);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "해당 조건에 맞는 숙소를 조회했습니다.", productList));
    }

    @Operation(summary = "숙소 조회", description = "숙소를 상세 조회한다.")
    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductInfo(@PathVariable("id") Long productId) {
        ProductDto productDto = productService.infoProduct(productId);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "해당 숙소의 객실을 조회했습니다.", productDto));
    }

    @Operation(summary = "숙소 등록", description = "숙소를 등록한다.")
    @PostMapping("/product")
    public ResponseEntity<ApiResponse<String>> registerProduct(ProductRegisterDto productRegisterDto) {
        String companyNumber = getAuthCompanyNumber();

        productService.registerProduct(productRegisterDto, companyNumber);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "숙소 등록을 완료했습니다."));
    }

    @Operation(summary = "숙소 정보 수정", description = "숙소 정보를 수정한다.")
    @PatchMapping("/product/{id}")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable("id") Long productId, ProductUpdateDto productUpdateDto) {
        String companyNumber = getAuthCompanyNumber();

        productService.updateProduct(productUpdateDto, companyNumber, productId);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "숙소 정보를 수정했습니다."));
    }

    private String getAuthCompanyNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String companyNumber = principal.getUsername();

        return companyNumber;
    }
}
