package Dcoding.Celebrem.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateResponseDto {
    private String email;
    private String name;
}
