package kr.co.broadwave.aci;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
public class AciApplication {
    public static void main(String[] args) {
        SpringApplication.run(AciApplication.class, args);
    }
}
