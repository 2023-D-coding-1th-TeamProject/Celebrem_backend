package Dcoding.Celebrem.dto.profile;

import Dcoding.Celebrem.domain.tag.ProfileTag;
import Dcoding.Celebrem.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateProfileResponseDto {

    String profileImageUrl;
    String description;
    String instagramId;
    List<ProfileTag> profileTags;
}
