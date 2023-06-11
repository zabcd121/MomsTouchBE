package com.momstouch.momstouchbe.domain.member.model.repository;
import com.momstouch.momstouchbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>  {

    Optional<Member> findByEmail (String email);
}
