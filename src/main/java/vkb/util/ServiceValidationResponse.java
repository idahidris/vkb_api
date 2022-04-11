package vkb.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : osatoojo
 * @email : oosato@nibss-plc.com.ng,
 * @date :: 06/05/2020
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceValidationResponse {
    private boolean valid;
    private String message;
}
