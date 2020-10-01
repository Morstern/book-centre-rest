package pl.kamil.zielinski.bookcentrerest.book;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;
import pl.kamil.zielinski.bookcentrerest.commons.ErrorHandler;
import pl.kamil.zielinski.bookcentrerest.commons.UriConstants;

@RestController
@RequestMapping(UriConstants.bookUri)
public class BookController {
    private BookService bookService;

    @Autowired
    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Get all books", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookEntity.class)))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getAllBooks(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        try {
            return bookService.getAllBooks(page, size);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Get book info", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Get book info", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookInfo(@PathVariable("id") int id) {
        try {
            return bookService.getBookInfo(id);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Update book data", method = "PUT", responses = {
            @ApiResponse(responseCode = "200", description = "Book data is modified", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "428", description = "Request sent without Match-If field in header", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") int id, @RequestHeader(value = "If-Match", required = false) Integer hashcode, @RequestBody BookEntity bookEntity) {
        try {
            return bookService.updateBook(id, hashcode, bookEntity);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Update book amount.", method = "put", responses = {
            @ApiResponse(responseCode = "200", description = "Update book info", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PutMapping("/{id}/amount")
    public ResponseEntity<?> updateBookAmount(@PathVariable("id") int id, @RequestHeader("If-Match") int hashcode, @RequestBody BookEntity bookEntity) {
        try {
            return bookService.updateBookAmount(id, hashcode, bookEntity);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }


}
