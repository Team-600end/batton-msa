package com.batton.noticeservice.controller;

import com.batton.noticeservice.common.BaseResponse;
import com.batton.noticeservice.dto.GetNoticeResDTO;
import com.batton.noticeservice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
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
     * 사용자 전체 알림 목록 조회 API
     * @param receiverId 알림 목록을 조회할 유저 아이디
     * @return List<GetNoticeResDTO>
     * */
    @GetMapping("/all/{receiverId}/{option}")
    @Operation(summary = "사용자 전체 알림 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getAllNoticeList(@PathVariable("receiverId") Long receiverId, @PathVariable("option") int option) {
        List<GetNoticeResDTO> getNoticeResDTOList = noticeService.getAllNoticeList(receiverId, option);

        return new BaseResponse<>(getNoticeResDTOList);
    }

    /**
     * 사용자 이슈 알림 목록 조회 API
     * @param receiverId 알림 목록을 조회할 유저 아이디
     * @return List<GetNoticeResDTO>
     * */
    @GetMapping("/issue/{receiverId}/{option}")
    @Operation(summary = "사용자 이슈 알림 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getIssueNoticeList(@PathVariable("receiverId") Long receiverId, @PathVariable("option") int option) {
        List<GetNoticeResDTO> getNoticeResDTOList = noticeService.getIssueNoticeList(receiverId, option);

        return new BaseResponse<>(getNoticeResDTOList);
    }

    /**
     * 사용자 프로젝트 알림 목록 조회 API
     * @param receiverId 알림 목록을 조회할 유저 아이디
     * @return List<GetNoticeResDTO>
     * */
    @GetMapping("/project/{receiverId}/{option}")
    @Operation(summary = "사용자 프로젝트 알림 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getProjectNoticeList(@PathVariable("receiverId") Long receiverId, @PathVariable("option") int option) {
        List<GetNoticeResDTO> getNoticeResDTOList = noticeService.getProjectNoticeList(receiverId, option);

        return new BaseResponse<>(getNoticeResDTOList);
    }
}
