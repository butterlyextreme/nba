package org.task.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

  private static final Set<Predicate<RequestHandler>> REQUEST_HANDLERS = Stream.of(
      "org.task.controller")
      .map(RequestHandlerSelectors::basePackage)
      .collect(Collectors.toSet());

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(Predicates.or(REQUEST_HANDLERS))
        .paths(PathSelectors.any())
        .build();
  }

}
