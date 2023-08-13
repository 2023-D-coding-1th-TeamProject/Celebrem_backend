package Dcoding.Celebrem.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileDto {
    String description;
    String profileImageUrl;
}
