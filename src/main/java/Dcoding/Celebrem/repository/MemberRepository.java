package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.member.Authority;
import Dcoding.Celebrem.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByEmail(String email);

    Boolean existsMemberByEmail(String email);

    @Query("SELECT m from Member m where m.authority =:authority and m.nickname LIKE CONCAT('%', :nickname, '%')")
    Page<Member> findAllByAuthorityAndNicknameContaining(@Param("authority") Authority authority, @Param("nickname") String nickname, Pageable pageable);

    List<Member> findByAuthorityAndNicknameContaining(Authority authority, String nickname);

    Boolean existsMemberByNickname(String nickname);

    @Query("select m FROM Member m JOIN FETCH m.profile p " + "WHERE m.email = :email")
    Optional<Member> findByEmailFetchProfile(@Param("email") String email);

    @Query("select m FROM Member m JOIN FETCH m.profile p " + "WHERE m.id = :id")
    Optional<Member> findByIdFetchProfile(@Param("id") Long memberId);

    @Query("SELECT m FROM Member m JOIN FETCH m.likes ml WHERE m.email = :email")
    Optional<Member> findByEmailFetchLikes(@Param("email") String email);

}
