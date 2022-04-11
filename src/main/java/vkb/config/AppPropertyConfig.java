package vkb.config;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * This class defines config properties that are assigned in the application.properties file
 **/


@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties("app")
@Validated
public class AppPropertyConfig {
    
    private final I18n i18n = new I18n();

    @Getter
    @Setter
    @ToString
    public static class I18n {
        private List<String> supportedLocales;
    }

    
    
}
