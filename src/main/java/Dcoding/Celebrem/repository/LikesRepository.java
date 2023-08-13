package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    /**
     * 내가 좋아요를 누른 인플루언서 목록 가져오기
     */
    @Query("select l from Likes l where l.profile =:profileId")
    public List<Profile> findProfilesByMemberId(Long profileId);

}
