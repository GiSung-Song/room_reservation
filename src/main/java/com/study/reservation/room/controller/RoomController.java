package com.study.reservation.room.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.room.dto.RoomDto;
import com.study.reservation.room.dto.RoomRegisterDto;
import com.study.reservation.room.dto.RoomUpdateDto;
import com.study.reservation.room.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Tag(name = "Room", description = "객실 API Document")
public class RoomController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final RoomService roomService;

    @PostMapping("/room")
    public ResponseEntity<ApiResponse<String>> registerRoom(RoomRegisterDto roomRegisterDto) {
        String companyNumber = getAuthCompanyNumber();

        roomService.registerRoom(roomRegisterDto, companyNumber);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK,"객실 등록을 완료했습니다."));
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<ApiResponse<RoomDto>> infoRoom(@PathVariable("id") Long roomId) {
        RoomDto room = roomService.infoRoom(roomId);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "객실 정보 조회를 완료했습니다.", room));
    }

    @PatchMapping("/room/{id}")
    public ResponseEntity<ApiResponse<String>> updateRoom(@PathVariable("id") Long roomId, RoomUpdateDto roomUpdateDto) {
        String companyNumber = getAuthCompanyNumber();
        roomService.updateRoom(roomUpdateDto, roomId, companyNumber);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "객실 정보 수정을 완료했습니다."));
    }

    private String getAuthCompanyNumber() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String companyNumber = principal.getUsername();

        return companyNumber;
    }
}
