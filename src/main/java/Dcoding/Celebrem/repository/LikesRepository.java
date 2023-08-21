package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("select l from Likes l where l.member =:member")
    List<Likes> findByFromId(Member member);

    List<Likes> findAllByMember_Id(Long memberId);

    @Query("select l FROM Likes l JOIN FETCH l.profile p JOIN FETCH p.member pm " +
            "WHERE l.member = :member")
    List<Likes> findAllByMemberFetchProfile(@Param("member") Member member);

    @Query("SELECT l FROM Likes l JOIN FETCH l.profile JOIN FETCH l.member " +
            "WHERE l.profile = :profile AND l.member = :member")
    Optional<Likes> findByMemberAndProfile(@Param("profile") Profile profile, @Param("member")Member member);

}
