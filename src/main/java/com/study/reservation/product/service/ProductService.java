package com.study.reservation.product.service;

import com.study.reservation.admin.entity.Admin;
import com.study.reservation.config.etc.SearchCondition;
import com.study.reservation.config.exception.CustomException;
import com.study.reservation.config.exception.ErrorCode;
import com.study.reservation.config.jwt.repository.AdminRepository;
import com.study.reservation.config.repository.CommonQueryRepository;
import com.study.reservation.product.dto.*;
import com.study.reservation.product.entity.Product;
import com.study.reservation.product.repository.ProductRepository;
import com.study.reservation.room.dto.RoomDto;
import com.study.reservation.room.entity.Room;
import com.study.reservation.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final RoomRepository roomRepository;
    private final CommonQueryRepository commonQueryRepository;

    @Transactional(readOnly = true)
    public List<ProductListDto> getProductList(SearchCondition searchCondition) {
        List<ProductListDto> dtoList = new ArrayList<>();

        List<Room> roomList = commonQueryRepository.searchRoom(searchCondition);

        for (Room room : roomList) {
            ProductListDto dto = new ProductListDto();

            dto.setProductId(room.getProduct().getId());
            dto.setProductName(room.getProduct().getProductName());
            dto.setLocation(room.getProduct().getLocation());
            dto.setLowPrice(room.getPrice());

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Transactional(readOnly = true)
    public ProductDto infoProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        List<Room> productRoom = roomRepository.findByProduct_Id(product.getId());

        ProductDto productDto = new ProductDto();

        productDto.setProductName(product.getProductName());
        productDto.setProductType(product.getProductType());
        productDto.setEmail(product.getEmail());
        productDto.setLocation(product.getLocation());
        productDto.setPhoneNumber(product.getPhoneNumber());
        productDto.setDescription(product.getDescription());

        List<RoomDto> roomDtoList = new ArrayList<>();

        for (Room room : productRoom) {
            RoomDto roomDto = new RoomDto();

            roomDto.setProductName(product.getProductName());
            roomDto.setRoomNum(room.getRoomNum());
            roomDto.setRoomType(room.getRoomType());
            roomDto.setPrice(room.getPrice());
            roomDto.setHeadCount(room.getHeadCount());
            roomDto.setDescription(room.getDescription());
            roomDto.setIsOperate(room.getIsOperate());

            roomDtoList.add(roomDto);
        }

        productDto.setRoomList(roomDtoList);

        return productDto;
    }

    @Transactional
    public Long registerProduct(ProductRegisterDto productRegisterDto, String companyNumber) {
        Admin admin = adminRepository.findByCompanyNumber(companyNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER));

        String description = "";

        if (StringUtils.hasText(productRegisterDto.getDescription())) {
            description = productRegisterDto.getDescription();
        }

        Product product = Product.builder()
                .admin(admin)
                .productName(productRegisterDto.getProductName())
                .location(productRegisterDto.getLocation())
                .productType(productRegisterDto.getProductType())
                .phoneNumber(productRegisterDto.getPhoneNumber())
                .email(productRegisterDto.getEmail())
                .description(description)
                .build();

        return productRepository.save(product).getId();
    }

    @Transactional
    public void updateProduct(ProductUpdateDto productUpdateDto, String companyNumber, Long productId) {
        Admin admin = adminRepository.findByCompanyNumber(companyNumber)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COMPANY_NUMBER));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));

        //해당 상품의 관리자의 ID와 현재 접속된 관리자의 ID가 다를 경우
        if (product.getAdmin().getId() != admin.getId()) {
            throw new CustomException(ErrorCode.NOT_VALID_ADMIN);
        }

        //Patch Validation X -> 직접 if throw
        if (StringUtils.hasText(productUpdateDto.getProductName())) {
            if (productUpdateDto.getProductName().length() > 30) {
                throw new CustomException(ErrorCode.NOT_VALID_PRODUCT_NAME);
            }

            product.updateProductName(productUpdateDto.getProductName());
        }

        if (StringUtils.hasText(productUpdateDto.getLocation())) {
            product.updateLocation(productUpdateDto.getLocation());
        }

        if (StringUtils.hasText(productUpdateDto.getPhoneNumber())) {
            if (productUpdateDto.getPhoneNumber().length() > 11 || productUpdateDto.getPhoneNumber().length() < 9) {
                throw new CustomException(ErrorCode.NOT_VALID_PHONE_NUMBER);
            }

            String phoneNumber = productUpdateDto.getPhoneNumber();

            for (int i = 0; i < phoneNumber.length(); i++) {
                char tmp = phoneNumber.charAt(i);

                if (Character.isDigit(tmp) == false) {
                    throw new CustomException(ErrorCode.NOT_VALID_NUMBER);
                }
            }

            product.updatePhoneNumber(phoneNumber);
        }

        if (StringUtils.hasText(productUpdateDto.getDescription())) {
            product.updateDescription(productUpdateDto.getDescription());
        } else {
            product.updateDescription("");
        }

        if (StringUtils.hasText(productUpdateDto.getEmail())) {
            String emailPattern = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

            if (!Pattern.matches(emailPattern, productUpdateDto.getEmail())) {
                throw new CustomException(ErrorCode.NOT_VALID_EMAIL);
            }

            product.updateEmail(productUpdateDto.getEmail());
        }

        if (productUpdateDto.getOperate() != null) {

            if (productUpdateDto.getOperate()) {
                product.updateIsOperate(true);
            } else {
                product.updateIsOperate(false);
            }

        }

    }

}
