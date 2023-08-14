package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.search.LikesSearch;
import Dcoding.Celebrem.dto.search.MainSearch;
import Dcoding.Celebrem.repository.LikesRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    /*
    <찜 기능 요구사항>
    - 로그인 한 사용자만 찜할 수 있어야 한다.
    -> 로그인 하지 않았다면 로그인 화면으로 이동
    - 찜 버튼 다시 클릭 -> 찜 취소
     */

    /**
     * 찜 추가하기
     * Member가 좋아요 클릭 -> Likes 테이블에 memberId와 받은 ProfileId로 저장 & Profile의 LikeCount 증가
     */
    @Transactional
    public Long addLikes(Long memberId, Long profileId) {

        // 엔티티 조회 (주체, 상대)
        Member member = memberRepository.findById(memberId).get();
        Profile profile = profileRepository.findById(profileId).get();

        // 좋아요 생성
        Likes likes = Likes.createLikes(profile, member);

        return likes.getId();
    }


    /**
     * 찜 취소하기
     * Likes Entity -> CANCEL()
     * Profile Entity -> likesCount--
     */
    @Transactional
    public void cancelLikes(Long likesId) {
        Optional<Likes> likes = likesRepository.findById(likesId);

        likes.cancel();
    }


    /**
     * 찜 목록 가져오기 v1 : 프로필 화면에서 이름 검색만
     * 프로필사진, 닉네임, 날짜?, 좋아요 수, 팔로워 수
     * fromId(좋아요를 누른 사람)으로 조회해 ToId(인플루언서) 목록을 가져온다.
     */
    public void findAllByName(LikesSearch likesSearch) {
        return likesRepository.findAllByName();
    }

}
