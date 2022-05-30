package at.ac.tuwien.sepm.groupphase.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
@EntityScan(basePackages = {"at.ac.tuwien.sepm.groupphase.backend.entity"})
@EnableJpaRepositories(basePackages = {"at.ac.tuwien.sepm.groupphase.backend.repository"})
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
