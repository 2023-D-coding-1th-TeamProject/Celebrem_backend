package Dcoding.Celebrem.dto.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RegisterInfluencerRequestDto {

    String instagramId;
    List<String> tagNames;

    public RegisterInfluencerRequestDto(String instagramId, List<String> tagNames) {
        this.instagramId = instagramId;
        this.tagNames = tagNames;
    }
}
