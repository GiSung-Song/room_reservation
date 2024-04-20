package com.study.reservation.room.service;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.room.dto.RoomDto;
import com.study.reservation.room.dto.RoomRegisterDto;
import com.study.reservation.room.dto.RoomUpdateDto;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public RoomDto infoRoom(Long productId, Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        if (room.getProduct().getId() != product.getId()) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNum(room.getRoomNum());
        roomDto.setRoomType(room.getRoomType());
        roomDto.setPrice(room.getPrice());
        roomDto.setIsOperate(room.getIsOperate());
        roomDto.setDescription(room.getDescription());
        roomDto.setHeadCount(room.getHeadCount());
        roomDto.setProductName(room.getProduct().getProductName());

        return roomDto;
    }

    @Transactional
    public Long registerRoom(RoomRegisterDto dto, String companyNumber) {
        //admin 관리자 찾기
        Admin admin = adminRepository.findByCompanyNumber(companyNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER));

        //product 상품 찾기
        Product product = productRepository.findByAdmin_Id(admin.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        String description = "";

        if (StringUtils.hasText(dto.getDescription())) {
            description = dto.getDescription();
        }

        Room room = Room.builder()
                .roomNum(dto.getRoomNum())
                .price(dto.getPrice())
                .headCount(dto.getHeadCount())
                .description(description)
                .roomType(dto.getRoomType())
                .build();

        //상품(product)에 객실(room) 추가
        product.addRoom(room);

        return roomRepository.save(room).getId();
    }

    @Transactional
    public void updateRoom(RoomUpdateDto dto, Long roomId, String companyNumber) {
        //변경하려는 사람이 숙소 주인이 맞는지 체크
        Admin admin = adminRepository.findByCompanyNumber(companyNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER));

        Product product = productRepository.findByAdmin_Id(admin.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        //해당 숙소의 객실이 맞는지 체크
        if (room.getProduct().getId() != product.getId()) {
            throw new CustomException(ErrorCode.NOT_VALID_ERROR);
        }

        //validation 체크
        if (StringUtils.hasText(dto.getDescription())) {
            room.updateDescription(dto.getDescription());
        }

        if (StringUtils.hasText(dto.getRoomNum())) {
            if (dto.getRoomNum().length() > 30) {
                throw new CustomException(ErrorCode.NOT_VALID_ROOM_NUM);
            }

            room.updateRoomNum(dto.getRoomNum());
        }

        if (dto.getPrice() != null) {
            room.updatePrice(dto.getPrice());
        }

        if (dto.getIsOperate() != null) {
            room.updateIsOperate(dto.getIsOperate());
        }

        if (dto.getHeadCount() != null) {
            room.updateHeadCount(dto.getHeadCount());
        }

    }
}
