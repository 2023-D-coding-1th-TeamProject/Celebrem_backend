package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.common.util.SecurityUtil;
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

import static Dcoding.Celebrem.common.util.SecurityUtil.getCurrentMemberEmail;

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
    public void like(Long profileId) {

        // 엔티티 조회 (주체, 상대)
        Member currentMember = memberRepository.findMemberByEmail(getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다."));
        Profile profile = profileRepository.findById(profileId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 프로필입니다."));

        // 좋아요 생성, +1
        Optional<Likes> likes = likesRepository.findByMemberAndProfile(profile, currentMember);
        if (likes.isPresent()) {
            profile.decreaseLikesCount();
            likesRepository.delete(likes.get());
            return;
        }
        likesRepository.save(Likes.builder().member(currentMember).profile(profile).build());
    }

    public List<Likes> getLikes(){
        Member member = memberRepository.findMemberByEmail(getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다."));
        return likesRepository.findByMember_Id(member.getId());
    }
}
