package pl.kamil.zielinski.bookcentrerest.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public class RequestException extends Exception {
    private String message;
    private HttpStatus httpStatus;
}
