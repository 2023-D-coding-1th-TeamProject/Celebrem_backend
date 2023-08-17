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
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private String nickname;
    private String phoneNumber;
<<<<<<< HEAD
    private List<String> categories;

=======
>>>>>>> 38da7dc021f815abbde844ff29aeff76c0f95884
    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(userName)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .build();
    }

}
