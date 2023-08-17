package Dcoding.Celebrem.dto.tag;

import Dcoding.Celebrem.domain.tag.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class GetTagsResponseDto {
    List<String> tagNames;

    public GetTagsResponseDto(List<Tag> tags) {
        this.tagNames = tags.stream().map(t -> t.getName()).collect(Collectors.toList());
    }
}
