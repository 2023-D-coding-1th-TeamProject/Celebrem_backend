package Dcoding.Celebrem.dto.email;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendVerificationCodeRequestDto {

    private String email;
}
