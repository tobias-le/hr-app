package cz.cvut.fel.pm2.timely_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimelyBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimelyBeApplication.class, args);
    }

}
