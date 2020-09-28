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
package com.aklimenko.miro.integration;

import static org.hamcrest.Matchers.equalTo;

import com.aklimenko.miro.MiroConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitRuleApiITest {
  @LocalServerPort private int serverPort;
  @Autowired private MiroConfig config;

  @BeforeEach
  void restAssuredPort() {
    RestAssured.port = serverPort;
  }

  @Test
  @DisplayName("should read rate limit rule")
  void shouldReadRateLimitRule() {
    // given / when
    var request = RestAssured.given().when().contentType(ContentType.JSON).get("/rateLimitRule");

    // then
    request
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("windowSizeMS", equalTo(config.getRateLimit().getWindowSizeMS()))
        .body("limitGlobal", equalTo(config.getRateLimit().getLimitGlobal()))
        .body("limitListWidgets", equalTo(config.getRateLimit().getLimitListWidgets()))
        .body("limitReadWidget", equalTo(config.getRateLimit().getLimitReadWidget()))
        .body("limitCreateWidget", equalTo(config.getRateLimit().getLimitCreateWidget()))
        .body("limitUpdateWidget", equalTo(config.getRateLimit().getLimitUpdateWidget()))
        .body("limitDeleteWidget", equalTo(config.getRateLimit().getLimitDeleteWidget()));
  }

  @Test
  @DisplayName("should update rate limit rule")
  void shouldUpdateRateLimitRule() {
    // given / when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"windowSizeMS\":999999,\"limitGlobal\":777777}")
            .put("/rateLimitRule");

    // then
    request
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("windowSizeMS", equalTo(999999))
        .body("limitGlobal", equalTo(777777))
        .body("limitListWidgets", equalTo(config.getRateLimit().getLimitListWidgets()))
        .body("limitReadWidget", equalTo(config.getRateLimit().getLimitReadWidget()))
        .body("limitCreateWidget", equalTo(config.getRateLimit().getLimitCreateWidget()))
        .body("limitUpdateWidget", equalTo(config.getRateLimit().getLimitUpdateWidget()))
        .body("limitDeleteWidget", equalTo(config.getRateLimit().getLimitDeleteWidget()));
  }
}
