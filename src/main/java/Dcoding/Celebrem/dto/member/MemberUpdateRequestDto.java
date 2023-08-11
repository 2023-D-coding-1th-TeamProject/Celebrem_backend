package Dcoding.Celebrem.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequestDto {

    private String name;
    private String profile;

}