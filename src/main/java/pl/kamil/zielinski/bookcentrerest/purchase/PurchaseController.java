package pl.kamil.zielinski.bookcentrerest.purchase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamil.zielinski.bookcentrerest.client.ClientEntity;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;
import pl.kamil.zielinski.bookcentrerest.commons.ErrorHandler;
import pl.kamil.zielinski.bookcentrerest.commons.UriConstants;


@RestController
@RequestMapping(UriConstants.purchaseUri)
public class PurchaseController {
    private PurchaseService purchaseService;

    @Autowired
    PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }


    @Operation(summary = "Get amount of purchases", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Retrieved amount of purchases", content = {@Content(mediaType = "application/json", examples = @ExampleObject(value = "14", description = "amount of purchases"), schema = @Schema(description = "amount of purchases", implementation = Integer.class))})
    })
    @GetMapping
    public int getAmountOfPurchases() {
        return purchaseService.getAmountOfPurchases();
    }


    @Operation(summary = "Create a new empty purchase", method = "POST", responses = {
            @ApiResponse(responseCode = "201", description = "Created new purchase", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createPurchase() {
        try {
            return purchaseService.createPurchase();
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }


    @Operation(summary = "Fill purchase data", method = "PUT", responses = {
            @ApiResponse(responseCode = "200", description = "Purchase data is modified", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Not enough copies in magazine", content = @Content),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchase(@PathVariable("id") int id, @RequestHeader("If-Match") int hashcode, @RequestBody PurchaseEntity purchaseEntity) {
        try {
            return purchaseService.updatePurchase(id, hashcode, purchaseEntity);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }


    @Operation(summary = "Get purchase info", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Get purchase info", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchaseInfo(@PathVariable("id") int id) {
        try {
            return purchaseService.getPurchaseInfo(id);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }


    @Operation(summary = "Get isPaid info", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Get isPaid info", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
    })
    @GetMapping("/{id}/isPaid")
    public ResponseEntity<?> getIsPaid(@PathVariable("id") int id) {
        try {
            return purchaseService.getIsPaid(id);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }


    @Operation(summary = "Update isPaid value", method = "PUT", responses = {
            @ApiResponse(responseCode = "200", description = "isPaid value updated successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))}),
            @ApiResponse(responseCode = "400", description = "Incorrect isPaid value", content = @Content),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PutMapping("/{id}/isPaid")
    public ResponseEntity<?> changeIsPaidValue(@PathVariable("id") int id, @RequestHeader("If-Match") int hashcode, @RequestBody int isPaid) {
        try {
            return purchaseService.changeIsPaidValue(id, hashcode, isPaid);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }


    @Operation(summary = "Cancel purchase", method = "DELETE", responses = {
            @ApiResponse(responseCode = "204", description = "Purchase cancelled successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "You can not cancel paid purchase", content = @Content),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelPurchase(@PathVariable("id") int id, @RequestHeader("If-Match") int hashcode) {
        try {
            return purchaseService.cancelPurchase(id, hashcode);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

}
