package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.release.PostReleasesReqDTO;
import com.batton.projectservice.service.ReleasesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    @PatchMapping("/{releaseId}")
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
}
