package vkb.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageDto  implements Serializable {

    private static final long serialVersionUID = 5777870492698369073L;

    @NotBlank(message = "pageNumber is required")
    @Pattern(regexp ="^[0-9]+$", message = "invalid pageNumber pattern")
    private String pageNumber;

    @NotBlank(message = "pageSize is required")
    @Pattern(regexp ="^[0-9]+$", message = "invalid pageNumber pattern")
    private String pageSize;

    private String searchValue;


}
