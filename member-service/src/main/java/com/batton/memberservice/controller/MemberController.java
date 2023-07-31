package com.batton.memberservice.controller;

import com.batton.memberservice.common.BaseResponse;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.dto.PatchMemberPasswordReqDTO;
import com.batton.memberservice.dto.PatchMemberReqDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    /**
     * 유저 정보 조회 API(Feign Client)
     * @param memberId 정보를 조회할 유저 아이디
     * @return GetMemberResDTO
     * */
    @GetMapping("/{memberId}")
    @Operation(summary = "유저 정보 조회 feign client")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private GetMemberResDTO getMember(@PathVariable("memberId") Long memberId) {
        GetMemberResDTO getMemberResDTO = memberService.getMember(memberId);
        log.info("getMember 요청: " + getMemberResDTO.toString());

        return getMemberResDTO;
    }

    /**
     * 유저 정보 조회 API
     * @param memberId 정보를 조회할 유저 아이디
     * @return GetMemberResDTO
     * */
    @GetMapping
    @Operation(summary = "유저 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetMemberInfoResDTO> getMemberInfo(@RequestHeader Long memberId) {
        GetMemberInfoResDTO getMemberInfoResDTO = memberService.getMemberInfo(memberId);
        log.info("getMember 요청: " + getMemberInfoResDTO.toString());

        return new BaseResponse<>(getMemberInfoResDTO);
    }

    /**
     * 추가할 멤버 정보 조회 API
     * @param email 정보를 조회할 유저 이메일
     * @return GetMemberInfoResDTO
     */
    @GetMapping("/list")
    @Operation(summary = "추가할 멤버 정보 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<GetMemberInfoResDTO> getCheckMember(@RequestParam(name = "email") String email) {
        GetMemberInfoResDTO getMemberInfoResDTO = memberService.getCheckMember(email);
        log.info("getCheckMember 요청: " + getMemberInfoResDTO.toString());

        return new BaseResponse<>(getMemberInfoResDTO);
    }

    /**
     * 유저 정보 수정 API
     * @param memberId 정보를 수정할 유저 아이디
     * @param patchMemberReqDTO 정보 수정 요청 바디에 포함될 DTO
     * @return String
     * */
    @PatchMapping
    @Operation(summary = "유저 정보 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요.")
    })
    private BaseResponse<String> patchMember(@RequestHeader Long memberId, @RequestBody PatchMemberReqDTO patchMemberReqDTO) {
        String patchMemberRes = memberService.patchMember(memberId, patchMemberReqDTO);
        log.info("patchMember 요청: " + patchMemberRes);

        return new BaseResponse<>(patchMemberRes);
    }

    /**
     * 유저 비밀번호 수정 API
     * @param memberId 비밀번호를 수정할 유저 아이디
     * @param patchMemberPasswordReqDTO 비밀번호 수정 요청 바디에 포함될 DTO
     * @return String
     * */
    @PatchMapping("/password")
    @Operation(summary = "유저 비밀번호 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "600", description = "유저 아이디 값을 확인해주세요."),
            @ApiResponse(responseCode = "602", description = "두 비밀번호를 같게 입력해주세요."),
            @ApiResponse(responseCode = "603", description = "비밀번호가 일치하지 않습니다.")

    })
    private BaseResponse<String> patchMemberPassword(@RequestHeader Long memberId, @RequestBody PatchMemberPasswordReqDTO patchMemberPasswordReqDTO) {
        String patchMemberPasswordRes = memberService.patchMemberPassword(memberId, patchMemberPasswordReqDTO);
        log.info("patchMemberPassword 요청: " + patchMemberPasswordRes);

        return new BaseResponse<>(patchMemberPasswordRes);
    }
}
