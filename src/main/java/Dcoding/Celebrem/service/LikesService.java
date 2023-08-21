package Dcoding.Celebrem.service;

import Dcoding.Celebrem.common.exception.NotFoundException;
import Dcoding.Celebrem.common.exception.UnauthorizedException;
import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.profile.LikesResponseDto;
import Dcoding.Celebrem.repository.LikesRepository;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Dcoding.Celebrem.common.util.SecurityUtil.getCurrentMemberEmail;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private static final Logger logger = LoggerFactory.getLogger(LikesService.class);

    @Transactional
    public void like(Long profileId) {
        Member currentMember = memberRepository.findMemberByEmail(getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다."));
        Profile profile = profileRepository.findById(profileId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 프로필입니다."));

        Optional<Likes> likes = likesRepository.findByMemberAndProfile(profile, currentMember);
        if (likes.isPresent()) {
            profile.decreaseLikesCount();
            likesRepository.delete(likes.get());
            return;
        }
        likesRepository.save(Likes.builder().member(currentMember).profile(profile).build());
    }

    public List<LikesResponseDto> getAllLikesProfile(){
        Member member = memberRepository.findMemberByEmail(getCurrentMemberEmail()).orElseThrow(
                () -> new UnauthorizedException("로그인이 필요합니다."));
        List<Likes> likes = likesRepository.findAllByMemberFetchProfile(member);
        return likes.stream().map(l -> l.getInfluencerInfo()).collect(Collectors.toList());
    }
}
