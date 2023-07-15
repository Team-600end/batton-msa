package com.batton.noticeservice.client;


import com.batton.noticeservice.common.BaseResponse;
import com.batton.noticeservice.dto.client.GetMemberResDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "member-service")
public interface MemberServiceFeignClient {
    @GetMapping(value = "/members/{memberId}", consumes = "application/json")
    BaseResponse<GetMemberResDTO> getMember(@PathVariable("memberId") Long memberId);
}
