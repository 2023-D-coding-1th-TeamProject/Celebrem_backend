package Dcoding.Celebrem.service;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.UpdateProfileDto;
import Dcoding.Celebrem.repository.MemberRepository;
import Dcoding.Celebrem.repository.ProfileRepository;
import Dcoding.Celebrem.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    /**
     * 인플루언서 등록하기
     * user_role -> 받은 정보로 등록
     * influencer_role -> 이미 등록된 사용자 -> 등록 불가
     */
    @Transactional
    public void registerInfluencer(Long memberId, String instagramId, Tag... tags) {
        // 엔티티 조회 : member는 빈 profile 객체를 가지고 있는 상태
        Member member = memberRepository.findById(memberId).get();
        Profile profile = profileRepository.findByMember_Id(memberId);

        // 인플루언서 등록 가능한 아이디인지 검사
        member.checkAuthorityToInfluencer();

        // 정보가 입력 된 프로필 객체 생성
        Profile getProfile = profile.registerInfluencer(instagramId, tags);

        // 등록
        member.registerInfluencer(getProfile);
    }

    /**
     *  프로필 업데이트 메소드
     */
    @Transactional
    public void updateProfile(Long profileId, UpdateProfileDto updateProfileDto){
        Profile profile = findById(profileId);
        List<Tag> updateTags = new ArrayList<>();

        for(String tagName : updateProfileDto.getTagNames()){
            updateTags.add(tagRepository.findByName(tagName));
        }

        profile.update(updateProfileDto.getProfileImageUrl(), updateProfileDto.getDescription(), updateProfileDto.getInstagramId(), updateTags);
    }

    /**
     * 프로필 반환 메소드
     */
    public Profile findById(Long profileId) {
        Optional<Profile> profile = profileRepository.findById(profileId);
        if(profile.isPresent()){
            return profile.get();
        }
        return null;
    }

    /**
     * 찜 목록 가져오기 v1 : 프로필 화면에서 이름 검색만
     * 프로필사진, 닉네임, 날짜?, 좋아요 수, 팔로워 수
     * fromId(좋아요를 누른 사람)으로 조회해 ToId(인플루언서) 목록을 가져온다.
     */
    public void findAllByName() {
        //return likesRepository.findAllByName(likesSearch.getName());
    }

    /**
     * 인플루언서 목록 가져오기 : 인플루언서 검색 화면에서  태그+조건 검색 -> 동적쿼리
     * profileService에 구현?
     */
    /*
    public void findAllByName(MainSearch mainSearch) {
        //return profileRepository.findAllByName(mainSearch);
    }
     */

}
