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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.aklimenko.miro.model.widget.Widget;
import com.aklimenko.miro.persistence.WidgetRepositoryImpl;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
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
public class WidgetApiReadIT {

  @LocalServerPort private int serverPort;
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
  @DisplayName("should read widgets ordered by z-index")
  void shouldReadWidgets() throws IOException {
    // given
    for (int i = 5; i >= 1; i--) {
      var request = new HttpPost("http://localhost:" + serverPort + "/widgets");
      var body =
          String.format(
              "{\"x\":%d,\"y\":%d,\"z\":%d,\"width\":%d.0,\"height\":%d.0}", i, i, i, i, i);
      request.setEntity(new StringEntity(body));
      request.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      HttpClientBuilder.create().build().execute(request);
    }

    // when
    var request = RestAssured.given().when().contentType(ContentType.JSON).get("/widgets");

    // then
    var response =
        request
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .as(new TypeRef<List<Widget>>() {});
    assertThat(5, equalTo(response.size()));
    for (int i = 1; i <= 5; i++) {
      var widget = response.get(i - 1);
      assertThat(widget.getId(), notNullValue());
      assertThat(widget.getX(), equalTo(i));
      assertThat(widget.getY(), equalTo(i));
      assertThat(widget.getZ(), equalTo(i));
      assertThat(widget.getHeight(), equalTo((double) i));
      assertThat(widget.getWidth(), equalTo((double) i));
      assertThat(widget.getLastModifiedAt(), notNullValue());
    }
    var ids = response.stream().map(Widget::getId).collect(Collectors.toSet());
    assertThat(ids.size(), equalTo(5));
  }

  @Test
  @DisplayName("should read single widget by id")
  void shouldReadWidgetsById() throws IOException {
    // given
    for (int i = 5; i >= 1; i--) {
      var request = new HttpPost("http://localhost:" + serverPort + "/widgets");
      var body =
          String.format(
              "{\"x\":%d,\"y\":%d,\"z\":%d,\"width\":%d.0,\"height\":%d.0}", i, i, i, i, i);
      request.setEntity(new StringEntity(body));
      request.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      HttpClientBuilder.create().build().execute(request);
    }

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
    var id = widgets.get(2).getId();

    // when
    var request = RestAssured.given().when().contentType(ContentType.JSON).get("/widgets/" + id);

    // then
    request
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("id", equalTo(id))
        .body("x", equalTo(3))
        .body("y", equalTo(3))
        .body("z", equalTo(3))
        .body("width", equalTo(3.0f))
        .body("height", equalTo(3.0f))
        .body("lastModifiedAt", notNullValue());
  }

  @Test
  @DisplayName("should return 404 if widget not found")
  void shouldFailIfWidgetNotFound() {
    // when
    var request =
        RestAssured.given().when().contentType(ContentType.JSON).get("/widgets/123-456-789");

    // then
    request
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value())
        .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
        .body("error", equalTo("Widget [id=123-456-789] not found."));
  }
}
