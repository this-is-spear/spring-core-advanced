package hello.advanced.app.v1;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerV1Test {

    @LocalServerPort
    private int port;
    private final String URI = "/v1/request";

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void request() {
        ExtractableResponse<Response> 주문_요청 = RestAssured.given().log().all()
            .queryParam("itemId", "hello")
            .when().get(URI)
            .then().log().all().extract();

        Assertions.assertThat(주문_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void exceptionTest() {
        ExtractableResponse<Response> 주문_요청 = RestAssured.given().log().all()
            .queryParam("itemId", "ex")
            .when().get(URI)
            .then().log().all().extract();

        Assertions.assertThat(주문_요청.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
