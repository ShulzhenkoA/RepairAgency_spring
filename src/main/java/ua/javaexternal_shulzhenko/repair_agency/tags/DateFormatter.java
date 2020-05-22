package ua.javaexternal_shulzhenko.repair_agency.tags;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateFormatter extends SimpleTagSupport {
    private LocalDateTime localDateTime;
    private String pattern;

    @Override
    public void doTag() throws IOException {
        String formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern(pattern));
        JspWriter writer = getJspContext().getOut();
        writer.print(formattedDateTime);
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
