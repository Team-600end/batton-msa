package com.batton.projectservice.common;

import com.batton.projectservice.dto.issue.GetMyIssueResDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class IssueComparator implements Comparator<GetMyIssueResDTO> {
    @Override
    public int compare(GetMyIssueResDTO data1, GetMyIssueResDTO data2) {
        try {
            String pattern = "yyyy.mm.dd"; // 날짜 형식과 매칭되는 패턴
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            Date first = sdf.parse(data1.getUpdatedDate());
            Date second = sdf.parse(data2.getUpdatedDate());

            if (first.compareTo(second)>0){
                return -1;
            } else if (first.compareTo(second)<0) {
                return 1;
            } else {
                return 0;
            }
        }  catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
