package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.client.GetMemberListResDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.batton.memberservice.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    public GetMemberResDTO getMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.isPresent()) {
            return GetMemberResDTO.builder()
                    .nickname(member.get().getNickname())
                    .profileImage(member.get().getProfileImage())
                    .build();
        } else {
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }
    }

    public List<GetMemberListResDTO> getMemberList() {
        List<Member> getMemberList = memberRepository.findAll();
        List<GetMemberListResDTO> getMemberListResDTOS = getMemberList.stream()
                .map(member -> GetMemberListResDTO.builder()
                        .nickname(member.getNickname())
                        .profileImage(member.getProfileImage())
                        .email(member.getEmail())
                        .build())
                .collect(Collectors.toList());

        return getMemberListResDTOS;
    }
}
