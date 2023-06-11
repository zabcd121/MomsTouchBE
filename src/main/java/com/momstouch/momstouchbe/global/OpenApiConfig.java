package com.momstouch.momstouchbe.global;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
        .title("맘스터치 API")
        .version("v1.0.0")
        .description("고급 웹프로그래밍");

    return new OpenAPI()
        .components(new Components())
        .info(info);
  }
}