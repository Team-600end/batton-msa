package com.batton.noticeservice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseException extends RuntimeException {
    private BaseResponseStatus status;
}
