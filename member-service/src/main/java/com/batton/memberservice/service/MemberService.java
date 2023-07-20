package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.batton.memberservice.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
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
}
