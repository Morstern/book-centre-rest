package pl.kamil.zielinski.bookcentrerest.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import pl.kamil.zielinski.bookcentrerest.commons.HeaderBuilder;
import pl.kamil.zielinski.bookcentrerest.commons.RequestException;

import java.util.Optional;

@Service
@Slf4j
public class BookService {

    BookRepository bookRepository;

    @Autowired
    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<?>getAllBooks(Integer page, Integer size) throws RequestException {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 10;
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            return new ResponseEntity<>(bookRepository.findAll(pageable).get(), HttpStatus.OK);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getBookInfo(int id) throws RequestException {
        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);

        if (optionalBookEntity.isEmpty()) {
            throw new RequestException("Couldn't find a book with provided ID", HttpStatus.NOT_FOUND);
        }
        MultiValueMap<String,String> headers = new HeaderBuilder()
                .setETag(String.valueOf(optionalBookEntity.get().hashCode()))
                .build();

        return new ResponseEntity<>(optionalBookEntity.get(), headers, HttpStatus.OK);
    }


    public ResponseEntity<?> updateBook(int id, Integer hashcode, BookEntity bookEntity) throws RequestException {

        if(hashcode==null){
            throw new RequestException("Please add Match-If value in header", HttpStatus.PRECONDITION_REQUIRED);
        }

        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
        if (optionalBookEntity.isEmpty()) {
            throw new RequestException("Couldn't find a book with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalBookEntity.get().hashCode()!=hashcode) {
            throw new RequestException("There has been a new update for the book resource with id: "+ optionalBookEntity.get().getBookId()+", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        try {
            BookEntity updatedBookEntity = bookRepository.save(bookEntity);
            return new ResponseEntity<>(updatedBookEntity, HttpStatus.OK);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public ResponseEntity<?> updateBookAmount(int id, int hashcode, BookEntity updateBookEntity) throws RequestException {
        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
        if (optionalBookEntity.isEmpty()) {
            throw new RequestException("Couldn't find a book with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalBookEntity.get().hashCode()!=hashcode) {
            throw new RequestException("There has been a new update for the book resource with id: "+ optionalBookEntity.get().getBookId()+", please fetch newest versions, and retry your request", HttpStatus.PRECONDITION_FAILED);
        }

        try {
            BookEntity bookEntity = optionalBookEntity.get();
            bookEntity.setAmount(updateBookEntity.getAmount());
            return new ResponseEntity<>(bookRepository.save(bookEntity), HttpStatus.OK);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public void subtractAmountFromBookAmount(Integer id, Integer amount) throws RequestException {

        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
        if (optionalBookEntity.isEmpty()) {
            throw new RequestException("Couldn't find a book with provided ID", HttpStatus.NOT_FOUND);
        }

        if (optionalBookEntity.get().getAmount() < amount) {
            throw new RequestException("There aren't enough copies in our magazine to fulfill your order", HttpStatus.BAD_REQUEST);
        }

        try {
            BookEntity bookEntity = optionalBookEntity.get();
            bookEntity.setAmount(bookEntity.getAmount() - amount);
            bookRepository.save(bookEntity);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void addAmountToBookAmount(Integer id, Integer amount) throws RequestException {
        Optional<BookEntity> optionalBookEntity = bookRepository.findById(id);
        if (optionalBookEntity.isEmpty()) {
            throw new RequestException("Couldn't find a book with provided ID", HttpStatus.NOT_FOUND);
        }
        try {
            BookEntity bookEntity = optionalBookEntity.get();
            bookEntity.setAmount(bookEntity.getAmount() + amount);
            bookRepository.save(bookEntity);
        } catch (Exception e) {
            throw new RequestException("Something went bad on the server side", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
