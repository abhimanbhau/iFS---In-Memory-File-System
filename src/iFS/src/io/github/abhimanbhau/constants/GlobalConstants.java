package io.github.abhimanbhau.constants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class GlobalConstants {
    public static String DebugString = "{iFS} Debug: ";
    public static String ErrorString = "{iFS} Error: ";

    public static String defaultUsername = "root";

    public static String getDateOrTime(boolean date)
    {
        if(date) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yy-mm-dd");
            return timeFormatter.format(LocalDate.now());
        }
        else
        {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");
            return timeFormatter.format(LocalDateTime.now());
        }
    }
}
