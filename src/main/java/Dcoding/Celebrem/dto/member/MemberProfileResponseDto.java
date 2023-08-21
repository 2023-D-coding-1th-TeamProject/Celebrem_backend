package Dcoding.Celebrem.dto.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MemberProfileResponseDto {

    String nickname;
    String email;
    String profileImageUrl;
    String description;
    String instagramId;
    List<String> profileTagNames;

    public MemberProfileResponseDto(String nickname, String email, String profileImageUrl, String description, String instagramId, List<String> profileTagNames) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.description = description;
        this.instagramId = instagramId;
        this.profileTagNames = profileTagNames;
    }
}
