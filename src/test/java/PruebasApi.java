import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

public class PruebasApi {

    @Test
    public void getBookingByIdTest() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Response response = RestAssured
                .given().pathParam("id", 1)
                .when().get("/booking/{id}");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("firstname", notNullValue());
        response.then().assertThat().body("lastname", notNullValue());
        response.then().assertThat().body("totalprice", notNullValue());
        response.then().assertThat().body("depositpaid", notNullValue());
        response.then().assertThat().body("bookingdates.checkin", notNullValue());
        response.then().assertThat().body("bookingdates.checkout", notNullValue());
        response.then().assertThat().body("additionalneeds", notNullValue());
        response.then().log().body();
    }

    @Test
    public void createBookingTest() throws Exception {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Booking booking = new Booking("Jim", "Brown", 111, true, "2018-01-01", "2019-01-01", "Breakfast");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .when().post("/booking");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("booking.firstname", equalTo(booking.getFirstname()));
        response.then().assertThat().body("booking.lastname", equalTo(booking.getLastname()));
        response.then().assertThat().body("booking.totalprice", equalTo(booking.getTotalprice()));
        response.then().assertThat().body("booking.depositpaid", equalTo(booking.isDepositpaid()));
        response.then().assertThat().body("booking.bookingdates.checkin", equalTo(booking.getBookingdates().getCheckin()));
        response.then().assertThat().body("booking.bookingdates.checkout", equalTo(booking.getBookingdates().getCheckout()));
        response.then().assertThat().body("booking.additionalneeds", equalTo(booking.getAdditionalneeds()));
        response.then().log().body();
    }

    @Test
    public void updateBookingTest() throws Exception {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Booking updatedBooking = new Booking("James", "Brown", 111, true, "2018-01-01", "2019-01-01", "Breakfast");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(updatedBooking);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Cookie", "token=abc123")
                .pathParam("id", 1)
                .body(payload)
                .when().put("/booking/{id}");

        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("firstname", equalTo(updatedBooking.getFirstname()));
        response.then().assertThat().body("lastname", equalTo(updatedBooking.getLastname()));
        response.then().assertThat().body("totalprice", equalTo(updatedBooking.getTotalprice()));
        response.then().assertThat().body("depositpaid", equalTo(updatedBooking.isDepositpaid()));
        response.then().assertThat().body("bookingdates.checkin", equalTo(updatedBooking.getBookingdates().getCheckin()));
        response.then().assertThat().body("bookingdates.checkout", equalTo(updatedBooking.getBookingdates().getCheckout()));
        response.then().assertThat().body("additionalneeds", equalTo(updatedBooking.getAdditionalneeds()));
        response.then().log().body();
    }

    @Test
    public void deleteBookingTest() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Response response = RestAssured
                .given().header("Cookie", "token=abc123")
                .pathParam("id", 1)
                .when().delete("/booking/{id}");

        response.then().assertThat().statusCode(201);
        response.then().log().body();
    }

    @Test
    public void createBookingWithNumericNameTest() throws Exception {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Booking booking = new Booking("12345", "Brown", 111, true, "2018-01-01", "2019-01-01", "Breakfast");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).accept(ContentType.JSON).body(payload)
                .when().post("/booking");

        response.then().assertThat().statusCode(200);
        response.then().log().body();
    }
}
