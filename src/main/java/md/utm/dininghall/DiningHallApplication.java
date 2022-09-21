package md.utm.dininghall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class DiningHallApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiningHallApplication.class, args);
    }

}
