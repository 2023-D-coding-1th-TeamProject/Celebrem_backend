package Dcoding.Celebrem.repository;

import Dcoding.Celebrem.domain.member.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByMember_Id(Long memberId);

    @Query("SELECT p FROM Profile p JOIN FETCH p.member pm LEFT JOIN p.profileTags pt " +
            "WHERE (:tagName IS NULL OR pt.tag.name = :tagName)")
    Page<Profile> findByTagNameFetch(@Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT p FROM Profile p JOIN FETCH p.member pm LEFT JOIN p.profileTags pt " +
            "WHERE :profile_id = p.id")
    Optional<Profile> findByIdFetch(@Param("profile_id") Long id);

    @Query("SELECT p FROM Profile p JOIN FETCH p.member pm LEFT JOIN p.profileTags pt " +
            "WHERE (:tagName IS NULL OR pt.tag.name = :tagName) " +
            "ORDER BY p.likeCount DESC")
    Page<Profile> findByTagNameFetchOrderByLikeCount(@Param("tagName") String tagName, Pageable pageable);

    @Query("SELECT p FROM Profile p JOIN FETCH p.member pm LEFT JOIN FETCH p.profileTags pt WHERE pm.nickname LIKE %:nickname%")
    List<Profile> findAllByNickname(@Param("nickname") String nickname);
}
