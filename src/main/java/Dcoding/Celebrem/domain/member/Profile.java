package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.tag.ProfileTag;
import Dcoding.Celebrem.domain.tag.Tag;
import Dcoding.Celebrem.dto.profile.UpdateProfileResponseDto;
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
    private void changeProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }
    private void changeProfileDescription(String description) { this.description = description; }
    private void changeInstagramId(String instagramId) {this.instagramId = instagramId;}
    private void clearProfileTags() {
        if (this.profileTags.size() != 0)
            this.profileTags.clear();
    }

    public void addProfileTag(ProfileTag profileTag) {
        this.profileTags.add(profileTag);
    }

    public void clearTags() {
        this.profileTags.clear();
    }


    public UpdateProfileResponseDto UpdateProfileResponseDto() {
        return new UpdateProfileResponseDto(this.profileImageUrl, this.description, this.instagramId, this.profileTags);
    }

    public List<String> getProfileTagNames() {
        List<String> tagNames = new ArrayList<>();
        for (ProfileTag profileTag : this.profileTags) {
            tagNames.add(profileTag.getTagName());
        }
        return tagNames;
    }

    //--Test 메서드--//
    public Boolean isInstagramIdSame(String instagramId) {
        if(!this.instagramId.equals(instagramId)) {
            logger.info("Instagram IDs are different: Expected {}, Actual {}", this.instagramId, instagramId);
            return false;
        }
        return true;
    }

    public Boolean isProfileTagsSame(int profileTagsSize) {
        if(this.profileTags.size() != profileTagsSize) {
            logger.info("Tag Size is different: Expected {}, Actual {}", this.profileTags.size(), profileTagsSize);
            return false;
        }
        return true;
    }

    public Boolean isProfileImageUrlSame(String profileImageUrl) {
        if(!this.profileImageUrl.equals(profileImageUrl)) {
            logger.info("ProfileImageUrl is different: Expected {}, Actual {}", this.profileImageUrl, profileImageUrl);
            return false;
        }
        return true;
    }

    public Boolean isDescriptionSame(String description) {
        if(!this.description.equals(description)) {
            logger.info("Description is different: Expected {}, Actual {}", this.description, description);
            return false;
        }
        return true;
    }

    //--비즈니스 로직--//
    /**
     * 찜 추가
     */
    public Long increaseLikesCount() {
        return ++likeCount;
    }

    /**
     * 찜 취소
     */
    public Long decreaseLikesCount() {
        return --likeCount;
    }

    /**
     * 인플루언서 등록
     */
    public Profile registerInfluencer(String instagramId, Tag... tags) {
        this.instagramId = instagramId;
        for (Tag tag : tags) {
            ProfileTag profileTag = new ProfileTag(this, tag);
            this.addProfileTag(profileTag);
        }
        return this;
    }

    /**
     * 프로필 수정(update)
     */
    public void update(String profileImageUrl, String description, String instagramId, List<Tag> tags) {
        this.changeProfileImage(profileImageUrl);
        this.changeProfileDescription(description);
        this.changeInstagramId(instagramId);
        this.clearProfileTags();

        for (Tag tag : tags) {
            ProfileTag profileTag = new ProfileTag(this, tag);
            this.addProfileTag(profileTag);
        }
    }
}
