package com.study.reservation.config.etc;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCondition {

    private String startDate; //시작날짜
    private String endDate; //종료날짜
    private String location; //위치
    private Integer count; //인원 수
    private String productName; //숙소명
    private Integer lowPrice; //최저가격
    private Integer highPrice; //최고가격
}
