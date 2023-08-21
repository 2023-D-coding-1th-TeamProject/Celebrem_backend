package Dcoding.Celebrem.dto.profile;

import java.util.List;

public class InfluencerProfileResponseDto {

    String nickname;
    String email;
    String profileImageUrl;
    String description;
    List<String> profileTagNames;

    public InfluencerProfileResponseDto(String nickname, String email, String profileImageUrl, String description, List<String> profileTagNames) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.profileTagNames = profileTagNames;
    }
}
