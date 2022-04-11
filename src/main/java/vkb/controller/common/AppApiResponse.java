package vkb.controller.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;

/**
 * defines the particular fields to be described,
 * when an api is called
 */


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppApiResponse extends Model implements Serializable {

    private static final long serialVersionUID = 6393087651500056970L;

    @Builder.Default
    private boolean requestSuccessful = false;

    private AppApiSuccess apiSuccess;

    @Builder.Default
    private double executionTime = 0;
    private AppApiErrors apiErrors;
    private AppApiWarnings apiWarnings;

    @JsonSerialize
    private Object responseBody;
}

