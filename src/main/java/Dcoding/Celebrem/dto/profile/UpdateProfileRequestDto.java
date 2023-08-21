package Dcoding.Celebrem.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateProfileRequestDto {
    String description;
    String instagramId;
    List<String> tagNames;
}
