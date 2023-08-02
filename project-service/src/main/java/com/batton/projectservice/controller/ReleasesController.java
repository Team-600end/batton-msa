package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.release.GetProjectReleasesListResDTO;
import com.batton.projectservice.dto.release.PatchReleasesReqDTO;
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
    public BaseResponse<Long> postReleases(@RequestHeader Long memberId, @RequestBody PostReleasesReqDTO postReleasesReqDTO) {
        Long postReleasesRes = releasesService.postReleases(memberId, postReleasesReqDTO);

        return new BaseResponse<>(postReleasesRes);
    }

    /**
     * 릴리즈 노트 발행 API
     * @param memberId 릴리즈 노트 발행하는 유저 아이디
     * @param releaseId 발행할 릴리즈 노트 아이디
     */
    @PatchMapping("/{releaseId}/publish")
    @Operation(summary = "릴리즈노트 발행 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    public BaseResponse<String> patchPublish(@RequestHeader Long memberId, @PathVariable Long releaseId) {
        String publishReleasesRes = releasesService.patchPublish(memberId, releaseId);

        return new BaseResponse<>(publishReleasesRes);
    }

    /**
     * 릴리즈 노트 수정 API
     * @param memberId 릴리즈 노트 수정하는 유저 아이디
     * @param releaseId 발행할 릴리즈 노트 아이디
     * @param patchReleasesReqDTO 릴리즈 노트 수정 요청 바디에 포함될 PatchReleasesReqDTO
     * @return String
     */
    @PatchMapping("/{releaseId}")
    @Operation(summary = "릴리즈노트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "708", description = "릴리즈 노트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchReleases(@RequestHeader Long memberId, @PathVariable Long releaseId, @RequestBody PatchReleasesReqDTO patchReleasesReqDTO) {
        String patchReleasesRes = releasesService.patchReleases(memberId, releaseId, patchReleasesReqDTO);

        return new BaseResponse<>(patchReleasesRes);
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
    public BaseResponse<String> deleteReleases(@RequestHeader Long memberId, @PathVariable Long releaseId) {
        String deleteReleasesRes = releasesService.deleteReleases(memberId, releaseId);

        return new BaseResponse<>(deleteReleasesRes);
    }

    /**
     * 프로젝트 릴리 노트 조회 API
     * @param projectId 조회할 프로젝트 아이디
     * @return List<GetProjectReleaseListResDTO>
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "프로젝트 릴리즈 노트 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "703", description = "소속 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "709", description = "프로젝트 아이디 값을 확인해주세요.")
    })
    public BaseResponse<List<GetProjectReleasesListResDTO>> getProjectReleaseList(@PathVariable Long projectId) {
        List<GetProjectReleasesListResDTO> getProjectReleaseListRes = releasesService.getProjectReleasesList(projectId);

        return new BaseResponse<>(getProjectReleaseListRes);
    }
}
