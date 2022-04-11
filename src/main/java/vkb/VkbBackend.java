package vkb;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class VkbBackend {

    public static void main(String[] args) {
        SpringApplication.run(VkbBackend.class, args);
    }


    @Bean
    public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
                                 @Value("${application-version}") String appVersion,
                                 @Value("${application-title}")String appTitle,
                                 @Value("${terms-of-service}")String termsOfService,
                                 @Value("${license-name}")String licenseName,
                                 @Value("${license-url}")String licenseUrl,
                                 @Value("${company-email}")String companyEmail,
                                 @Value("${company-url}")String companyUrl,
                                 @Value("${company-name}")String companyName
                                 ) {

        return new OpenAPI()
                        .info(new Info()
                        .title(appTitle)
                        .version(appVersion)
                        .contact(new Contact().email(companyEmail).name(companyName).url(companyUrl))
                        .description(appDesciption)
                        .termsOfService(termsOfService)
                        .license(new License().name(licenseName).url(licenseUrl)));
    }



}
