package com.study.reservation.room.etc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoomType {

    SINGLE_ROOM("싱글룸"),
    DOUBLE_ROOM("더블룸"),
    TWIN_ROOM("트윈룸"),
    SUITE_ROOM("스위트룸"),
    NO_BED_ROOM("온돌방"),
    DUPLEX_ROOM("복층방"),
    ONE_ROOM("원룸"),
    TWO_ROOM("투룸");

    private final String korean;

}
