import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.common.ValidationRegex;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.PostMemberReqDTO;
import com.batton.memberservice.dto.client.GetMemberResDTO;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import com.batton.memberservice.mq.QueueService;
import com.batton.memberservice.repository.MemberRepository;
import com.batton.memberservice.service.AuthService;
import com.batton.memberservice.service.MemberService;
import com.batton.memberservice.service.ObjectStorageService;
import com.batton.memberservice.service.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.batton.memberservice.common.ValidationRegex.isRegexEmail;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTests {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;
    @InjectMocks
    private AuthService authService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private QueueService queueService;
    @Mock
    private RedisUtil redisUtil;
    @Mock
    private ObjectStorageService objectStorageService;

    @Test
    @DisplayName("유저 회원가입 성공")
    public void testSignupMemberSuccess() {
        // given
        PostMemberReqDTO postMemberReqDTO = new PostMemberReqDTO("testd@example.com", "code", "nika", "password", "password");

        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded-password");
        when(redisUtil.existData(any())).thenReturn(true);
        when(redisUtil.getData(any())).thenReturn("code");

        // when
        String result = authService.signupMember(postMemberReqDTO);

        // then
        assertEquals("회원가입 성공하였습니다.", result);
        verify(memberRepository, times(1)).save(any());
        verify(queueService, times(1)).createQueueForMember(any());
    }

    @Test
    @DisplayName("유저 회원가입 시 이미 존재하는 이메일 예외 처리")
    public void testSignupMemberExistingEmail() {
        // given
        PostMemberReqDTO postMemberReqDTO = new PostMemberReqDTO("test@example.com", "code","nika", "password", "password");

        when(memberRepository.existsByEmail(postMemberReqDTO.getEmail())).thenReturn(true);

        // when, then
        assertThrows(BaseException.class, () -> authService.signupMember(postMemberReqDTO));
    }

    @Test
    @DisplayName("유저 회원가입 시 이메일 미인증 예외 처리")
    public void testSignupMemberEmailAdmin() {
        // given
        PostMemberReqDTO postMemberReqDTO = new PostMemberReqDTO("tes@example.com", "code","nika", "password", "password");

        when(redisUtil.existData(postMemberReqDTO.getEmail())).thenReturn(true);
        when(redisUtil.getData(postMemberReqDTO.getEmail()).equals(postMemberReqDTO.getAuthCode())).thenReturn(false);

        // when, then
        assertThrows(BaseException.class, () -> authService.signupMember(postMemberReqDTO));
    }


    @Test
    @DisplayName("유저 정보 조회 성공")
    public void testGetMemberValidUser() {
        // given
        Long memberId = 1L;
        Member validMember = new Member(memberId, "cjsdkfn", "sdfndsf", "ssdfdsf", Authority.ROLE_USER, "ssdfsdfsdf", Status.ENABLED);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(validMember));

        // when
        GetMemberResDTO result = memberService.getMember(memberId);

        // then
        assertNotNull(result);
        assertEquals(validMember.getNickname(), result.getNickname());
        // 여기에 추가적인 필드 검증을 수행할 수 있습니다.
    }

    @Test
    @DisplayName("유저 잘못된 아이디 예외처리")
    public void testGetMemberInvalidUser() {
        // Arrange
        Long memberId = 2L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BaseException.class, () -> memberService.getMember(memberId));
    }

    //
    @Test
    @DisplayName("유저가 탈퇴한 상태 예외처리")
    public void testGetMemberDisabledUser() {
        // Arrange
        Long memberId = 3L;
        Member disabledMember = new Member(memberId, "cjsdkfn", "sdfndsf", "ssdfdsf", Authority.ROLE_USER, "ssdfsdfsdf", Status.DISABLED);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(disabledMember));

        // Act & Assert
        assertThrows(BaseException.class, () -> memberService.getMember(memberId));
    }

    @Test
    @DisplayName("유저 정보 수정")
    public void testPatchMemberSuccess() {
        // Arrange
        Long memberId = 1L;
        String nickname = "newNickname";
        MultipartFile profileImage = mock(MultipartFile.class);
        String imageUrl = "https://example.com/image.jpg";

        Member existingMember = new Member(memberId, "cjsdkfn", nickname, "ssdfdsf", Authority.ROLE_USER, imageUrl, Status.ENABLED);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(existingMember));
        when(objectStorageService.uploadFile(any(MultipartFile.class))).thenReturn(imageUrl);

        // Act
        String result = memberService.patchMember(memberId, profileImage, nickname);

        // Assert
        assertEquals(imageUrl, result);
        assertEquals(nickname, existingMember.getNickname());
        verify(memberRepository, times(1)).findById(memberId);
        verify(objectStorageService, times(1)).uploadFile(profileImage);
    }

    @Test
    @DisplayName("유저 아이디 확인")
    public void testPatchMemberInvalidUser() {
        // Arrange
        Long memberId = 2L;
        MultipartFile profileImage = mock(MultipartFile.class);
        String nickname = "newNickname";

        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BaseException.class, () -> memberService.patchMember(memberId, profileImage, nickname));
    }

    @Test
    @DisplayName("잘못된 유저")
    public void testPatchMemberDisabledUser() {
        // Arrange
        Long memberId = 3L;
        MultipartFile profileImage = mock(MultipartFile.class);
        String nickname = "newNickname";
        String imageUrl = "https://example.com/image.jpg";


        Member disabledMember = new Member(memberId, "cjsdkfn", nickname, "ssdfdsf", Authority.ROLE_USER, imageUrl, Status.DISABLED);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(disabledMember));

        // Act & Assert
        assertThrows(BaseException.class, () -> memberService.patchMember(memberId, profileImage, nickname));
    }
}

