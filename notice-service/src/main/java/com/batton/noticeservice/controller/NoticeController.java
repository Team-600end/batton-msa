package com.batton.noticeservice.controller;

import com.batton.noticeservice.common.BaseResponse;
import com.batton.noticeservice.dto.GetNoticeResDTO;
import com.batton.noticeservice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;

    /**
     * 사용자 개인 알림 목록 조회 API
     *
     * @param receiverId 알림 목록을 조회할 유저 아이디
     * @return List of GetNoticeListResDTO
     * */
    @GetMapping("/{receiverId}")
    @Operation(summary = "사용자 개인 알림 목록 조회")
    @Parameter(name = "receiverId",  description = "알림 목록을 조회할 유저 아이디", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getNoticeList(@PathVariable("receiverId") Long receiverId) {
        List<GetNoticeResDTO> getNoticeListResDTO = noticeService.getNoticeList(receiverId);

        return new BaseResponse<>(getNoticeListResDTO);
    }
}
