package vkb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vkb.config.FileStorageProperties;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class VkbBackend {

    public static void main(String[] args) {
        SpringApplication.run(VkbBackend.class, args);
    }




}
