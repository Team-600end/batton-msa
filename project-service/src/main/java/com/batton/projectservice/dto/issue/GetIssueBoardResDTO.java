package com.batton.projectservice.dto.issue;

import com.batton.projectservice.domain.Issue;
import com.batton.projectservice.dto.client.GetMemberResDTO;
import com.batton.projectservice.enums.GradeType;
import com.batton.projectservice.enums.IssueTag;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GetIssueBoardResDTO {
    private List<GetIssueListResDTO> todoList = new ArrayList<>();
    private List<GetIssueListResDTO> progressList = new ArrayList<>();
    private List<GetIssueListResDTO> reviewList = new ArrayList<>();
    private List<GetIssueListResDTO> doneList = new ArrayList<>();

    @Builder
    public GetIssueBoardResDTO (List<GetIssueListResDTO> todoList, List<GetIssueListResDTO> progressList, List<GetIssueListResDTO> reviewList, List<GetIssueListResDTO> doneList) {
        this.todoList = todoList;
        this.progressList = progressList;
        this.reviewList = reviewList;
        this.doneList = doneList;
    }
}
