package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.issue.PatchIssueBoardReqDTO;
import com.batton.projectservice.dto.issue.PatchIssueReportReqDTO;
import com.batton.projectservice.dto.issue.PostIssueReportReqDTO;
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
     * @param postIssueReportReqDTO 이슈 레포트 생성 요청 DTO
     * @return id of Report
     * */
    @PostMapping
    @Operation(summary = "이슈 레포트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<Long> postReport(@RequestBody PostIssueReportReqDTO postIssueReportReqDTO) {
        Long reportId = reportService.addReport(postIssueReportReqDTO);

        return new BaseResponse<>(reportId);
    }

    /**
     * 이슈 레포트 수정 API
     * @param patchIssueReportReqDTO 이슈 레포트 수정 요청 DTO
     * @return String
     * */
    @PatchMapping("/{reportId}")
    @Operation(summary = "이슈 레포트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> postReport(@PathVariable("reportId") Long reportId, @RequestBody PatchIssueReportReqDTO patchIssueReportReqDTO) {
        String modifyReportRes = reportService.modifyReport(reportId, patchIssueReportReqDTO);

        return new BaseResponse<>(modifyReportRes);
    }

    /**
     * 이슈 레포트 삭제 API
     * @param reportId 이슈 레포트 아이디 값
     * @return message
     */
    @DeleteMapping("/{reportId}")
    @Operation(summary = "이슈 레포트 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteReport(@PathVariable("reportId") Long reportId) {
        String deleteReportRes = reportService.removeReport(reportId);

        return new BaseResponse<>(deleteReportRes);
    }

}
