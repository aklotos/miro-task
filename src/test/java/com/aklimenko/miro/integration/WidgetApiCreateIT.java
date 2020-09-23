package com.aklimenko.miro.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.aklimenko.miro.model.Widget;
import com.aklimenko.miro.persistence.WidgetRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WidgetApiCreateIT {

  @LocalServerPort private int serverPort;
  @Autowired private ObjectMapper objectMapper;
  @Autowired WidgetRepositoryImpl widgetRepository;

  @BeforeEach
  void restAssuredPort() {
    RestAssured.port = serverPort;
  }

  @AfterEach
  void cleanUp() {
    widgetRepository.cleanUp();
  }

  @Test
  @DisplayName("should create a single widget with z-index")
  void createWidgetWithZIndex() {
    // given/when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":1,\"y\":2,\"z\":3,\"width\":4.0,\"height\":5.0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body("id", notNullValue())
        .body("x", equalTo(1))
        .body("y", equalTo(2))
        .body("z", equalTo(3))
        .body("width", is(4.0f))
        .body("height", is(5.0f))
        .body("lastModifiedAt", notNullValue());
  }

  @Test
  @DisplayName("should create a single widget without z-index")
  void createWidgetWithoutZIndex() {
    // given/when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":1,\"y\":2,\"width\":4.0,\"height\":5.0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body("id", notNullValue())
        .body("x", equalTo(1))
        .body("y", equalTo(2))
        .body("z", equalTo(0))
        .body("width", is(4.0f))
        .body("height", is(5.0f))
        .body("lastModifiedAt", notNullValue());
  }

  @Test
  @DisplayName("should move new widget to the foreground")
  void moveNewWidgetToTheForeground() throws IOException {
    // given
    for (int i = 0; i < 10; i++) {
      var request = new HttpPost("http://localhost:" + serverPort + "/widgets");
      request.setEntity(new StringEntity("{\"x\":1,\"y\":1,\"z\":1,\"width\":1.0,\"height\":1.0}"));
      request.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      HttpClientBuilder.create().build().execute(request);
    }

    // when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":1,\"y\":2,\"width\":4.0,\"height\":5.0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body("id", notNullValue())
        .body("x", equalTo(1))
        .body("y", equalTo(2))
        .body("z", equalTo(11))
        .body("width", is(4.0f))
        .body("height", is(5.0f))
        .body("lastModifiedAt", notNullValue());
  }

  @Test
  @DisplayName("should shift widgets with the same and higher z-index upwards")
  void shiftWidgetsWithSameAndHigherZIndex() throws IOException {
    // given
    for (int i = 1; i <= 3; i++) {
      var request = new HttpPost("http://localhost:" + serverPort + "/widgets");
      var body =
          String.format(
              "{\"x\":%d,\"y\":%d,\"z\":%d,\"width\":%d.0,\"height\":%d.0}", i, i, i, i, i);
      request.setEntity(new StringEntity(body));
      request.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      HttpClientBuilder.create().build().execute(request);
    }

    // when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":0,\"y\":0,\"z\":1,\"width\":0,\"height\":0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.CREATED.value())
        .contentType(ContentType.JSON)
        .body("id", notNullValue())
        .body("x", equalTo(0))
        .body("y", equalTo(0))
        .body("z", equalTo(1))
        .body("width", is(0f))
        .body("height", is(0f))
        .body("lastModifiedAt", notNullValue());

    var getWidgets = new HttpGet("http://localhost:" + serverPort + "/widgets");
    var widgetsResponse = HttpClientBuilder.create().build().execute(getWidgets);
    var jsonFromResponse = EntityUtils.toString(widgetsResponse.getEntity());
    var widgets = objectMapper.readValue(jsonFromResponse, new TypeReference<List<Widget>>() {});
    widgets.forEach(
        w -> {
          assertThat(w.getZ(), greaterThan(w.getX()));
        });
  }

  @Test
  @DisplayName("should return 400 on missing field in the payload")
  void shouldFailOnMissingField() {
    // given / when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":1,\"width\":4.0,\"height\":5.0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("error", equalTo("Field 'y' must be provided."));
  }

  @Test
  @DisplayName("should return 400 on invalid payload")
  void shouldFailOnInvalidPayload() {
    // given / when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("}} not a json {\"x\":1,\"width\":4.0,\"height\":5.0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("error", equalTo("Invalid JSON input."));
  }

  @Test
  @DisplayName("should return 409 if z-index limit exceeded")
  void shouldConflictIfZIndexLimitExceeded() throws IOException {
    // given
    var createWidgetRequest = new HttpPost("http://localhost:" + serverPort + "/widgets");
    var maxInteger = Integer.MAX_VALUE;
    createWidgetRequest.setEntity(
        new StringEntity(
            "{\"x\":1,\"y\":1,\"z\":" + maxInteger + ",\"width\":1.0,\"height\":1.0}"));
    createWidgetRequest.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    HttpClientBuilder.create().build().execute(createWidgetRequest);

    // when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":1,\"y\":2,\"z\":" + maxInteger + ",\"width\":4.0,\"height\":5.0}")
            .post("/widgets");

    // then
    request
        .then()
        .statusCode(HttpStatus.CONFLICT.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo(HttpStatus.CONFLICT.value()));
  }
}
