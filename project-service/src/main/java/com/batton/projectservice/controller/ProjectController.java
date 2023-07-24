package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.project.PatchProjectReqDTO;
import com.batton.projectservice.dto.project.PostProjectReqDTO;
import com.batton.projectservice.dto.GetProjectListResDTO;
import com.batton.projectservice.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
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
     * @param postProjectReqDTO 프로젝트 생성 요청 DTO
     * @return id of Project
     * */
    @PostMapping
    @Operation(summary = "프로젝트 생성", description = "프로젝트를 생성할 때 ProjectManager 권한 부여를 위해 생성자 멤버 정보도 포함해서 보내주세요.")
    private BaseResponse<Long> postProject(@RequestHeader Long memberId, @RequestBody PostProjectReqDTO postProjectReqDTO) {
        Long projectId = projectService.addProject(memberId, postProjectReqDTO);

        return new BaseResponse<>(projectId);
    }

    /**
     * 프로젝트 수정 API
     * @param projectId 프로젝트 아이디 값
     * @return message
     */
    @PatchMapping("/{projectId}")
    @Operation(summary = "프로젝트 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchProject(@RequestHeader Long memberId, @PathVariable("projectId") Long projectId, @RequestBody PatchProjectReqDTO patchProjectReqDTO) {
        String modifyProjectRes = projectService.modifyProject(memberId, projectId, patchProjectReqDTO);

        return new BaseResponse<>(modifyProjectRes);
    }

    /**
     * 프로젝트 삭제 API
     * @param projectId 프로젝트 아이디 값
     * @return message
     */
    @DeleteMapping("/{projectId}")
    @Operation(summary = "프로젝트 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "700", description = "유저에게 해당 권한이 없습니다."),
            @ApiResponse(responseCode = "701", description = "프로젝트 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> deleteProject (@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        String deleteProjectRes = projectService.removeProject(memberId, projectId);

        return new BaseResponse<>(deleteProjectRes);
    }

    /**
     * 프로젝트 네비바 리스트 조회 API
     * @return List of Project for Navbar
     */
    @GetMapping("/navbar")
    @Operation(summary = "프로젝트 네비바 리스트 조회")
    private BaseResponse<List<GetProjectListResDTO>> getProjectListForNavbar(@RequestHeader Long memberId) {
        List<GetProjectListResDTO> projectListForNavbarRes = projectService.getProjectListForNavbar(memberId);

        return new BaseResponse<>(projectListForNavbarRes);
    }
}
