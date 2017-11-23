//Author: Abhiman S. Kolte

package io.github.abhimanbhau.filesystemnative;

import io.github.abhimanbhau.logging.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class NativeHelper {
    public static byte[] readFileSystemFromNativeFileSystem(String path) {
        File f = new File(path);
        byte[] _filebuffer = new byte[(int) f.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            fis.read(_filebuffer);
        } catch (Exception e) {
            Logger.getInstance().LogError(e.getMessage());
        }
        return _filebuffer;
    }

    public static void writeFileSystemToNativeFileSystem(byte[] _buffer, String path) {
        try {
            FileUtils.writeByteArrayToFile(new File(path), _buffer);
        } catch (IOException e) {
            Logger.getInstance().LogError(e.getMessage());
        }
    }
}