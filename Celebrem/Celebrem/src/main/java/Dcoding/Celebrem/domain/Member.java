package Dcoding.Celebrem.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private LocalDateTime lastLogin;

    private LocalDateTime createAt;

    private LocalDateTime withdrawalAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();
}
