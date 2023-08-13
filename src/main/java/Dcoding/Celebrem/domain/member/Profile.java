package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.tag.ProfileTag;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
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

    @OneToMany(mappedBy = "profile")
    private List<ProfileTag> profileTags = new ArrayList<>();

    @Builder
    public Profile(String instagramId, Long followerCount, String description, String profileImageUrl) {
        this.instagramId = instagramId;
        this.followerCount = followerCount;
        this.description = description;
        this.profileImageUrl = profileImageUrl;
    }

    //--연관관계 메서드--//
    public void changeProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }
    public void changeProfileDescription(String description) { this.description = description; }
}
