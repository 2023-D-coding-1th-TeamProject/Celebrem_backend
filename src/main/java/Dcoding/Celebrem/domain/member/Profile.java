package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.tag.ProfileTag;
import Dcoding.Celebrem.dto.profile.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Profile extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

    private String profileImageUrl;

    private String description;

    private String instagramId;

    private Long followerCount;

    private Long likeCount = 0l;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileTag> profileTags = new ArrayList<>();

    @Builder
    public Profile(String instagramId, Long followerCount, String description, String profileImageUrl) {
        this.instagramId = instagramId;
        this.followerCount = followerCount;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    private static final Logger logger = LoggerFactory.getLogger(Profile.class);

    //--연관관계 메서드--//
    public void changeProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void addProfileTag(ProfileTag profileTag) {
        this.profileTags.add(profileTag);
    }

    public void clearTags() {
        this.profileTags.clear();
    }

    public List<String> getProfileTagNames() {
        List<String> tagNames = new ArrayList<>();
        for (ProfileTag profileTag : this.profileTags) {
            tagNames.add(profileTag.getTagName());
        }
        return tagNames;
    }

    public Long increaseLikesCount() {
        return ++likeCount;
    }

    public Long decreaseLikesCount() {
        return --likeCount;
    }

    public void registerInfluencer(RegisterInfluencerRequestDto requestDto) {
        this.instagramId = requestDto.getInstagramId();
        this.member.changeRole();
    }

    public void update(UpdateProfileRequestDto updateProfileDto) {
        this.description = updateProfileDto.getDescription();
        this.instagramId = updateProfileDto.getInstagramId();
    }

    public InfluencerProfileResponseDto getInfluencerProfile() {
        return new InfluencerProfileResponseDto(
                member.getNickname(),
                member.getEmail(),
                this.profileImageUrl,
                this.instagramId,
                this.description,
                this.getProfileTagNames());
    }
}
