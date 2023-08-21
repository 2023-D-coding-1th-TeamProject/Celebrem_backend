package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    /**
     * 내가 좋아요를 누른 인플루언서 목록 가져오기
     */
    @Query("select l from Likes l where l.member =:member")
    List<Likes> findByFromId(Member member);

    List<Likes> findAllByMember_Id(Long memberId);

    @Query("select l FROM Likes l JOIN FETCH l.profile p " + "WHERE l.member = :member")
    List<Likes> findAllByIdFetchProfile(@Param("member") Member member);
}
