package pl.kamil.zielinski.bookcentrerest.purchase_merger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kamil.zielinski.bookcentrerest.commons.ErrorHandler;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;
import pl.kamil.zielinski.bookcentrerest.commons.UriConstants;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseEntity;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseService;

import java.util.Collection;


@RestController
@RequestMapping(UriConstants.purchaseMergerUri)
public class PurchaseMergerController {
    private PurchaseService purchaseService;

    @Autowired
    PurchaseMergerController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @Operation(summary = "Merge purchases", method = "POST", responses = {
            @ApiResponse(responseCode = "200", description = "Merged purchases", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PurchaseEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Purchase not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "At least one of purchases are paid", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> mergePurchases(@RequestBody Collection<PurchaseEntity> purchaseEntityCollection){
        try{
            return purchaseService.mergePurchases(purchaseEntityCollection);
        }catch(RequestException re){
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }
}
