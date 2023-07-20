package com.batton.memberservice.controller;

import com.batton.memberservice.common.BaseResponse;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * 사용자 정보 조회 API(Feign Client)
     *
     * @param memberId 정보를 조회할 유저 아이디
     * @return GetMemberResDTO
     * */
    @GetMapping("/{memberId}")
    @Operation(summary = "사용자 정보 조회")
    @Parameter(name = "memberId",  description = "정보를 조회할 유저 아이디", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetMemberResDTO> getMember(@PathVariable("memberId") Long memberId) {
        GetMemberResDTO getMemberResDTO = memberService.getMember(memberId);

        return new BaseResponse<>(getMemberResDTO);
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


}
