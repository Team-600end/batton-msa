//package com.batton.projectservice.dto;
//
//import com.batton.projectservice.domain.Releases;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//public class PostReleaseReqDTO {
//    private Long projectId;
//    private int versionMajor;
//    private int versionMinor;
//    private int versionPatch;
//    private String releaseContent;
//
//    @Builder
//    public PostReleaseReqDTO(Long projectId, int versionMajor, int versionMinor, int versionPatch, String releaseContent) {
//        this.projectId = projectId;
//        this.versionMajor = versionMajor;
//        this.versionMinor = versionMinor;
//        this.versionPatch = versionPatch;
//        this.releaseContent = releaseContent;
//    }
//
//    public static Releases toEntity(PostReleaseReqDTO postReleaseReqDTO, Project project) {
//        return Releases.builder()
//                .project(project)
//                .versionMajor(postReleaseReqDTO.versionMajor)
//                .versionMinor(postReleaseReqDTO.versionMinor)
//                .versionPatch(postReleaseReqDTO.versionPatch)
//                .releaseContent(postReleaseReqDTO.releaseContent)
//                .build();
//    }
//}
