package com.batton.noticeservice.controller;

import com.batton.noticeservice.common.BaseResponse;
import com.batton.noticeservice.dto.GetNoticeResDTO;
import com.batton.noticeservice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;

    /**
     * 사용자 전체 알림 목록 조회 API
     * @param memberId 알림 목록을 조회할 유저 아이디
     * @return List<GetNoticeResDTO>
     * */
    @GetMapping("/all/{option}")
    @Operation(summary = "사용자 전체 알림 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getAllNoticeList(@RequestHeader Long memberId, @PathVariable("option") int option) {
        List<GetNoticeResDTO> getNoticeResDTOList = noticeService.getAllNoticeList(memberId, option);

        return new BaseResponse<>(getNoticeResDTOList);
    }

    /**
     * 사용자 이슈 알림 목록 조회 API
     * @param memberId 알림 목록을 조회할 유저 아이디
     * @return List<GetNoticeResDTO>
     * */
    @GetMapping("/issues/{option}")
    @Operation(summary = "사용자 이슈 알림 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getIssueNoticeList(@RequestHeader Long memberId, @PathVariable("option") int option) {
        List<GetNoticeResDTO> getNoticeResDTOList = noticeService.getIssueNoticeList(memberId, option);

        return new BaseResponse<>(getNoticeResDTOList);
    }

    /**
     * 사용자 프로젝트 알림 목록 조회 API
     * @param memberId 알림 목록을 조회할 유저 아이디
     * @return List<GetNoticeResDTO>
     * */
    @GetMapping("/projects/{option}")
    @Operation(summary = "사용자 프로젝트 알림 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "1300", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetNoticeResDTO>> getProjectNoticeList(@RequestHeader Long memberId, @PathVariable("option") int option) {
        List<GetNoticeResDTO> getNoticeResDTOList = noticeService.getProjectNoticeList(memberId, option);

        return new BaseResponse<>(getNoticeResDTOList);
    }
}
