package Dcoding.Celebrem.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedResponseDto {
    private String nickname;
    private String imageUrl;
    private Long likeCount;
    private List<String> tagNames;
    private boolean isLike;

    @Builder
    public FeedResponseDto(String nickname, String imageUrl, Long likeCount, List<String> tagNames, boolean isLike) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.isLike = isLike;
    }
}
