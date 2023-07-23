import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.GetMemberInfoResDTO;
import com.batton.memberservice.dto.PatchMemberReqDTO;
import com.batton.memberservice.dto.client.GetMemberListResDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import com.batton.memberservice.repository.MemberRepository;
import com.batton.memberservice.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.JsonPathAssertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("유저 정보 조회 테스트")
    void getMemberTest() {
        // given
        Member member = new Member(1L, "cjsdkfn", "sdfndsf","ssdfdsf", Authority.ROLE_USER, "ssdfsdfsdf", Status.ENABLED);
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        GetMemberResDTO getMemberResDTO = memberService.getMember(1L);

        // then
        assertThat(getMemberResDTO.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("유저 목록 조회 테스트")
    void getMemberListTest() {
        // given
        List<Member> memberList = new ArrayList<>();
        Member member1 = new Member(1L, "cjsdkfn", "sdfndsf","ssdfdsf", Authority.ROLE_USER, "ssdfsdfsdf", Status.ENABLED);
        Member member2 = new Member(2L, "dftrjsdkfn", "tyefh","rttrfduy", Authority.ROLE_USER, "ijknkfsdf", Status.ENABLED);
        memberList.add(member1);
        memberList.add(member2);

        given(memberRepository.findAll()).willReturn(memberList);

        // when
        List<GetMemberListResDTO> getMemberListResDTO = memberService.getMemberList();

        // then
        assertThat(getMemberListResDTO.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 정보 수정 테스트")
    void patchMemberTest() {
        // given
        Member member = new Member(2L, "cjsdkfn", "sdfndsf","ssdfdsf", Authority.ROLE_USER, "ssdfsdfsdf", Status.ENABLED);
        PatchMemberReqDTO patchMemberReqDTO = new PatchMemberReqDTO("tryyrsdfsdf", "sdfsdwtew");
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        memberService.patchMember(2L, patchMemberReqDTO);

        // then
        assertThat(member.getNickname()).isEqualTo("tryyrsdfsdf");
    }


    @Test
    @DisplayName("추가할 프로젝트 멤버 정보 조회")
    public void checkMember() {
        // given
        Member member = new Member(1L, "테스트이메일", "테스트닉네임", "테스트비번", Authority.ROLE_USER, "테스트이미지", Status.ENABLED);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        GetMemberInfoResDTO getMemberInfoResDTO = memberService.checkMember("테스트이메일");

        // then
        assertThat(getMemberInfoResDTO.getEmail()).isEqualTo(member.getEmail());
    }
}
