package Dcoding.Celebrem.dto.profile;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class FeedResponseDto {

    private Long influencerId;
    private String nickname;
    private String imageUrl;
    private Long likeCount;
    private List<String> tagNames;
    private boolean isLike;

    @Builder
    public FeedResponseDto(Long influencerId, String nickname, String imageUrl, Long likeCount, List<String> tagNames, boolean isLike) {
        this.influencerId = influencerId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.tagNames = tagNames;
        this.isLike = isLike;
    }
}
