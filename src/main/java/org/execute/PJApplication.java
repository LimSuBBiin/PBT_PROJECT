package org.execute;


import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PJApplication {
    public static void main(String[] args) {

        SpringApplication.run(PJApplication.class, args);
    }


    @Bean
    Hibernate6Module hibernate5Module() {
        Hibernate6Module hibernate5Module = new Hibernate6Module();
        //강제 지연 로딩 설정
        hibernate5Module.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5Module;
    }
}