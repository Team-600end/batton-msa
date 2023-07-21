package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.service.BelongService;
import com.batton.projectservice.service.ProjectService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class BelongController {
    private final BelongService belongService;

    /**
     * 프로젝트 멤버 권한 변경 API
     * @param projectId 프로젝트 아이디 값 belongId 소속 아이디 값
     * @return message
     */
    @PatchMapping("/{projectId}/{belongId}")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "703", description = "소속을 찾을 수 없습니다.")
    })
    private BaseResponse<String> patchGrade (@RequestHeader Long memberId, @PathVariable("projectId") Long projectId, @PathVariable("belongId") Long belongId, @RequestBody GradeType grade) {
        String modifyGradeRes = belongService.modifyBelong(memberId, projectId, belongId, grade);

        return new BaseResponse<>(modifyGradeRes);
    }
}
