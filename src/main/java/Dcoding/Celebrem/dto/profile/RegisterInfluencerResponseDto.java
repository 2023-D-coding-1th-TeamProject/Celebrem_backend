package Dcoding.Celebrem.dto.profile;

import Dcoding.Celebrem.domain.member.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInfluencerResponseDto {
    Authority type;
}
