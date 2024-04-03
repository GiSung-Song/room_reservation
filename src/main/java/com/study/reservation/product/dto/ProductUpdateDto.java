package com.study.reservation.product.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductUpdateDto {

    private String productName;
    private String location;
    private String phoneNumber;
    private String description;
    private String email;
    private Boolean operate;

}
