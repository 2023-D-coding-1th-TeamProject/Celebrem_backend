package Dcoding.Celebrem.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {
    private String description;
    private String instagramId;
    private List<String> tagNames;
}
