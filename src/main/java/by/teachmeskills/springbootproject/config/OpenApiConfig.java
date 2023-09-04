package by.teachmeskills.springbootproject.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    /** Group beans*/
    @Bean
    public GroupedOpenApi publicCartApi() {
        return GroupedOpenApi.builder()
                .group("cart")
                .pathsToMatch("/**/cart/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicCategoryApi() {
        return GroupedOpenApi.builder()
                .group("categories")
                .pathsToMatch("/**/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicHomeApi() {
        return GroupedOpenApi.builder()
                .group("home")
                .pathsToMatch("/**/home/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicLoginApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/**/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicProductApi() {
        return GroupedOpenApi.builder()
                .group("products")
                .pathsToMatch("/**/products/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicProfileApi() {
        return GroupedOpenApi.builder()
                .group("profile")
                .pathsToMatch("/**/profile/**")
                .build();
    }

    @Bean
    public GroupedOpenApi publicSearchApi() {
        return GroupedOpenApi.builder()
                .group("search")
                .pathsToMatch("/**/search/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi(@Value("${application.description}") String appDescription,
                                 @Value("${application.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Book shop")
                        .version(appVersion)
                        .description(appDescription)
                        .license(new License().name("Apache 2.0")
                                .url("http://springdoc.org"))
                        //Contact information about organization of exposed API
                        .contact(new Contact().name("TeachMeSkills")
                                .email("tms@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8080")
                                .description("Dev service")));
    }
}
