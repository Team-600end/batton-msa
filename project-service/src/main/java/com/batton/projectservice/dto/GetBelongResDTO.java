package com.batton.projectservice.dto;

import com.batton.projectservice.enums.GradeType;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class GetBelongResDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private GradeType grade;
    private Long memberId;
    private String nickname;

}
