/*
 Copyright 2020 Anton Klimenko

 <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 except in compliance with the License. You may obtain a copy of the License at

 <p>http://www.apache.org/licenses/LICENSE-2.0

 <p>Unless required by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 express or implied. See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.aklimenko.miro;

import com.aklimenko.miro.model.ratelimit.RateLimitRule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiroTaskApplication {

  public static void main(String[] args) {
    SpringApplication.run(MiroTaskApplication.class, args);
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jacksonBuilder() {
    return builder ->
        builder
            .featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .featuresToEnable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Bean
  public MiroConfig config() {
    return new MiroConfig();
  }

  @Bean
  public RateLimitRule rateLimitRule(final MiroConfig config) {
    return new RateLimitRule(config);
  }
}
