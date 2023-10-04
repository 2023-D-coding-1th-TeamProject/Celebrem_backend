package Dcoding.Celebrem.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class InfluencerProfileResponseDto {

    Long profileId;
    String nickname;
    String email;
    String instagramId;
    String profileImageUrl;
    String description;
    boolean isLike;
    List<String> profileTagNames;
    

    public InfluencerProfileResponseDto(Long profileId, String nickname, String email, String profileImageUrl, String instagramId, String description, boolean isLike, List<String> profileTagNames) {
        this.profileId = profileId;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.instagramId = instagramId;
        this.description = description;
        this.isLike = isLike;
        this.profileTagNames = profileTagNames;
    }
}
