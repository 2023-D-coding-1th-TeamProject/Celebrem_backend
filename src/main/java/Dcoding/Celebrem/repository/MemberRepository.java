package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Boolean existsMemberByEmail(String email);

    List<Member> findMemberByNickname(String nickname);
}
