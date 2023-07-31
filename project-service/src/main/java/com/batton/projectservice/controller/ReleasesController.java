package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.release.PostReleasesReqDTO;
import com.batton.projectservice.service.ReleasesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/realeases")
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
}
