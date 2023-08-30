package Dcoding.Celebrem.dto.profile;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class FeedResponseDto {
    private Long influencerId;
    private String nickname;
    private String imageUrl;
    private Long likeCount;
    private List<String> tagNames;
    private boolean isLike;

    public FeedResponseDto(Profile profile) {
        this.influencerId = profile.getId();
        this.nickname = profile.getMember().getNickname();
        this.imageUrl = profile.getProfileImageUrl();
        this.likeCount = profile.getLikeCount();
        this.tagNames = profile.getProfileTagNames();
        this.isLike = false;
    }

    public FeedResponseDto(Member member, Profile profile) {
        this.influencerId = profile.getId();
        this.nickname = profile.getMember().getNickname();
        this.imageUrl = profile.getProfileImageUrl();
        this.likeCount = profile.getLikeCount();
        this.tagNames = profile.getProfileTagNames();
        this.isLike = member.getLikes().stream().map(l -> l.getProfile()).collect(Collectors.toList()).contains(profile);
    }
}
