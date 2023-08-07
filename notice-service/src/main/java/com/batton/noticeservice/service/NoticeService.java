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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static com.batton.noticeservice.common.BaseResponseStatus.NOTICE_INVALID_USER_ID;
import static com.batton.noticeservice.enums.NoticeType.*;

@RequiredArgsConstructor
@Service
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    /**
     * 사용자 전체 알림 목록 조회
     * */
    public List<GetNoticeResDTO> getAllNoticeList(Long receiverId, int option) {
        List<Notice> noticeList = new ArrayList<>();

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
        List<NoticeType> noticeTypeList = Arrays.asList(REVIEW, APPROVE, REJECT, BATTON, COMMENT);
        List<Notice> noticeList;

        if (option == 0) {
            noticeList = noticeRepository.findTop4ByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, noticeTypeList);
        } else {
            noticeList = noticeRepository.findAllByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, noticeTypeList);
        }

        return getNoticeList(noticeList);
    }

    /**
     * 사용자 프로젝트 알림 목록 조회
     * */
    public List<GetNoticeResDTO> getProjectNoticeList(Long receiverId, int option) {
        List<NoticeType> typeList = Arrays.asList(INVITE, EXCLUDE, NEW, GRADE);
        List<Notice> noticeList;

        if (option == 0) {
            noticeList = noticeRepository.findTop4ByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, typeList);
        } else {
            noticeList = noticeRepository.findAllByReceiverIdAndNoticeTypeInOrderByCreatedAtDesc(receiverId, typeList);
        }

        return getNoticeList(noticeList);
    }

    private List<GetNoticeResDTO> getNoticeList(List<Notice> noticeList) {
        List<GetNoticeResDTO> getNoticeResDTOList = new ArrayList<>();

        for (Notice notice : noticeList) {
            GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(notice.getSenderId());
            String date = Chrono.timesAgo(notice.getCreatedAt());

            if (getMemberResDTO == null) {
                throw new BaseException(NOTICE_INVALID_USER_ID);
            }
            getNoticeResDTOList.add(GetNoticeResDTO.toDTO(notice, date, getMemberResDTO.getProfileImage()));
        }

        return getNoticeResDTOList;
    }
}