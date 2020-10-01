package pl.kamil.zielinski.bookcentrerest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pl.kamil.zielinski.bookcentrerest.commons.HeaderBuilder;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;
import pl.kamil.zielinski.bookcentrerest.commons.UriConstants;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseDto;
import pl.kamil.zielinski.bookcentrerest.purchase.PurchaseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<?> getAmountOfClients() {
        return new ResponseEntity<>(clientRepository.findAll().size(), HttpStatus.OK);
    }

    public ResponseEntity<?> getClientPurchases(Integer id) throws RequestException {
        Optional<ClientEntity> optionalClientEntity = clientRepository.findById(id);
        if (optionalClientEntity.isEmpty()) {
            throw new RequestException("Couldn't find a client with provided ID", HttpStatus.NOT_FOUND);
        }
        List<PurchaseDto> purchaseDtoList = optionalClientEntity.get().getPurchases().stream().map(PurchaseDto::new).collect(Collectors.toList());
        return new ResponseEntity<>(purchaseDtoList, HttpStatus.OK);
    }

    public ResponseEntity<?> createClient() throws RequestException {
        try {
            ClientEntity newClientEntity = new ClientEntity();
            newClientEntity.setPurchases(Arrays.asList());
            ClientEntity createdClient =  clientRepository.save(newClientEntity);
            MultiValueMap<String,String> headers = new HeaderBuilder()
                    .setETag(String.valueOf(createdClient.hashCode()))
                    .setLocation(UriConstants.clientUri+"/"+createdClient.getClientId())
                    .build();

            return new ResponseEntity<>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateClient(int id, int hashcode, ClientEntity clientEntity) throws RequestException {
        Optional<ClientEntity> optionalClientEntity = clientRepository.findById(id);
        if (optionalClientEntity.isEmpty()) {
            throw new RequestException("Couldn't find a client with provided ID", HttpStatus.NOT_FOUND);

        }

        if (optionalClientEntity.get().hashCode()!=hashcode) {
            throw new RequestException("There has been a new update for the client resource with id: "+ optionalClientEntity.get().getClientId()+", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        try{
            ClientEntity databaseClientEntity = optionalClientEntity.get();
            databaseClientEntity.setEmail(clientEntity.getEmail());
            databaseClientEntity.setPassword(clientEntity.getPassword());
            databaseClientEntity.setForename(clientEntity.getForename());
            databaseClientEntity.setSurname(clientEntity.getSurname());
            databaseClientEntity.setPhone(clientEntity.getPhone());
            databaseClientEntity.setAddress(clientEntity.getAddress());
            return new ResponseEntity<>(clientRepository.save(databaseClientEntity), HttpStatus.OK);
        }catch(Exception e){
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getClientInfo(Integer id) throws RequestException {
        Optional<ClientEntity> optionalClientEntity = clientRepository.findById(id);
        if (optionalClientEntity.isEmpty()) {
            throw new RequestException("Couldn't find a client with provided ID", HttpStatus.NOT_FOUND);
        }

        MultiValueMap<String,String> headers = new HeaderBuilder()
                .setETag(String.valueOf(optionalClientEntity.get().hashCode()))
                .build();

        return new ResponseEntity<>(optionalClientEntity.get(), headers, HttpStatus.OK);
    }

    public ResponseEntity<?> partialUpdateClient(Integer id, int hashcode, ClientEntity updatedClientEntity) throws RequestException {
        Optional<ClientEntity> optionalClientEntity = clientRepository.findById(id);
        if (optionalClientEntity.isEmpty()) {
            throw new RequestException("Couldn't find a client with provided ID", HttpStatus.NOT_FOUND);

        }

        if (optionalClientEntity.get().hashCode()!=hashcode) {
            throw new RequestException("There has been a new update for the client resource with id: "+ optionalClientEntity.get().getClientId()+", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        try {
            ClientEntity clientEntity = optionalClientEntity.get();
            clientEntity.setPhone(updatedClientEntity.getPhone());
            clientEntity.setEmail(updatedClientEntity.getEmail());
            clientEntity.setAddress(updatedClientEntity.getAddress());
            return new ResponseEntity<>(clientRepository.save(clientEntity), HttpStatus.OK);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> deleteClientData(Integer id, int hashcode) throws RequestException {
        Optional<ClientEntity> optionalClientEntity = clientRepository.findById(id);
        if (optionalClientEntity.isEmpty()) {
            throw new RequestException("Couldn't find a client with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalClientEntity.get().hashCode()!=hashcode) {
            throw new RequestException("There has been a new update for the client resource with id: "+ optionalClientEntity.get().getClientId()+", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        for (PurchaseEntity purchaseEntity : optionalClientEntity.get().getPurchases()) {
            if (purchaseEntity.getIsPaid() == 0) {
                throw new RequestException("Cancel your orders before you delete your account", HttpStatus.CONFLICT);
            }
        }

        try {
            ClientEntity clientEntity = optionalClientEntity.get();
            clientRepository.delete(clientEntity);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side. Try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}