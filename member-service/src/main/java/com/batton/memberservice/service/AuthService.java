package com.batton.memberservice.service;

import com.batton.memberservice.common.BaseException;
import com.batton.memberservice.domain.Member;
import com.batton.memberservice.dto.PostMemberReqDTO;
import com.batton.memberservice.enums.Authority;
import com.batton.memberservice.enums.Status;
import com.batton.memberservice.mq.QueueService;
import com.batton.memberservice.repository.MemberRepository;
import com.batton.memberservice.security.TokenDTO;
import com.batton.memberservice.security.TokenProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.batton.memberservice.common.BaseResponseStatus.*;
import static com.batton.memberservice.common.ValidationRegex.isRegexEmail;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final QueueService queueService;

    /**
     * 회원가입 API
     */
    @Transactional
    public String signupMember(PostMemberReqDTO postMemberReqDTO) {
        //이메일 정규 표현 검증
        if (!isRegexEmail(postMemberReqDTO.getEmail()))
            throw new BaseException(POST_MEMBERS_INVALID_EMAIL);

        // 이메일 존재 여부 확인
        if (memberRepository.existsByEmail(postMemberReqDTO.getEmail())) {
            log.info("signupMember 예외: " + EXIST_EMAIL_ERROR.getMessage());
            throw new BaseException(EXIST_EMAIL_ERROR);
        }
        // 비밀번호 일치 확인
        if (!postMemberReqDTO.getPassword().equals(postMemberReqDTO.getCheckPassword())) {
            log.info("signupMember 예외: " + MEMBER_PASSWORD_CONFLICT.getMessage());
            throw new BaseException(MEMBER_PASSWORD_CONFLICT);
        }
        Member member = postMemberReqDTO.toEntity(postMemberReqDTO, passwordEncoder.encode(postMemberReqDTO.getPassword()), Authority.ROLE_USER, Status.ENABLED);

        memberRepository.save(member);
        queueService.createQueueForMember(member.getId()); // 유저 Queue 생성

        return "회원가입 성공하였습니다.";
    }

//    /**
//     * 카카오 로그인 및 jwt 생성
//     */
//    public TokenProvider kakaoLogin(String token) {
//        // token으로 사용자 정보 가져오기
//        PostMemberReqDTO info = getKakaoInfo(token);
//
//        // 존재하지 않는 이메일(신규 회원)이면 회원가입 자동진행 후 로그인
//        if (memberRepository.findByEmail(info.getEmail()).isEmpty()) {
//
//            Member member = PostMemberReqDTO.toEntity(info,"s",Authority.ROLE_USER, Status.ENABLED);
//            memberRepository.save(member);
//
//            String memberId = user.getUsername();
//            String accessToken = tokenProvider.createAccessToken(memberId, request.getRequestURI(), roles);
//            Date expiredTime = tokenProvider.getExpiredTime(accessToken);
//            String refreshToken = tokenProvider.createRefreshToken();
//
//            refreshTokenService.updateRefreshToken(Long.valueOf(memberId), tokenProvider.getRefreshTokenId(refreshToken));
//            TokenDTO tokenDTO = TokenDTO.builder()
//                    .accessToken(accessToken)
//                    .accessTokenExpiredDate(expiredTime)
//                    .refreshToken(refreshToken)
//                    .build();
//            // jwt 생성 후 반환
//            String jwt = jwtService.createJwt(member.getId());
//            String ref = jwtService.createRef(member.getId());
//
//            return new PostLoginRes(member.getId(), jwt, System.currentTimeMillis()+1*(1000*60*30), ref);
//        } else {
//            // 존재하는 이메일이면 로그인 진행
//            // jwt 생성 후 반환
//            Member member = memberRepository.findByEmail(info.getEmail()).get();
//
//            String jwt = jwtService.createJwt(member.getId());
//            String ref = jwtService.createRef(member.getId());
//
//            return new PostLoginResDTO(member.getId(), jwt, System.currentTimeMillis()+1*(1000*60*30), ref);
//        }
//    }

    /**
     * 카카오 서버에서 회원가입에 필요한 사용자 정보 가져오기
     */
    public PostMemberReqDTO getKakaoInfo(String token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String email = "";
        String nickName = "";

        // access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            // JsonElement element =  Jsonparser.parseString(result);

            // 이메일 가져오기
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }
            System.out.println("email : " + email);

            // 닉네임 가져오기
            nickName = element.getAsJsonObject().get("properties").getAsJsonObject().get("nickname").getAsString();
            System.out.println("email : " + nickName);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PostMemberReqDTO info = new PostMemberReqDTO(email, nickName);

        return info;
    }
}