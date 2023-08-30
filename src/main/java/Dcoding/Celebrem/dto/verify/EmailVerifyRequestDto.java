package Dcoding.Celebrem.dto.verify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerifyRequestDto {

    private String email;
    private String code;
}
