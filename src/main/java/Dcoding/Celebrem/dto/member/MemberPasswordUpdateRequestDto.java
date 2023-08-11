package Dcoding.Celebrem.dto.member;

import lombok.Getter;

@Getter
public class MemberPasswordUpdateRequestDto {
    private String originalPassword;
    private String updatedPassword;
}
