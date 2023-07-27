package com.batton.projectservice.dto.issue;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetIssueBoardResDTO {
    private List<GetIssueResDTO> todoList = new ArrayList<>();
    private List<GetIssueResDTO> progressList = new ArrayList<>();
    private List<GetIssueResDTO> reviewList = new ArrayList<>();
    private List<GetIssueResDTO> doneList = new ArrayList<>();

    @Builder
    public GetIssueBoardResDTO (List<GetIssueResDTO> todoList, List<GetIssueResDTO> progressList, List<GetIssueResDTO> reviewList, List<GetIssueResDTO> doneList) {
        this.todoList = todoList;
        this.progressList = progressList;
        this.reviewList = reviewList;
        this.doneList = doneList;
    }
}
