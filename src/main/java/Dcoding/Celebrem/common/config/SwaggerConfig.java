package Dcoding.Celebrem.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Celebrem",
                description = "Celebrem api명세",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
//
//    @Bean
//    public GroupedOpenApi chatOpenApi() {
//        String[] paths = {"/Dcoding/**"};
//
//        return GroupedOpenApi.builder()
//                .group("Celebrem v1")
//                .pathsToMatch(paths)
//                .build();
//    }
}