package vkb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class VkbBackend {

    public static void main(String[] args) {
        SpringApplication.run(VkbBackend.class, args);
    }




}
