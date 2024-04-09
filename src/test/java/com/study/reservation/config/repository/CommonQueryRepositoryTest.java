package com.study.reservation.config.repository;

import com.study.reservation.config.etc.SearchCondition;
import com.study.reservation.room.entity.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@DataJpaTest
class CommonQueryRepositoryTest {

    @MockBean
    private CommonQueryRepository queryRepository;

    @Test
    @DisplayName("조건에 맞는 객실 조회")
    void 객실_조회_테스트() {
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setHighPrice(1000000);
        searchCondition.setLowPrice(5000);
        searchCondition.setCount(4);
        searchCondition.setEndDate("20240409");
        searchCondition.setStartDate("20240401");
        searchCondition.setLocation("");
        searchCondition.setProductName("");

        List<Room> roomList = new ArrayList<>();

        when(queryRepository.searchRoom(searchCondition)).thenReturn(roomList);

        List<Room> findRooms = queryRepository.searchRoom(searchCondition);

        Assertions.assertNotNull(findRooms);

        Assertions.assertEquals(0, findRooms.size());
    }

}