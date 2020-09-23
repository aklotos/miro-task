package com.aklimenko.miro.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MiroTaskApplicationTests {

  @Test
  @DisplayName("should start the application")
  void contextLoads() {}
}
