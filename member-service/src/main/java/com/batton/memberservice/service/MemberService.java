package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.dto.client.GetMemberListResDTO;
import com.batton.memberservice.dto.PatchMemberPasswordReqDTO;
import com.batton.memberservice.dto.PatchMemberReqDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 정보 조회 API(Feign Client)
     * */
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

    /**
     * 유저 정보 확인 조회 API
     * */
    public GetMemberInfoResDTO checkMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        GetMemberInfoResDTO getMemberInfoResDTO;

        if (member.isPresent()) {
            getMemberInfoResDTO = GetMemberInfoResDTO.toDTO(member.get());
        } else {
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return getMemberInfoResDTO;
    }

    /**
     * 유저 정보 수정 API
     * */
    public String patchMember(Long memberId, PatchMemberReqDTO patchMemberReqDTO) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            member.get().update(patchMemberReqDTO.getNickname(), patchMemberReqDTO.getProfileImage());
        } else {
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return "회원 정보 수정되었습니다.";
    }

    /**
     * 유저 비밀번호 수정 API
     * */
    public String patchMemberPassword(Long memberId, PatchMemberPasswordReqDTO patchMemberPasswordReqDTO) {
        Optional<Member> member = memberRepository.findById(memberId);

        if (member.isPresent()) {
            if (passwordEncoder.matches(patchMemberPasswordReqDTO.getCurrentPassword(), member.get().getPassword())) {
                throw new BaseException(MEMBER_PASSWORD_DISCORD);
            }
            if (!patchMemberPasswordReqDTO.getChangedPassword().equals(patchMemberPasswordReqDTO.getCheckChangedPassword())) {
                throw new BaseException(MEMBER_PASSWORD_CONFLICT);
            }
            member.get().updatePassword(passwordEncoder.encode(patchMemberPasswordReqDTO.getChangedPassword()));
        } else {
            throw new BaseException(MEMBER_INVALID_USER_ID);
        }

        return "회원 비밀번호 수정되었습니다.";
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
