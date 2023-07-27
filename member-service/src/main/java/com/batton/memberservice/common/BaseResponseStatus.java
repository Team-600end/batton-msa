package com.batton.memberservice.common;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    /**
     * Default status code
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),
    NOT_FOUND(false, 404, "요청을 찾을 수 없습니다."),
    SERVER_ERROR(false, 500, "서버 처리에 오류가 발생하였습니다."),

    /**
     * member-service - 600 ~ 699
     */
    MEMBER_INVALID_USER_ID(false, 600, "유저 아이디 값을 확인해주세요."),
    EXIST_EMAIL_ERROR(false, 601, "이미 존재하는 이메일입니다."),
    MEMBER_PASSWORD_CONFLICT(false, 602, "두 비밀번호를 같게 입력해주세요."),
    MEMBER_PASSWORD_DISCORD(false, 603, "비밀번호가 일치하지 않습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
