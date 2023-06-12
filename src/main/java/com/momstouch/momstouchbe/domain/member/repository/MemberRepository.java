package com.momstouch.momstouchbe.domain.member.repository;
import com.momstouch.momstouchbe.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>  {

    Optional<Member> findByEmail (String email);

    @Query("select m from Member m where m.account.loginId = :sub")
    Optional<Member> findBySub(@Param("sub") String sub);
}
