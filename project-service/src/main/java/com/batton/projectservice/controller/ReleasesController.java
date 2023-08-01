package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.release.GetReleasesIssueResDTO;
import com.batton.projectservice.dto.release.GetReleasesResDTO;
import com.batton.projectservice.dto.release.PostReleasesReqDTO;
import com.batton.projectservice.service.ReleasesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/releases")
public class ReleasesController {
    private final ReleasesService releasesService;

    /**
     * 릴리즈 노트 생성 API
     * @param memberId 릴리즈 노트 생성하는 유저 아이디
     * @param postReleasesReqDTO 릴리즈 노트 생성 요청 바디에 포함될 PostReleasesReqDTO
     * @return releasesId
     */
    @PostMapping
    @Operation(summary = "릴리즈노트 생성 요청")
    private BaseResponse<Long> postReleases(@RequestHeader Long memberId, @RequestBody PostReleasesReqDTO postReleasesReqDTO) {
        Long postReleasesRes = releasesService.postReleases(memberId, postReleasesReqDTO);

        return new BaseResponse<>(postReleasesRes);
    }

    /**
     * 릴리즈 노트 발행 API
     * @param memberId 릴리즈 노트 발행하는 유저 아이디
     * @param releaseId 발행할 릴리즈 노트 아이디
     */
    @PatchMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 발행 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchPublish(@RequestHeader Long memberId, @PathVariable("releaseId") Long releaseId) {
        String publishReleasesRes = releasesService.patchPublish(memberId, releaseId);

        return new BaseResponse<>(publishReleasesRes);
    }

    /**
     * 릴리즈 노트 삭제 API
     * @param memberId 릴리즈 노트 삭제하는 유저 아이디
     * @param releaseId 삭제할 릴리즈 노트 아이디
     */
    @DeleteMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 삭제 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteReleases(@RequestHeader Long memberId, @PathVariable("releaseId") Long releaseId) {
        String deleteReleasesRes = releasesService.deleteReleases(memberId, releaseId);

        return new BaseResponse<>(deleteReleasesRes);
    }

    /**
     * 릴리즈 노트에 포함된 이슈 목록 조회 API
     * @param releaseId 조회할 릴리즈 노트 아이디
     */
    @GetMapping("/{releaseId}/issues/list")
    @Operation(summary = "릴리즈노트에 포함된 이슈 목록 조회 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "704", description = "이슈 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<List<GetReleasesIssueResDTO>> getReleasesIssues(@PathVariable("releaseId") Long releaseId) {
        List<GetReleasesIssueResDTO> getReleasesIssueRes = releasesService.getReleasesIssues(releaseId);

        return new BaseResponse<>(getReleasesIssueRes);
    }

    /**
     * 릴리즈 노트 상세 조회 API
     * @param releaseId 이슈 추가할 릴리즈 노트 아이디
     */
    @GetMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 상세 조회 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetReleasesResDTO> getReleases(@PathVariable("releaseId") Long releaseId) {
        GetReleasesResDTO getReleasesRes = releasesService.getReleases(releaseId);

        return new BaseResponse<>(getReleasesRes);
    }
}
