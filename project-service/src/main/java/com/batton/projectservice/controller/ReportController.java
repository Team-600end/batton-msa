package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
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
     * @param postIssueReportReqDTO 이슈 레포트 생성 요청 요청 바디에 포함될 PostIssueReportReqDTO
     * @return reportId
     * */
    @PostMapping
    @Operation(summary = "이슈 레포트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<Long> postReport(@RequestBody PostIssueReportReqDTO postIssueReportReqDTO) {
        Long postReportRes = reportService.postReport(postIssueReportReqDTO);

        return new BaseResponse<>(postReportRes);
    }

    /**
     * 이슈 레포트 수정 API
     * @param reportId 수정할 이슈 레포트 아이디
     * @param patchIssueReportReqDTO 이슈 레포트 수정 요청 요청 바디에 포함될 PatchIssueReportReqDTO
     * @return String
     * */
    @PatchMapping("/{reportId}")
    @Operation(summary = "이슈 레포트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchReport(@PathVariable("reportId") Long reportId, @RequestBody PatchIssueReportReqDTO patchIssueReportReqDTO) {
        String patchReportRes = reportService.patchReport(reportId, patchIssueReportReqDTO);

        return new BaseResponse<>(patchReportRes);
    }

    /**
     * 이슈 레포트 삭제 API
     * @param reportId 삭제할 이슈 레포트 아이디
     * @return String
     */
    @DeleteMapping("/{reportId}")
    @Operation(summary = "이슈 레포트 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "705", description = "이슈 레포트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteReport(@PathVariable("reportId") Long reportId) {
        String deleteReportRes = reportService.deleteReport(reportId);

        return new BaseResponse<>(deleteReportRes);
    }

}
