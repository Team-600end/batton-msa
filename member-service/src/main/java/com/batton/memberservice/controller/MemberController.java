package com.batton.memberservice.controller;

import com.batton.memberservice.common.BaseResponse;
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
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private GetMemberResDTO getMember(@PathVariable("memberId") Long memberId) {
        return memberService.getMember(memberId);
    }
}
