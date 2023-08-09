package com.batton.memberservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostEmailCheckReqDTO {
    private String email;
    private String authCode;
}
