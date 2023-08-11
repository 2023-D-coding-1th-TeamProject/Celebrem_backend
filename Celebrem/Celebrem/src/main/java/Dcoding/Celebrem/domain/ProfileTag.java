package Dcoding.Celebrem.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileTag {

    @Id @GeneratedValue
    @Column(name = "profile_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
