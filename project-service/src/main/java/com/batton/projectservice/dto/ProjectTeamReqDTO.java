package com.batton.projectservice.dto;

import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@NoArgsConstructor
@Getter
public class ProjectTeamReqDTO {

        private Long memberId;

        private String nickname;

        @Enumerated(EnumType.STRING)
        private GradeType gradeType;

        @Enumerated(EnumType.STRING)
        private Status status;

        @Builder
        public ProjectTeamReqDTO(Long memberId, String nickname, GradeType gradeType, Status status) {
            this.memberId = memberId;
            this.nickname = nickname;
            this.gradeType = gradeType;
            this.status = status;
        }
}
