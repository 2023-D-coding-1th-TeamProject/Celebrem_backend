package Dcoding.Celebrem.dto.profile;

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
    List<String> profileTagNames;
    
    public InfluencerProfileResponseDto(String nickname, String email, String profileImageUrl, String description, String instagramId, List<String> profileTagNames) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.instagramId = instagramId;
        this.description = description;
        this.profileTagNames = profileTagNames;
    }
}
