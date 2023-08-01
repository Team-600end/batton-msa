package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.comment.PostCommentReqDTO;
import com.batton.projectservice.dto.report.PatchIssueReportReqDTO;
import com.batton.projectservice.dto.report.PostIssueReportReqDTO;
import com.batton.projectservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    /**
     * 이슈 레포트 생성 API
     * @param memberId 생성 작업을 하는 멤버의 아이디
     * @param postIssueReportReqDTO 이슈 레포트 생성 요청 요청 바디에 포함될 PostIssueReportReqDTO
     * @return reportId
     * */
    @PostMapping
    @Operation(summary = "이슈 레포트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<Long> postReport(@RequestHeader Long memberId, @RequestBody PostIssueReportReqDTO postIssueReportReqDTO) {
        Long postReportRes = reportService.postReport(memberId, postIssueReportReqDTO);

        return new BaseResponse<>(postReportRes);
    }

    /**
     * 이슈 레포트 수정 API
     * @param memberId 수정 작업을 하는 멤버의 아이디
     * @param reportId 수정할 이슈 레포트 아이디
     * @param patchIssueReportReqDTO 이슈 레포트 수정 요청 요청 바디에 포함될 PatchIssueReportReqDTO
     * @return String
     * */
    @PatchMapping("/{reportId}")
    @Operation(summary = "이슈 레포트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchReport(@RequestHeader Long memberId, @PathVariable("reportId") Long reportId, @RequestBody PatchIssueReportReqDTO patchIssueReportReqDTO) {
        String patchReportRes = reportService.patchReport(memberId, reportId, patchIssueReportReqDTO);

        return new BaseResponse<>(patchReportRes);
    }

    /**
     * 이슈 레포트 삭제 API
     * @param memberId 삭제 작업을 하는 멤버의 아이디
     * @param reportId 삭제할 이슈 레포트 아이디
     * @return String
     */
    @DeleteMapping("/{reportId}")
    @Operation(summary = "이슈 레포트 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteReport(@RequestHeader Long memberId, @PathVariable("reportId") Long reportId) {
        String deleteReportRes = reportService.deleteReport(memberId, reportId);

        return new BaseResponse<>(deleteReportRes);
    }

    /**
     * 이슈 코멘트 생성 API
     * @param memberId 코멘트 생성하는 유저 아이디
     * @param reportId 코멘트를 작성할 레포트 아이디
     * @param postCommentReqDTO 생성 요청 바디에 포함될 PostCommentReqDTO
     * @return String
     * */
    @PostMapping("/comments/{reportId}")
    @Operation(summary = "이슈 코멘트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> postComment(@RequestHeader Long memberId, @PathVariable("reportId") Long reportId, @RequestBody PostCommentReqDTO postCommentReqDTO) {
        String postCommentRes = reportService.postComment(reportId, memberId, postCommentReqDTO);

        return new BaseResponse<>(postCommentRes);
    }
}
