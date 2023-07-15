package com.batton.memberservice.repository;

import com.batton.memberservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    /**
     * 메일로 해당 멤버의 존재 여부를 반환
     * @param email
     * @return
     */
    boolean existsByEmail(String email);
}
