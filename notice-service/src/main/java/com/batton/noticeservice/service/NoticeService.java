package com.batton.noticeservice.service;

import com.batton.noticeservice.client.MemberServiceFeignClient;
import com.batton.noticeservice.common.BaseException;
import com.batton.noticeservice.common.Chrono;
import com.batton.noticeservice.domain.Notice;
import com.batton.noticeservice.dto.GetNoticeResDTO;
import com.batton.noticeservice.dto.client.GetMemberResDTO;
import com.batton.noticeservice.enums.NoticeType;
import com.batton.noticeservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.batton.noticeservice.common.BaseResponseStatus.NOTICE_INVALID_USER_ID;
import static com.batton.noticeservice.enums.NoticeType.*;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    /**
     * 사용자 전체 알림 목록 조회
     * */
    public List<GetNoticeResDTO> getAllNoticeList(Long receiverId, int option) {
        List<Notice> noticeList;

        if (option == 0) {
            noticeList = noticeRepository.findTop4ByReceiverIdOrderByCreatedAtDesc(receiverId);
        } else {
            noticeList = noticeRepository.findAllByReceiverIdOrderByCreatedAtDesc(receiverId);
        }

        return getNoticeList(noticeList);
    }

    /**
     * 사용자 이슈 알림 목록 조회
     * */
    public List<GetNoticeResDTO> getIssueNoticeList(Long receiverId, int option) {
        List<NoticeType> types = Arrays.asList(REVIEW, APPROVE, REJECT, BATTON, COMMENT);
        List<Notice> noticeList;

        if (option == 0) {
            noticeList = noticeRepository.findTop4ByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, types);
        } else {
            noticeList = noticeRepository.findAllByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, types);
        }

        return getNoticeList(noticeList);
    }

    /**
     * 사용자 프로젝트 알림 목록 조회
     * */
    public List<GetNoticeResDTO> getProjectNoticeList(Long receiverId, int option) {
        List<NoticeType> types = Arrays.asList(INVITE, EXCLUDE, NEW);
        List<Notice> noticeList;

        if (option == 0) {
            noticeList = noticeRepository.findTop4ByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, types);
        } else {
            noticeList = noticeRepository.findAllByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, types);
        }

        return getNoticeList(noticeList);
    }

    private List<GetNoticeResDTO> getNoticeList(List<Notice> noticeList) {
        List<GetNoticeResDTO> responseDTO = new ArrayList<>();

        for (Notice notice : noticeList) {
            GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(notice.getSenderId()).getResult();
            String date = Chrono.timesAgo(notice.getCreatedAt());

            if (getMemberResDTO == null) {
                throw new BaseException(NOTICE_INVALID_USER_ID);
            }
            GetNoticeResDTO getNoticeResDTO = GetNoticeResDTO.builder()
                    .contentId(notice.getContentId())
                    .noticeType(notice.getNoticeType())
                    .noticeContent(notice.getNoticeContent())
                    .noticeDate(date)
                    .senderProfileImage(getMemberResDTO.getProfileImage())
                    .build();

            responseDTO.add(getNoticeResDTO);
        }

        return responseDTO;
    }
}