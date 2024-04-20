package com.study.reservation.room.controller;

import com.study.reservation.config.response.ApiResponse;
import com.study.reservation.config.util.CommonUtils;
import com.study.reservation.room.dto.RoomDto;
import com.study.reservation.room.dto.RoomRegisterDto;
import com.study.reservation.room.dto.RoomUpdateDto;
import com.study.reservation.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Room", description = "객실 API Document")
public class RoomController {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;
    private final RoomService roomService;
    private final CommonUtils commonUtils;

    @Operation(summary = "객실 등록", description = "객실을 등록한다.")
    @PostMapping("/admin/room")
    public ResponseEntity<ApiResponse<String>> registerRoom(RoomRegisterDto roomRegisterDto) {
        String companyNumber = commonUtils.getAuthUsername();

        roomService.registerRoom(roomRegisterDto, companyNumber);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK,"객실 등록을 완료했습니다."));
    }

    @Operation(summary = "객실 정보 수정", description = "객실 정보를 수정한다.")
    @PatchMapping("/admin/room/{id}")
    public ResponseEntity<ApiResponse<String>> updateRoom(@PathVariable("id") Long roomId, RoomUpdateDto roomUpdateDto) {
        String companyNumber = commonUtils.getAuthUsername();
        roomService.updateRoom(roomUpdateDto, roomId, companyNumber);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "객실 정보 수정을 완료했습니다."));
    }

    @Operation(summary = "숙소 내 객실 상세 조회", description = "해당 상품의 객실을 상세조회한다.")
    @GetMapping("/product/{productId}/rooms/{roomId}")
    public ResponseEntity<ApiResponse<RoomDto>> getProductRoom(@PathVariable Long productId, @PathVariable Long roomId) {
        RoomDto room = roomService.infoRoom(productId, roomId);

        return ResponseEntity.ok(ApiResponse.res(HTTP_STATUS_OK, "객실 정보 조회를 완료했습니다.", room));
    }

}
