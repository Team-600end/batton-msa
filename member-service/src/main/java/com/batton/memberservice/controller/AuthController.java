package com.batton.memberservice.controller;

import com.batton.memberservice.common.BaseResponse;
import com.batton.memberservice.dto.PostMemberReqDTO;
import com.batton.memberservice.security.TokenProvider;
import com.batton.memberservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입 API
     * @param postMemberReqDTO 회원 정보 DTO
     * @return String
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입 요청")
    public BaseResponse<String> signupMember(@RequestBody PostMemberReqDTO postMemberReqDTO) {
        String signupMemberRes = authService.signupMember(postMemberReqDTO);
        log.info("signupMember 요청: " + signupMemberRes);
        return new BaseResponse<>(signupMemberRes);
    }

//    /**
//     * 카카오 로그인 및 가입 여부 확인(미가입 시 회원가입 자동 진행 후 로그인도 자동 진행됨)
//     * @param token 접근 토큰
//     * @return BaseResponse<TokenProvider>
//     */
//    @ResponseBody
//    @PostMapping("/kakao/{access-token}")
//    @Operation(summary = "카카오 소셜 로그인")
//    @ApiResponses({
//            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.")
//    })
//    private BaseResponse<TokenProvider> kakaoLogin(@PathVariable("access-token") String token) {
//        TokenProvider tokenProvider = authService.kakaoLogin(token);
//
//        return new BaseResponse<>(tokenProvider);
//    }
}
