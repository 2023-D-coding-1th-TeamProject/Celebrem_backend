package Dcoding.Celebrem;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureRestDocs
public class DocsTest {

    @Test
    void test() {
        Assertions.assertThat(1).isEqualTo(1);
    }

}
