package com.batton.memberservice.controller;


import com.batton.memberservice.common.BaseResponse;
import com.batton.memberservice.dto.SignupMemberReqDTO;
import com.batton.memberservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입 API
     *
     * @param reqDTO 회원가입 시 필요한 정보들의 Object
     * @return
     */
    @PostMapping("/signup")
    @Operation(summary = "회원가입 요청")
    @Parameter(name = "reqDTO",  description = "회원가입 시 필요한 정보 Object", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "401", description = "...")
    })
    public BaseResponse<String> signupMember(@RequestBody SignupMemberReqDTO reqDTO) {
        String signupMemberRsp = authService.signupMember(reqDTO);

        return new BaseResponse<>(signupMemberRsp);
    }
}
