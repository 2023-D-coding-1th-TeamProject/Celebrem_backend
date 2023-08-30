package Dcoding.Celebrem.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SearchProfileResponseDto {
    private Long influencerId;
    private String nickname;
    private String imageUrl;
    private Long likeCount;
    private List<String> tagNames;
    private boolean isLike;
}
