package com.batton.noticeservice.enums;

/**
 * INVITE - 프로젝트 초대 알림
 * EXCLUDE - 프로젝트 제외(방출) 알림
 * REVIEW - 검토 이슈 발생 알림 (리더)
 * APPROVE - 이슈 승인 알림
 * REJECT - 이슈 반려 알림
 * BATTON - 이슈 바톤 터치 알림
 * NEW - 새 릴리즈 노트 출시 알림
 */
public enum NoticeType {
    INVITE, EXCLUDE, REVIEW, APPROVE, REJECT, BATTON, NEW
}
