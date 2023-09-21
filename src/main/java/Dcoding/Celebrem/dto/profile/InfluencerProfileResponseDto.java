package Dcoding.Celebrem.dto.profile;

import Dcoding.Celebrem.domain.member.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InfluencerProfileResponseDto {

    String nickname;
    String email;
    String instagramId;
    String profileImageUrl;
    String description;
    boolean isLike;
    List<String> profileTagNames;
    
    public InfluencerProfileResponseDto(String nickname, String email, String profileImageUrl, String description, String instagramId, boolean isLike, List<String> profileTagNames) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.instagramId = instagramId;
        this.description = description;
        this.isLike = isLike;
        this.profileTagNames = profileTagNames;
    }
}
