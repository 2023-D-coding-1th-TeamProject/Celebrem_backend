package Dcoding.Celebrem.domain.likes;

import Dcoding.Celebrem.domain.member.Member;
import Dcoding.Celebrem.domain.member.Profile;
import Dcoding.Celebrem.dto.profile.LikesResponseDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Likes {

    @Id @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id")
    private Member member;

    @Builder
    public Likes(Profile profile, Member member) {
        this.profile = profile;
        this.member = member;
        this.profile.increaseLikesCount();
    }

    public LikesResponseDto getInfluencerInfo() {
        return new LikesResponseDto(member, profile);
    }
}
