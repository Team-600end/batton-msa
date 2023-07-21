package com.batton.projectservice.controller;

import com.batton.projectservice.common.BaseResponse;
import com.batton.projectservice.dto.PatchProjectReqDTO;
import com.batton.projectservice.dto.PostProjectReqDTO;
import com.batton.projectservice.dto.ProjectTeamReqDTO;
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
            @ApiResponse(responseCode = "701", description = "프로젝트를 찾을 수 없습니다.")
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
            @ApiResponse(responseCode = "701", description = "프로젝트를 찾을 수 없습니다.")
    })
    private BaseResponse<String> deleteProject (@RequestHeader Long memberId, @PathVariable("projectId") Long projectId) {
        String deleteProjectRes = projectService.removeProject(memberId, projectId);

        return new BaseResponse<>(deleteProjectRes);
    }

    /**
     * 프로젝트 멤버 추가를 위한 API
     * @param projectId 프로젝트 ID 값
     * @param memberId 추가할 멤버의 ID 값
     * @param invitedMemberId 초대된 멤버의 ID 값
     * @param projectTeamReqDTOList 요청 바디에 포함될 ProjectTeamReqDTO 객체
     * @return message
     */
    @PostMapping("/{projectId}/{invitedMemberId}")
    @Operation(summary = "프로젝트 멤버 추가")
    private BaseResponse<String> addMember(
            @RequestHeader Long memberId,
            @PathVariable("projectId") Long projectId,
            @PathVariable("invitedMemberId") Long invitedMemberId,
            @RequestBody List<ProjectTeamReqDTO> projectTeamReqDTOList
    ) {
        String addMemberRes = projectService.addTeamMember(invitedMemberId, projectId, projectTeamReqDTOList);

        return new BaseResponse<>(addMemberRes);
    }

    /**
     * 프로젝트 멤버 삭제를 위한 API
     * @param belongId 삭제할 멤버의 belongID 값
     * @return message
     */
    @DeleteMapping("/{belongId}")
    @Operation(summary = "프로젝트 멤버 삭제")
    private BaseResponse<String> deleteMember(@RequestHeader Long memberId, @PathVariable("belongId") Long belongId) {
        String deleteMemberRes = projectService.deleteTeamMember(memberId, belongId);

        return new BaseResponse<>(deleteMemberRes);
    }
    // 토큰 확인?을 어떻게 할지, 왜 관리자로 다 들어가는지, 멤버 추가 삭제할 때 실행하는 멤버아이디 받아서 관리자인지 체크하는거 넣어야 하는지?
    //테스트 잘 되다가 왜 안되는지 모르겠음 500에러 발생
}
