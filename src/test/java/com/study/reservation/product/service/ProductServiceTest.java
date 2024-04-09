package com.study.reservation.product.service;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.etc.SearchCondition;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.product.dto.ProductListDto;
import com.study.reservation.product.dto.ProductRegisterDto;
import com.study.reservation.product.dto.ProductUpdateDto;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.etc.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Spy
    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CommonQueryRepository queryRepository;

    Admin setAdmin() {
        return Admin.builder()
                .password("1234")
                .companyNumber("1234123412")
                .owner("테스터")
                .accountNumber("12341234123412")
                .openDate("20240401")
                .build();
    }

    Product setProduct(Admin admin) {
        return Product.builder()
                .email("test@email.com")
                .productType(ProductType.HOTEL)
                .productName("테스트 호텔")
                .location("테스트시 테스트구 테스트동 42-123")
                .phoneNumber("01012341234")
                .description("테스트 호텔입니다.")
                .admin(admin)
                .build();
    }

    ProductRegisterDto setProductRegisterDto() {
        ProductRegisterDto dto = new ProductRegisterDto();
        dto.setProductType(ProductType.HOTEL);
        dto.setEmail("test@email.com");
        dto.setDescription("테스트 호텔입니다.");
        dto.setProductName("테스트 호텔");
        dto.setLocation("테스트시 테스트구 테스트동 42-123");
        dto.setPhoneNumber("01012341234");

        return dto;
    }

    @Nested
    class register {

        @DisplayName("등록 성공 테스트")
        @Test
        void 등록_성공_테스트() {
            Admin admin = setAdmin();
            ProductRegisterDto productRegisterDto = setProductRegisterDto();
            Product product = setProduct(admin);

            given(adminRepository.findByCompanyNumber(any())).willReturn(Optional.ofNullable(admin));
            given(productRepository.save(any())).willReturn(product);
            given(productRepository.findById(any())).willReturn(Optional.ofNullable(product));

            Long savedProductId = productService.registerProduct(productRegisterDto, admin.getCompanyNumber());
            Product savedProduct = productRepository.findById(savedProductId).get();

            assertEquals(savedProduct.getProductType(), productRegisterDto.getProductType());
            assertEquals(savedProduct.getEmail(), productRegisterDto.getEmail());
            assertEquals(savedProduct.getLocation(), productRegisterDto.getLocation());
        }

        @DisplayName("등록 실패 테스트")
        @Test
        void 등록_실패_테스트() {
            doThrow(new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER)).when(adminRepository).findByCompanyNumber(any());

            assertThrows(CustomException.class, () -> productService.registerProduct(any(), any()));
        }
    }

    @Nested
    class update {

        @DisplayName("수정 성공 테스트")
        @Test
        void 수정_성공_테스트() {
            Admin admin = setAdmin();
            Product product = setProduct(admin);
            Long fakeId = 0L;

            ReflectionTestUtils.setField(product, "id", fakeId);

            ProductUpdateDto productUpdateDto = new ProductUpdateDto();
            productUpdateDto.setProductName("새로운 숙소명");
            productUpdateDto.setDescription("새로운 설명");
            productUpdateDto.setLocation("새로운 위치");
            productUpdateDto.setEmail("new@test.com");
            productUpdateDto.setOperate(false);
            productUpdateDto.setPhoneNumber("01098765432");

            given(adminRepository.findByCompanyNumber(any())).willReturn(Optional.ofNullable(admin));
            given(productRepository.findById(any())).willReturn(Optional.ofNullable(product));

            productService.updateProduct(productUpdateDto, admin.getCompanyNumber(), fakeId);

            Product updateProduct = productRepository.findById(fakeId).get();

            assertEquals(updateProduct.getProductName(), productUpdateDto.getProductName());
            assertEquals(updateProduct.getEmail(), productUpdateDto.getEmail());
            assertEquals(updateProduct.getPhoneNumber(), productUpdateDto.getPhoneNumber());
        }

        @DisplayName("수정 실패 테스트")
        @Test
        void 수정_실패_테스트() {
            doThrow(new CustomException(ErrorCode.NOT_VALID_EMAIL)).when(productService).updateProduct(any(), any(), any());

            assertThrows(CustomException.class, () -> productService.updateProduct(any(), any(), any()));
        }
    }

    @Test
    @DisplayName("조건에 맞는 데이터 찾기")
    void 조건_객실_찾기() {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setHighPrice(1000000);
        searchCondition.setLowPrice(5000);
        searchCondition.setCount(4);
        searchCondition.setEndDate("20240409");
        searchCondition.setStartDate("20240401");
        searchCondition.setLocation("");
        searchCondition.setProductName("");

        Admin admin = setAdmin();
        Product product = setProduct(admin);

        Room room = Room.builder()
                .roomType(RoomType.DUPLEX_ROOM)
                .headCount(200)
                .price(20000)
                .product(product)
                .build();

        when(queryRepository.searchRoom(searchCondition)).thenReturn(List.of(room));

        List<ProductListDto> productList = productService.getProductList(searchCondition);

        assertNotNull(productList);
        assertFalse(productList.isEmpty());
        assertEquals(productList.get(0).getProductName(), product.getProductName());
    }

}