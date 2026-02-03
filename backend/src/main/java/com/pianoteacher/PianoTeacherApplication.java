package com.pianoteacher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PianoTeacherApplication {

    public static void main(String[] args) {
        SpringApplication.run(PianoTeacherApplication.class, args);
    }

}
