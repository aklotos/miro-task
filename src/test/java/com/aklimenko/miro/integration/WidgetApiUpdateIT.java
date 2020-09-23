package com.aklimenko.miro.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.aklimenko.miro.model.Widget;
import com.aklimenko.miro.persistence.WidgetRepositoryImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
public class WidgetApiUpdateIT {

  @LocalServerPort private int serverPort;
  @Autowired WidgetRepositoryImpl widgetRepository;
  @Autowired ObjectMapper objectMapper;

  @BeforeEach
  void restAssuredPort() {
    RestAssured.port = serverPort;
  }

  @AfterEach
  void cleanUp() {
    widgetRepository.cleanUp();
  }

  @Test
  @DisplayName("should update a widget by id")
  void updateWidgetById() throws IOException {
    // given
    var createWidgetRequest = new HttpPost("http://localhost:" + serverPort + "/widgets");
    createWidgetRequest.setEntity(
        new StringEntity("{\"x\":1,\"y\":1,\"z\":1,\"width\":1.0,\"height\":1.0}"));
    createWidgetRequest.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    var createdWidgetResponse = HttpClientBuilder.create().build().execute(createWidgetRequest);
    var jsonFromResponse = EntityUtils.toString(createdWidgetResponse.getEntity());
    var widget = objectMapper.readValue(jsonFromResponse, new TypeReference<Widget>() {});

    // when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":2,\"y\":2,\"z\":2,\"width\":2.0,\"height\":2.0}")
            .put("/widgets/" + widget.getId());

    // then
    request
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(widget.getId()))
        .body("x", equalTo(2))
        .body("y", equalTo(2))
        .body("z", equalTo(2))
        .body("width", equalTo(2.0f))
        .body("height", equalTo(2.0f))
        .body("lastModifiedAt", notNullValue());
  }

  @Test
  @DisplayName("should shift other widgets when updating with existing z-index")
  void shiftOtherWidgetsWhenUpdatingExistingZIndex() throws IOException {
    // given
    var ids = new ArrayList<String>();
    for (int i = 1; i <= 3; i++) {
      var createWidgetRequest = new HttpPost("http://localhost:" + serverPort + "/widgets");
      var body =
          String.format(
              "{\"x\":%d,\"y\":%d,\"z\":%d,\"width\":%d.0,\"height\":%d.0}", i, i, i, i, i);
      createWidgetRequest.setEntity(new StringEntity(body));
      createWidgetRequest.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      var createdWidgetResponse = HttpClientBuilder.create().build().execute(createWidgetRequest);
      var jsonFromResponse = EntityUtils.toString(createdWidgetResponse.getEntity());
      var widget = objectMapper.readValue(jsonFromResponse, new TypeReference<Widget>() {});
      ids.add(widget.getId());
    }
    var id = ids.get(0);

    // when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"z\":2}")
            .put("/widgets/" + id);

    // then
    request
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(id))
        .body("x", equalTo(1))
        .body("y", equalTo(1))
        .body("z", equalTo(2))
        .body("width", equalTo(1.0f))
        .body("height", equalTo(1.0f))
        .body("lastModifiedAt", notNullValue());

    var widgets =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .get("/widgets")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<Widget>>() {});
    assertThat(widgets.size(), equalTo(3));
    var zIndexes = widgets.stream().map(Widget::getZ).collect(Collectors.toList());
    assertThat(zIndexes, equalTo(Arrays.asList(2, 3, 4)));
  }

  @Test
  @DisplayName("should return 404 if widget not found")
  void failUpdateNotExistingWidget() {
    // given/when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{\"x\":1,\"y\":2,\"width\":4.0,\"height\":5.0}")
            .put("/widgets/123-456-789");

    // then
    request
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
        .body("error", equalTo("Widget [id=123-456-789] not found."));
  }

  @Test
  @DisplayName("should return 400 if invalid payload provided")
  void failUpdateWithInvalidPayload() {
    // given/when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("}} _not a json_ {\"x\":1,\"y\":2,\"width\":4.0,\"height\":5.0}")
            .put("/widgets/123-456-789");

    // then
    request
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
        .body("error", equalTo("Invalid JSON input."));
  }

  @Test
  @DisplayName("should return 400 if empty payload provided")
  void failUpdateWithEmptyPayload() {
    // given/when
    var request =
        RestAssured.given()
            .when()
            .contentType(ContentType.JSON)
            .body("{}")
            .put("/widgets/123-456-789");

    // then
    request
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .contentType(ContentType.JSON)
        .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
        .body(
            "error",
            equalTo(
                "At least one of the fields 'x', 'y', 'z', 'width' or 'height' must be provided."));
  }
}
