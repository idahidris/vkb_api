package vkb.config.i18n;

import vkb.config.AppPropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 * this class uses the accept header locale
 */

@Slf4j
@Component
public class SmartLocaleResolver extends AcceptHeaderLocaleResolver {
    
    private final AppPropertyConfig appPropertyConfig;
    
    public SmartLocaleResolver(AppPropertyConfig appPropertyConfig) {
        this.appPropertyConfig = appPropertyConfig;
    }
    
    
}
