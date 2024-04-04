package com.study.reservation.room.service;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.etc.ProductType;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.room.dto.RoomRegisterDto;
import com.study.reservation.room.dto.RoomUpdateDto;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.etc.RoomType;
import com.study.reservation.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @InjectMocks
    RoomService roomService;

    @Mock
    AdminRepository adminRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    RoomRepository roomRepository;

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

    RoomRegisterDto makeDto() {
        RoomRegisterDto dto = new RoomRegisterDto();

        dto.setRoomNum("1024");
        dto.setDescription("테스트 설명입니다.");
        dto.setRoomType(RoomType.TWIN_ROOM);
        dto.setPrice(200000);
        dto.setHeadCount(8);

        return dto;
    }

    Room makeRoom() {
        return Room.builder()
                .roomNum("1024")
                .description("테스트 설명입니다.")
                .roomType(RoomType.TWIN_ROOM)
                .price(200000)
                .headCount(8)
                .build();
    }

    @Nested
    class register {

        @DisplayName("객실 등록 성공 테스트")
        @Test
        void 등록_성공_테스트() {
            Admin admin = makeAdmin();
            Product product = makeProduct(admin);
            RoomRegisterDto dto = makeDto();
            Room room = makeRoom();

            given(adminRepository.findByCompanyNumber(any())).willReturn(Optional.ofNullable(admin));
            given(productRepository.findByAdmin_Id(any())).willReturn(Optional.ofNullable(product));

            given(roomRepository.save(any())).willReturn(room);
            given(roomRepository.findById(any())).willReturn(Optional.ofNullable(room));

            Long savedRoomId = roomService.registerRoom(dto, admin.getCompanyNumber());

            Room findRoom = roomRepository.findById(savedRoomId).get();

            assertEquals(findRoom.getRoomNum(), dto.getRoomNum());
            assertEquals(findRoom.getPrice(), dto.getPrice());
        }

        @DisplayName("객실 등록 실패 테스트")
        @Test
        void 등록_실패_테스트() {
            doThrow(new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER)).when(adminRepository).findByCompanyNumber(any());

            String companyNumber = "1234123412";
            RoomRegisterDto dto = makeDto();

            assertThrows(CustomException.class, () -> roomService.registerRoom(dto, companyNumber));
        }
    }

    @Nested
    class update {

        @DisplayName("수정 성공 테스트")
        @Test
        void 수정_성공_테스트() {
            Admin admin = makeAdmin();
            Product product = makeProduct(admin);
            Room room = makeRoom();
            product.addRoom(room);
            Long fakeId = 0L;

            ReflectionTestUtils.setField(room, "id", fakeId);

            given(adminRepository.findByCompanyNumber(any())).willReturn(Optional.ofNullable(admin));
            given(productRepository.findByAdmin_Id(any())).willReturn(Optional.ofNullable(product));
            given(roomRepository.findById(fakeId)).willReturn(Optional.ofNullable(room));

            RoomUpdateDto updateDto = new RoomUpdateDto();
            updateDto.setDescription("수정 테스트 설명");
            updateDto.setRoomNum("2024");
            updateDto.setPrice(100000);
            updateDto.setIsOperate(false);

            roomService.updateRoom(updateDto, fakeId, admin.getCompanyNumber());

            Room updateRoom = roomRepository.findById(fakeId).get();

            assertEquals(updateRoom.getRoomNum(), updateDto.getRoomNum());
            assertEquals(updateRoom.getIsOperate(), updateDto.getIsOperate());
            assertEquals(updateRoom.getDescription(), updateDto.getDescription());
            assertEquals(updateRoom.getHeadCount(), room.getHeadCount());
        }
    }

}