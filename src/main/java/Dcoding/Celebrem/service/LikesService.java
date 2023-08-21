package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.repository.LikesRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private static final Logger logger = LoggerFactory.getLogger(LikesService.class);

    /*
    <찜 기능 요구사항>
    - 로그인 한 사용자만 찜할 수 있어야 한다.
    -> 로그인 하지 않았다면 로그인 화면으로 이동
    - 찜 버튼 다시 클릭 -> 찜 취소
     */

    /**
     * 인플루언서 찜하기
     * Member가 좋아요 클릭 -> Likes 테이블에 memberId와 받은 ProfileId로 저장 & Profile의 LikeCount 증가
     */
    @Transactional
    public Long addLikes(Long memberId, Long profileId) {

        // 엔티티 조회 (주체, 상대)
        Member member = memberRepository.findById(memberId).get();
        Profile profile = profileRepository.findById(profileId).get();

        // 좋아요 생성, +1
        Likes likes = Likes.createLikes(profile, member);
        Long likesCount = likes.increaseLikesCount();

        likesRepository.save(likes);

        return likesCount;
    }


    /**
     * 찜 취소하기
     * Likes Entity -> CANCEL()
     * Profile Entity -> likesCount--
     */
    @Transactional
    public Long cancelLikes(Long likesId) {
        Optional<Likes> likes = likesRepository.findById(likesId);

        if(!likes.isPresent()) {
            logger.error("Likes with ID " + likesId + " not found", likesId);
            throw new EntityNotFoundException("Likes with ID " + likesId + " not found");
        }

        likesRepository.delete(likes.get());
        return likes.get().cancelLikes();
    }

    /**
     * 찜 목록 반환하기
     */
    public List<Likes> findAll(Long memberId){
        List<Likes> likesList = likesRepository.findAllByMember_Id(memberId);

        if (likesList.isEmpty()) {
            throw new EntityNotFoundException("아이디: " + memberId + ", 해당하는 찜 목록이 없습니다.");
        }
        return likesList;
    }
}
