//package com.batton.projectservice.service;
//
//import com.batton.projectservice.common.BaseException;
//import com.batton.projectservice.domain.Releases;
//import com.batton.projectservice.dto.PostReleaseReqDTO;
//import com.batton.projectservice.repository.IssueRepository;
//import com.batton.projectservice.repository.ReleasesRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//
//@RequiredArgsConstructor
//@Service
//public class ReleasesService {
//    private final ReleasesRepository releasesRepository;
//
//    /**
//     * 릴리즈 생성 API
//     * */
//    @Transactional
//    public Long addReleases(PostReleaseReqDTO postReleaseReqDTO) {
//        Releases releases = postReleaseReqDTO.toEntity(postReleaseReqDTO);
//        Long newReleases = releasesRepository.save(releases).getId();
//
//        return newReleases;
//    }
//}
