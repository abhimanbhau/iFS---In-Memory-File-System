//Author: Abhiman S. Kolte

package io.github.abhimanbhau.constants;

public class GlobalConstants {
    public static String libName = "iFS";
    public static String versionCode = "iFS v1.0";
    public static String logFileExtension = ".iFS.log";

    public static String DebugString = "{iFS} Debug: ";
    public static String ErrorString = "{iFS} Error: ";

    public static String defaultUsername = "root";
    public static String pathEscape = "/";

    public static int fileBlockSize = 4;
    public static int twoPowerTen = 1024;

    public static int magicSplitLength = 8;
    public static byte[] magicCode = {0, 0, 0, 0, 0, 0, 0, 0};

}