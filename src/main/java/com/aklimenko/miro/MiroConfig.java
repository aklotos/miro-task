package com.aklimenko.miro;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiroConfig {

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonBuilder() {
    return builder ->
        builder
            .featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
