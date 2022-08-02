package hello.advanced.app.v3;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerV3Test {
    @LocalServerPort
    private int port;
    private final String URI = "/v3/request";

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

        assertThat(주문_요청.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void exceptionTest() {
        ExtractableResponse<Response> 주문_요청 = RestAssured.given().log().all()
            .queryParam("itemId", "ex")
            .when().get(URI)
            .then().log().all().extract();

        assertThat(주문_요청.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void manyRequest() {

        ExtractableResponse<Response> 주문_요청1 = RestAssured.given().log().all()
            .queryParam("itemId", "hello")
            .when().get(URI)
            .then().log().all().extract();

        ExtractableResponse<Response> 예외_발생_요청 = RestAssured.given().log().all()
            .queryParam("itemId", "ex")
            .when().get(URI)
            .then().log().all().extract();

        ExtractableResponse<Response> 주문_요청3 = RestAssured.given().log().all()
            .queryParam("itemId", "hello")
            .when().get(URI)
            .then().log().all().extract();

        assertThat(주문_요청1.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(예외_발생_요청.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(주문_요청3.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
