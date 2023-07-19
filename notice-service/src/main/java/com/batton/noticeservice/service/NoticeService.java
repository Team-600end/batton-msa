package com.batton.noticeservice.service;

import com.batton.noticeservice.client.MemberServiceFeignClient;
import com.batton.noticeservice.common.BaseException;
import com.batton.noticeservice.domain.Notice;
import com.batton.noticeservice.dto.GetNoticeResDTO;
import com.batton.noticeservice.dto.client.GetMemberResDTO;
import com.batton.noticeservice.enums.NoticeType;
import com.batton.noticeservice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


import static com.batton.noticeservice.common.BaseResponseStatus.NOTICE_INVALID_USER_ID;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberServiceFeignClient memberServiceFeignClient;

    public List<GetNoticeResDTO> getNoticeList(Long receiverId) {
        List<Notice> noticeList = noticeRepository.findAllByReceiverId(receiverId);
        List<GetNoticeResDTO> responseDTO = new ArrayList<>();

        for (Notice notice : noticeList) {
            GetMemberResDTO getMemberResDTO = memberServiceFeignClient.getMember(notice.getSenderId()).getResult();

            if (getMemberResDTO == null) {
                throw new BaseException(NOTICE_INVALID_USER_ID);
            }
            GetNoticeResDTO getNoticeResDTO = GetNoticeResDTO.builder()
                    .contentId(notice.getContentId())
                    .noticeType(notice.getNoticeType())
                    .noticeContent(notice.getNoticeContent())
                    .noticeDate("이승희바보")
                    .senderProfileImage(getMemberResDTO.getProfileImage())
                    .build();
            responseDTO.add(getNoticeResDTO);
        }
        return responseDTO;
    }
}