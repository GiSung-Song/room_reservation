package com.study.reservation.room.repository;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.etc.RoomType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

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

    @Test
    @DisplayName("저장 테스트")
    void 저장_테스트() {

        Admin admin = makeAdmin();
        Product product = makeProduct(admin);

        adminRepository.save(admin);
        productRepository.save(product);

        Room room = Room.builder()
                .roomNum("1024")
                .roomType(RoomType.TWIN_ROOM)
                .price(100000)
                .headCount(4)
                .product(product)
                .build();

        Room savedRoom = roomRepository.save(room);

        Assertions.assertThat(savedRoom.getRoomNum()).isEqualTo(room.getRoomNum());
        Assertions.assertThat(savedRoom.getPrice()).isEqualTo(room.getPrice());
        Assertions.assertThat(savedRoom.getRoomType()).isEqualTo(room.getRoomType());
    }

    @Test
    @DisplayName("조회 테스트")
    void 조회_테스트() {

        Admin admin = makeAdmin();
        Product product = makeProduct(admin);

        adminRepository.save(admin);
        productRepository.save(product);

        Room room = Room.builder()
                .roomNum("1024")
                .roomType(RoomType.TWIN_ROOM)
                .price(100000)
                .headCount(4)
                .product(product)
                .build();

        Long savedId = roomRepository.save(room).getId();
        Room findRoom = roomRepository.findById(savedId).get();

        Assertions.assertThat(findRoom.getRoomNum()).isEqualTo(room.getRoomNum());
        Assertions.assertThat(findRoom.getPrice()).isEqualTo(room.getPrice());
        Assertions.assertThat(findRoom.getRoomType()).isEqualTo(room.getRoomType());
    }

    @Test
    @DisplayName("상품ID로 조회 테스트")
    void 상품ID로_조회_테스트() {

        Admin admin = makeAdmin();
        Product product = makeProduct(admin);

        adminRepository.save(admin);
        productRepository.save(product);

        Room room = Room.builder()
                .roomNum("1024")
                .roomType(RoomType.TWIN_ROOM)
                .price(100000)
                .headCount(4)
                .product(product)
                .build();

        roomRepository.save(room);

        Room findProductRoom = roomRepository.findByProduct_Id(product.getId()).get().get(0);

        Assertions.assertThat(findProductRoom.getRoomNum()).isEqualTo(room.getRoomNum());
        Assertions.assertThat(findProductRoom.getPrice()).isEqualTo(room.getPrice());
        Assertions.assertThat(findProductRoom.getRoomType()).isEqualTo(room.getRoomType());
    }
}