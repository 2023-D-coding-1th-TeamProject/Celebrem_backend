package Dcoding.Celebrem.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TagSetupRequestDto {

    private List<String> tagNames;
}
