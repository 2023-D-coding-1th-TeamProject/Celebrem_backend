package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.common.exception.BadRequestException;
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
    private String email;
    private String password;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Likes> likes = new ArrayList<>();

    @Builder
    public Member(String email, String password, String nickname){
        this.email = email;
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

    public void changeRole() {
        if (this.authority.equals(ROLE_INFLUENCER)) throw new BadRequestException("이미 인플루언서가 등록된 유저입니다");
        this.authority = ROLE_INFLUENCER;
    }

}
