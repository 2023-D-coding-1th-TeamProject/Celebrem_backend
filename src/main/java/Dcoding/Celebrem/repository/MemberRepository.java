package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Boolean existsMemberByEmail(String email);

    List<Member> findByAuthorityAndNicknameContaining(Authority authority, String nickname);
    Boolean existsMemberByNickname(String nickname);

    @Query("SELECT m FROM Member m JOIN FETCH m.profile p " +
            "WHERE m.email = :email")
    Optional<Member> findByEmailFetchProfile(@Param("email") String email);
}
