package Dcoding.Celebrem.domain.tag;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.member.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileTag extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "profile_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public ProfileTag(Profile profile, Tag tag) {
        this.profile = profile;
        this.tag = tag;
    }

    //--연관관계 메서드--//
    public void connectTag(Tag tag) {
        this.tag = tag;
    }

    public void connectProfile(Profile profile) {
        this.profile = profile;
    }
}
