package pl.kamil.zielinski.bookcentrerest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "pl.kamil.zielinski.bookcentrerest")
@EntityScan(basePackages = "pl.kamil.zielinski.bookcentrerest")
public class BookCentreRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookCentreRestApplication.class, args);
    }



}
