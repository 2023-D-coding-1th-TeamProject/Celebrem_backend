package Dcoding.Celebrem.dto.profile;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class InfluencerProfileResponseDto {

    String nickname;
    String email;
    String instagramId;
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
