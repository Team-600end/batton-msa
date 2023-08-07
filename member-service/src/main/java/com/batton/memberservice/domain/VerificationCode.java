package com.batton.memberservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "verification_code", timeToLive = 60 * 3)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VerificationCode {
    private int code;
}
