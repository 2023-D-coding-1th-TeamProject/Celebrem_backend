package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.domain.likes.Likes;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static Dcoding.Celebrem.domain.member.Authority.ROLE_USER;

@Entity
@NoArgsConstructor
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
    public Member(String email, String phoneNumber, String password, String nickname){
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickname = nickname;
        this.authority = ROLE_USER;
        this.profile = new Profile(email);
    }

    public static User memberToUser(Member member){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.authority.toString());
        return new User(
                String.valueOf(member.id),
                member.password,
                Collections.singleton(grantedAuthority)
        );
    }

    public static MemberCreateResponseDto of(Member member) {
        return new MemberCreateResponseDto(member.email, member.nickname, member.authority.toString());
    }

}
