package vkb.controller.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * sends a list of API warning
 */


@Getter
@Setter
@EqualsAndHashCode(callSuper = true)

final class AppApiWarnings extends Model implements Serializable {
    
    private static final long serialVersionUID = -8886037717240201799L;
    
    private int warningCount;
    private List<AppApiWarning> apiWarningList = new ArrayList<>();
    
    AppApiWarnings() {
    }
    
    AppApiWarnings(int warningCount, List<AppApiWarning> apiWarningList) {
        this.warningCount = warningCount;
        this.apiWarningList = apiWarningList;
    }
}
