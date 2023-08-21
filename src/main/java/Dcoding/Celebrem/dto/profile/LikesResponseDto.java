package Dcoding.Celebrem.dto.profile;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikesResponseDto {

    String nickname;
    String profileImageUrl;
    Long likesCount;

    public LikesResponseDto(Member member, Profile profile) {
        nickname = member.getNickname();
        profileImageUrl = profile.getProfileImageUrl();
        likesCount = getLikesCount();
    }
}
