package Dcoding.Celebrem.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RegisterInfluencerResponseDto {

    String nickname;
    String email;
    String profileImageUrl;
    String description;
    String instagramId;
    List<String> profileTagNames;

    public RegisterInfluencerResponseDto(String nickname, String email, String profileImageUrl, String description, String instagramId, List<String> profileTagNames) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.instagramId = instagramId;
        this.profileTagNames = profileTagNames;
    }
}
