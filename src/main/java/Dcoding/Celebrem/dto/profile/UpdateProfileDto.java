package Dcoding.Celebrem.dto.profile;

import Dcoding.Celebrem.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateProfileDto {
    String profileImageUrl;
    String description;
    String instagramId;
    List<String> tagNames;
}
