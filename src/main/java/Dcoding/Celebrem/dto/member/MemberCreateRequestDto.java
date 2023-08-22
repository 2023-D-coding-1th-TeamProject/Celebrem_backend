package Dcoding.Celebrem.dto.member;

import Dcoding.Celebrem.domain.member.Member;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequestDto {
    private String email;
    private String password;
    private String nickname;
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();
    }

}
