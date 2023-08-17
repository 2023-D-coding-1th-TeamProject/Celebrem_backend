package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.tag.ProfileTag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    private Long likeCount;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileTag> profileTags = new ArrayList<>();

    @Builder
    public Profile(String instagramId, Long followerCount, String description) {
        this.instagramId = instagramId;
        this.followerCount = followerCount;
        this.description = description;
        this.likeCount = 0L;
    }

    public void changeProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

    public void addProfileTag(ProfileTag profileTag) {
        this.profileTags.add(profileTag);
    }

    public void clearTags() {
        this.profileTags.clear();
    }

}
