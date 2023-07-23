package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.issue.GetIssueBoardResDTO;
import com.batton.projectservice.dto.issue.PatchIssueBoardReqDTO;
import com.batton.projectservice.dto.issue.PostIssueReqDTO;
import com.batton.projectservice.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/issues")
public class IssueController {
    private final IssueService issueService;

    /**
     * 이슈 생성 API
     *
     * @return id of Issue
     */
    @PostMapping
    @Operation(summary = "이슈 생성 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 유저를 찾을 수 없습니다.")
    })
    public BaseResponse<Long> postIssue(@RequestBody PostIssueReqDTO postIssueReqDTO) {
        Long signupMemberRsp = issueService.addIssue(postIssueReqDTO);

        return new BaseResponse<>(signupMemberRsp);
    }

    /**
     * 이슈 보드 상태 및 순서 변경 API
     *
     * @param issueId 상태를 변경할 이슈 아이디
     * @return String
     * */
    @PatchMapping("/{issueId}/status")
    @Operation(summary = "이슈 보드 상태 및 순서 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 유저를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "704", description = "이슈를 찾을 수 없습니다.")
    })
    private BaseResponse<String> patchIssueBoard(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PatchIssueBoardReqDTO patchIssueBoardReqDTO) {
        String patchIssueBoard = issueService.modifyIssueBoard(memberId, issueId, patchIssueBoardReqDTO);

        return new BaseResponse<>(patchIssueBoard);
    }

    /**
     * 이슈 보드 목록 조회
     *
     * @return GetIssueBoardResDTO
     * */
    @GetMapping("/board/{projectId}")
    @Operation(summary = "이슈 보드 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트를 찾을 수 없습니다."),
    })
    private BaseResponse<GetIssueBoardResDTO> getIssueBoard(@PathVariable("projectId") Long projectId) {
        GetIssueBoardResDTO getIssueBoardResDTO = issueService.getIssueBoard(projectId);

        return new BaseResponse<>(getIssueBoardResDTO);
    }

}
