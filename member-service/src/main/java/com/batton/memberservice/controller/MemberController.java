package com.batton.memberservice.controller;

import com.batton.memberservice.common.BaseResponse;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.dto.PatchMemberPasswordReqDTO;
import com.batton.memberservice.dto.PatchMemberReqDTO;
import com.batton.memberservice.dto.client.GetMemberListResDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * 유저 정보 조회 API(Feign Client)
     *
     * @param memberId 정보를 조회할 유저 아이디
     * @return GetMemberResDTO
     * */
    @GetMapping("/{memberId}")
    @Operation(summary = "유저 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private GetMemberResDTO getMember(@PathVariable("memberId") Long memberId) {
        return memberService.getMember(memberId);
    }

    /**
     * 추가할 프로젝트 멤버 정보 조회 API
     * @param email 정보를 조회할 유저 이메일
     * @return GetMemberResDTO
     */
    @GetMapping("/add-members")
    @Operation(summary = "추가할 프로젝트 멤버 정보 조회")
    @Parameter(name = "email",  description = "정보를 조회할 유저 이메일", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 이메일 값을 확인해주세요.")
    })
    private BaseResponse<GetMemberInfoResDTO> checkMember(String email) {
        GetMemberInfoResDTO getMemberInfoResDTO = memberService.checkMember(email);

        return new BaseResponse<>(getMemberInfoResDTO);
    }

    /**
     * 유저 정보 수정 API
     *
     * @param memberId 정보를 수정할 유저 아이디
     * @return String
     * */
    @PatchMapping("/{memberId}")
    @Operation(summary = "유저 정보 수정")
    @Parameter(name = "memberId",  description = "정보를 수정할 유저 아이디", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchMember(@PathVariable("memberId") Long memberId, @RequestBody PatchMemberReqDTO patchMemberReqDTO) {
        String patchMemberRes = memberService.patchMember(memberId, patchMemberReqDTO);

        return new BaseResponse<>(patchMemberRes);
    }

    /**
     * 유저 비밀번호 수정 API
     *
     * @param memberId 비밀번호를 수정할 유저 아이디
     * @return String
     * */
    @PatchMapping("/{memberId}/password")
    @Operation(summary = "유저 비밀번호 수정")
    @Parameter(name = "memberId",  description = "비밀번호를 수정할 유저 아이디", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "602", description = "두 비밀번호를 같게 입력해주세요."),
            @ApiResponse(responseCode = "603", description = "비밀번호가 일치하지 않습니다.")

    })
    private BaseResponse<String> patchMemberPassword(@PathVariable("memberId") Long memberId, @RequestBody PatchMemberPasswordReqDTO patchMemberPasswordReqDTO) {
        String patchMemberPasswordRes = memberService.patchMemberPassword(memberId, patchMemberPasswordReqDTO);

        return new BaseResponse<>(patchMemberPasswordRes);
    }

    /**
     * 유저 목록 조회 API(Feign Client)
     *
     * @return List<GetMemberListResDTO>
     * */
    @GetMapping("/list")
    @Operation(summary = "유저 목록 조회")
    @ApiResponses({
    })
    private BaseResponse<List<GetMemberListResDTO>> getMember() {
        List<GetMemberListResDTO> getMemberListResDTO = memberService.getMemberList();

        return new BaseResponse<>(getMemberListResDTO);
    }
}
