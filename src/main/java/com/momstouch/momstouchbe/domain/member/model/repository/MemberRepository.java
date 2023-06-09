package com.momstouch.momstouchbe.domain.member.model.repository;

import com.momstouch.momstouchbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
