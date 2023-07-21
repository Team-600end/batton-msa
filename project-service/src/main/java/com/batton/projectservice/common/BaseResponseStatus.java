package com.batton.projectservice.common;

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
     * project-service - 700 ~ 1299
     * notice-service - 1300 ~ 1399
     */

    USER_NO_AUTHORITY(false, 700, "유저에게 해당 권한이 없습니다."),
    PROJECT_NOT_FOUND(false, 701, "프로젝트를 찾을 수 없습니다."),
    USER_NOT_FOUND(false, 702, "유저를 찾을 수 없습니다."),
    BELONG_NOT_FOUND(false, 703, "소속을 찾을 수 없습니다."),
    MEMBER_INVALID_USER_ID(false, 1300, "유저 아이디 값을 확인해주세요.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
