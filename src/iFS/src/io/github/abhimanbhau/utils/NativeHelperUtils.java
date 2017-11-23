//Author: Abhiman S. Kolte

package io.github.abhimanbhau.utils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NativeHelperUtils {
    public static String getUserHomeDirectory() {
        return System.getProperty("user.home");
    }

    public static String getDateOrTime(boolean date) {
        if (date) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yy-mm-dd");
            return timeFormatter.format(LocalDate.now());
        } else {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yy-mm-dd-HH-mm-ss");
            return timeFormatter.format(LocalDateTime.now());
        }
    }

    public static boolean fileExists(String path) {
        File f = new File(path);
        if (f.exists() && !f.isDirectory())
            return true;
        else
            return false;
    }
}
