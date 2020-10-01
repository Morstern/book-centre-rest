package pl.kamil.zielinski.bookcentrerest.commons;

import org.springframework.http.ResponseEntity;

public class ErrorHandler {
    public static ResponseEntity<?> convertErrorToResponseEntity(RequestException re) {
        return new ResponseEntity(re.getMessage(), re.getHttpStatus());
    }
}
