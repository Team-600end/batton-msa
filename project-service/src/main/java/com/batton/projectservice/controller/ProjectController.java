package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.PostProjectReqDTO;
import com.batton.projectservice.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    /**
     * 프로젝트 생성 API
     *
     * @param
     * @return id of Project
     * */
    @PostMapping
    @ResponseBody
    @Operation(summary = "프로젝트 생성")
    private BaseResponse<Long> getNoticeList(@RequestHeader Long memberId, @RequestBody PostProjectReqDTO postProjectReqDTO) {
        Long projectId = projectService.addProject(memberId, postProjectReqDTO);

        return new BaseResponse<>(projectId);
    }

}
