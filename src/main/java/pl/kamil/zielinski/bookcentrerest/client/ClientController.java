package pl.kamil.zielinski.bookcentrerest.client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamil.zielinski.bookcentrerest.commons.UriConstants;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseDto;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;
import pl.kamil.zielinski.bookcentrerest.commons.ErrorHandler;

@RestController
@RequestMapping(UriConstants.clientUri)
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @Operation(summary = "Get amount of clients", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Retrieved amount of clients", content = {@Content(mediaType = "application/json", examples = @ExampleObject(value = "14", description = "amount of clients"), schema = @Schema(description = "amount of clients", implementation = Integer.class))})
    })
    @GetMapping
    public ResponseEntity<?> getAmountOfClients() {
        return clientService.getAmountOfClients();
    }


    @Operation(summary = "Create a new empty client", method = "POST", responses = {
            @ApiResponse(responseCode = "201", description = "Created new client", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))}),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> createClient() {
        try {
            return clientService.createClient();
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Fill client data", method = "PUT", responses = {
            @ApiResponse(responseCode = "200", description = "Client data is modified", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable("id") int id, @RequestHeader("If-Match") int hashcode, @RequestBody ClientEntity clientEntity){
        try{
            return clientService.updateClient(id,hashcode,clientEntity);
        }catch(RequestException re){
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Get client info", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Get client info", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientInfo(@PathVariable("id") Integer id) {
        try {
            return clientService.getClientInfo(id);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Update client's e-mail, phone and/or address.", method = "PATCH", responses = {
            @ApiResponse(responseCode = "200", description = "Update client info", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientEntity.class))}),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateClient(@PathVariable("id") Integer id, @RequestHeader("If-Match") int hashcode, @RequestBody ClientEntity updatedClientEntity) {
        try {
            return clientService.partialUpdateClient(id, hashcode, updatedClientEntity);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Delete client data and all of his purchases", method = "DELETE", responses = {
            @ApiResponse(responseCode = "204", description = "Deleted client info", content = @Content),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Cancel all unpaid orders before you try to delete account", content = @Content),
            @ApiResponse(responseCode = "412", description = "Invalid ETag value", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClientData(@PathVariable("id") Integer id, @RequestHeader("If-Match") int hashcode) {
        try {
            return clientService.deleteClientData(id, hashcode);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }

    @Operation(summary = "Get all client purchases", method = "GET", responses = {
            @ApiResponse(responseCode = "200", description = "Update client info", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PurchaseDto.class)))}),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @GetMapping("/{id}/purchases")
    public ResponseEntity<?> getClientPurchases(@PathVariable("id") Integer id) {
        try {
            return clientService.getClientPurchases(id);
        } catch (RequestException re) {
            return ErrorHandler.convertErrorToResponseEntity(re);
        }
    }
}
