package pl.kamil.zielinski.bookcentrerest.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.parsing.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.kamil.zielinski.bookcentrerest.book.BookEntity;
import pl.kamil.zielinski.bookcentrerest.book.BookRepository;

import java.util.Map;

import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=9090"})
public class BookServiceTest {
    @Autowired
    private BookRepository bookRepository;

    @Before
    public void init() {
        RestAssured.port = 9090;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    public void whenFetch_thenReturnBooks_givenNoParams() {
        RestAssured
                .given()
                .when().get("/api/books")
                .then()
                .assertThat()
                .statusCode(200).and()
                .contentType(ContentType.JSON).and()
                .body("size()", is(5));
    }

    @Test
    public void whenFetch_thenReturnBooks_givenParams() {
        Map<String, String> queryParams = Map.of(
                "page", String.valueOf(1),
                "size", String.valueOf(2));

        RestAssured
                .given().queryParams(queryParams)
                .when().get("/api/books")
                .then()
                .assertThat()
                .statusCode(200).and()
                .contentType(ContentType.JSON).and()
                .body("size()", is(2)).and()
                .body("[0].bookId", is(3)).and()
                .body("[1].bookId", is(4));
    }

    @Test
    public void whenFetch_thenReturnBook_givenValidId() {
        RestAssured
                .given()
                .when().get("/api/books/1")
                .then().assertThat()
                .statusCode(200).and()
                .contentType(ContentType.JSON).and()
                .body("bookId", is(1));
    }

    @Test
    public void whenFetch_thenCode404_givenInvalidId() {
        RestAssured
                .given()
                .when().get("/api/books/1545")
                .then().assertThat()
                .statusCode(404);
    }

    @Test
    public void whenUpdateAmount_thenCode412_givenInvalidETagValue() {
        BookEntity updatedBookEntity = BookEntity.builder().amount(56).build();
        Headers headers = Headers.headers(new Header("If-Match", String.valueOf(1234234)), new Header("Content-Type", ContentType.JSON.toString()));
        RestAssured
                .given().headers(headers).body(updatedBookEntity)
                .when().put("/api/books/1/amount")
                .then().assertThat()
                .statusCode(412);
    }

    @Test
    public void whenUpdateAmount_thenCode400_givenNoHeaders() {

        BookEntity updatedBookEntity = BookEntity.builder().amount(56).build();
        RestAssured
                .given().body(updatedBookEntity)
                .when().put("/api/books/1/amount")
                .then().assertThat()
                .statusCode(400);
    }

    @Test
    public void whenUpdateAmount_thenCode400_givenNoBody() {

        Headers headers = Headers.headers(new Header("If-Match", String.valueOf(1234234)), new Header("Content-Type", ContentType.JSON.toString()));

        RestAssured
                .given().headers(headers)
                .when().put("/api/books/1/amount")
                .then().assertThat()
                .statusCode(400);
    }

    // TODO: Transactional doesn't work
    @Test
    @Transactional
    @Rollback
    public void whenUpdateAmount_thenUpdate_givenValidETagValue() {

        BookEntity updatedBookEntity = BookEntity.builder().amount(60).build();
        Headers headers = Headers.headers(new Header("If-Match", String.valueOf(2108976180)), new Header("Content-Type", ContentType.JSON.toString()));
        RestAssured
                .given().headers(headers).body(updatedBookEntity)
                .when().put("/api/books/1/amount")
                .then().assertThat()
                .statusCode(200).and()
                .contentType(ContentType.JSON).and()
                .body("amount", is(60));

    }
}
