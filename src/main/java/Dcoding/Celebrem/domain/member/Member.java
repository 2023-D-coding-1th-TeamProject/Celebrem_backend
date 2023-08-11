package Dcoding.Celebrem.domain.member;

import Dcoding.Celebrem.domain.base.BaseEntity;
import Dcoding.Celebrem.dto.member.MemberCreateResponseDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collections;

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

    private LocalDateTime lastLogin;

    private LocalDateTime withdrawalAt;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String email, String password, String nickname, Authority authority){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
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
