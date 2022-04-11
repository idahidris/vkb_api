package vkb.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;

@Data
public class PageDto {

    private static final int MIN_DATA_SIZE = 10;

    private static final int MAX_DATA_SIZE = 100;

    private static final int DEFAULT_DATA_SIZE = 100;

    private int pageNumber;

    private int totalElements;

    private int pageSize;

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    @DateTimeFormat(pattern = DATE_PATTERN)
    private Date from;

    @DateTimeFormat(pattern = DATE_PATTERN)
    private Date to;

    public int getPageSize() {
        if (pageSize <= 0)
            return MIN_DATA_SIZE;
        if( pageSize > MAX_DATA_SIZE)
            return MAX_DATA_SIZE;

        return pageSize;
    }

    public int getPageNumber() {
        if (pageNumber <= 0)
            return 0;

        int page = pageNumber;

        return page < 0 ? 0 : page;
    }

    public void setFrom(Date from) {
        if(null != from) {
            startOfDay(from);
        }
    }

    private void startOfDay(Date from) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(from);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND, 0);

        this.from = calendar.getTime();
    }

    public void setTo(Date to) {
        if( null != to) {
            endOfDay(to);
        }
    }

    private void endOfDay(Date to) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(to);
        cal.set(Calendar.HOUR, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        this.to = cal.getTime();
    }

    public boolean isDateEmpty() {
        return null == from || null == to || from.after(to);
    }


    public boolean isPageable(){
        return !(this.pageSize == 0 && this.pageNumber == 0);
    }


}
