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

    private String email;

    private String description;

    private String instagramId;

    private Long followerCount;

    private Long likeCount;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "profile")
    private List<ProfileTag> profileTags = new ArrayList<>();

    @Builder
    public Profile(String instagramId, Long followerCount, String description, Long likeCount) {
        this.instagramId = instagramId;
        this.followerCount = followerCount;
        this.description = description;
        this.likeCount = likeCount;
    }

    @Builder
    public Profile(String email) {
        this.email = email;
    }

    public void changeProfileImage(String imageUrl) {
        this.profileImageUrl = imageUrl;
    }

}
