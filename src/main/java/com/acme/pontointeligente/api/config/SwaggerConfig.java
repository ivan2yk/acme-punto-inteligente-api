package com.acme.pontointeligente.api.config;

import com.acme.pontointeligente.api.security.utils.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Ivan on 4/10/2018.
 */
@Configuration
@Profile("dev")
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.acme.pontointeligente.api.controllers"))
                .paths(PathSelectors.any()).build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Acme punto inteligente API")
                .description("Documentacion del API punto inteligente de ACME")
                .build();
    }

    @Bean
    public SecurityConfiguration securityConfiguration(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        String token;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername("admin@acme.com");
            token = jwtTokenUtil.obterToken(userDetails);
        } catch (Exception ex) {
            token = "";
        }

        return new SecurityConfiguration(
                null,
                null,
                null,
                null,
                "Bearer " + token,
                ApiKeyVehicle.HEADER,
                "Authorization",
                ",");
    }

}
