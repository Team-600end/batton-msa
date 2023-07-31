package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.comment.PostCommentReqDTO;
import com.batton.projectservice.dto.issue.*;
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
     * @param memberId 이슈 생성하는 유저 아이디
     * @param postIssueReqDTO 생성 요청 바디에 포함될 PostIssueReqDTO
     * @return IssueId
     */
    @PostMapping
    @Operation(summary = "이슈 생성 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    public BaseResponse<Long> postIssue(@RequestHeader Long memberId, @RequestBody PostIssueReqDTO postIssueReqDTO) {
        Long postIssueRes = issueService.postIssue(memberId, postIssueReqDTO);

        return new BaseResponse<>(postIssueRes);
    }

    /**
     * 이슈 보드 상태 및 순서 변경 API
     * @param memberId 변경하는 작업을 하는 유저 아이디
     * @param issueId 상태를 변경할 이슈 아이디
     * @param patchIssueBoardReqDTO 변경 요청 바디에 포함될 PatchIssueBoardReqDTO
     * @return String
     * */
    @PatchMapping("/board/status/{issueId}")
    @Operation(summary = "이슈 보드 상태 및 순서 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchIssueBoard(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PatchIssueBoardReqDTO patchIssueBoardReqDTO) {
        String patchIssueBoardRes = issueService.patchIssueBoard(memberId, issueId, patchIssueBoardReqDTO);

        return new BaseResponse<>(patchIssueBoardRes);
    }

    /**
     * 이슈 보드 목록 조회
     * @param memberId 조회하는 유저 아이디
     * @param projectId 이슈 조회할 프로젝트 아이디
     * @return GetIssueBoardResDTO
     * */
    @GetMapping("/board/list/{projectId}")
    @Operation(summary = "이슈 보드 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetIssueBoardResDTO> getIssueBoard(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        GetIssueBoardResDTO getIssueBoardResDTO = issueService.getIssueBoard(memberId, projectId);

        return new BaseResponse<>(getIssueBoardResDTO);
    }

    /**
     * 개인 이슈 목록 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param projectId 프로젝트 아이디
     * @return List<GetMyIssueResDTO>
     * */
    @GetMapping("/list/{projectId}")
    @Operation(summary = "개인 이슈 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetMyIssueResDTO>> getMyIssue(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        List<GetMyIssueResDTO> getMyIssueResDTOList = issueService.getMyIssue(memberId, projectId);

        return new BaseResponse<>(getMyIssueResDTOList);
    }

    /**
     * 이슈 상세 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param issueId 조회할 이슈 아이디
     * @param projectId 프로젝트 아이디
     * @return GetIssueInfoResDTO
     * */
    @GetMapping("/{issueId}/{projectId}")
    @Operation(summary = "이슈 상세 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetIssueInfoResDTO> getIssueInfo(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @PathVariable("projectId") Long projectId) {
        GetIssueInfoResDTO getIssueInfoResDTO = issueService.getIssueInfo(memberId, issueId, projectId);

        return new BaseResponse<>(getIssueInfoResDTO);
    }

    /**
     * 대시보드 이슈 리스트 조회 API
     * @param memberId 조회하는 유저 아이디
     * @param projectId 조회할 프로젝트의 아이디
     * @return List<GetIssueResDTO>
     * */
    @GetMapping("/projects/list/{projectId}")
    @Operation(summary = "대시보드 이슈 리스트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetIssueResDTO>> getIssueList(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        List<GetIssueResDTO> getIssueResDTOList = issueService.getIssueList(memberId, projectId);

        return new BaseResponse<>(getIssueResDTOList);
    }

    /**
     * 이슈 코멘트 생성 API
     * @param memberId 코멘트 생성하는 유저 아이디
     * @param issueId 생성할 이슈 아이디
     * @param postCommentReqDTO 생성 요청 바디에 포함될 PostCommentReqDTO
     * @return String
     * */
    @PostMapping("/{issueId}/reports/comments")
    @Operation(summary = "이슈 코멘트 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> postComment(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PostCommentReqDTO postCommentReqDTO) {
        String postCommentRes = issueService.postComment(issueId, memberId, postCommentReqDTO);

        return new BaseResponse<>(postCommentRes);
    }

    /**
     * 이슈 수정 API
     * @param memberId 이슈 수정하는 유저 아이디
     * @param issueId 수정할 이슈 아이디
     * @param patchIssueReqDTO 수정 요청 바디에 포함될 PatchIssueReqDTO
     * @return String
     */
    @PatchMapping("/{issueId}")
    @Operation(summary = "이슈 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchIssue(@RequestHeader Long memberId, @PathVariable("issueId") Long issueId, @RequestBody PatchIssueReqDTO patchIssueReqDTO) {
        String patchIssueRes = issueService.patchIssue(memberId, issueId, patchIssueReqDTO);

        return new BaseResponse<>(patchIssueRes);
    }

    /**
     * 이슈 삭제 API
     * @param memberId 이슈 삭제하는 유저 아이디
     * @param projectId 프로젝트 아이디
     * @param issueId 삭제할 이슈 아이디
     * @return String
     */
    @DeleteMapping("/{projectId}/{issueId}")
    @Operation(summary = "이슈 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteIssue(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId, @PathVariable("issueId") Long issueId) {
        String deleteIssueRes = issueService.deleteIssue(memberId, projectId, issueId);

        return new BaseResponse<>(deleteIssueRes);
    }
}
