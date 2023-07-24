package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.PostIssueReportReqDTO;
import com.batton.projectservice.dto.project.PostProjectReqDTO;
import com.batton.projectservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
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
    private BaseResponse<Long> postReport(@RequestBody PostIssueReportReqDTO postIssueReportReqDTO) {
        Long reportId = reportService.addReport(postIssueReportReqDTO);

        return new BaseResponse<>(reportId);
    }
}
