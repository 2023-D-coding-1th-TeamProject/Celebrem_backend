package Dcoding.Celebrem.dto.member;

import Dcoding.Celebrem.domain.Authority;
import Dcoding.Celebrem.domain.Member;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateRequestDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String name;
    private List<String> categories;
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .authority(Authority.ROLE_USER)
                .build();
    }


}
