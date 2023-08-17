package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static Dcoding.Celebrem.domain.member.Authority.ROLE_INFLUENCER;
import static Dcoding.Celebrem.domain.member.Authority.ROLE_USER;

@Entity
@NoArgsConstructor
@Getter
public class Member extends BaseEntity {

    @Id
    @Column(name ="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    @Builder
    public Member(String email, String phoneNumber, String password, String nickname, Profile profile){
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickname = nickname;
        this.authority = ROLE_USER;
        this.profile = new Profile();
    }

    private static final Logger logger = LoggerFactory.getLogger(Profile.class);

    public static MemberCreateResponseDto of(Member member) {
        return new MemberCreateResponseDto(member.email, member.nickname, member.authority.toString());
    }

    public String authorityToString(){
        return this.authority.toString();
    }

    //--연관관계 메소드--//
    public void checkAuthorityToInfluencer() {
        if (this.authority != ROLE_USER)
            logger.info("already Influencer!!");
        this.authority = ROLE_INFLUENCER;
    }

    //--비즈니스 로직--//
    /**
     * 인플루언서 등록 V2
     */
    public void registerInfluencer(Profile profile) {
        this.profile = profile; // 이렇게 하면 안되고, 값만 바꿔줘야 하나?
    }
}
