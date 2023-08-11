package Dcoding.Celebrem.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id @GeneratedValue
    @Column(name = "profile_id")
    private Long id;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "profile")
    private List<ProfileTag> profileTags = new ArrayList<>();

    private Long instagramId;

    private Long followerCount;

    private String description;

    private Long likeCount;
}
