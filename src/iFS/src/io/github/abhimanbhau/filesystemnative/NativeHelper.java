//Author: Abhiman S. Kolte

package io.github.abhimanbhau.filesystemnative;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class NativeHelper {
    public static byte[] readFileSystemFromNativeFileSystem(String path) throws IOException {
        File f = new File(path);
        FileInputStream fis = new FileInputStream(f);
        byte[] _filebuffer = new byte[(int) f.length()];
        fis.read(_filebuffer);
        return _filebuffer;
    }

    public static void writeFileSystemToNativeFileSystem(byte[] _buffer, String path) throws IOException {
        FileUtils.writeByteArrayToFile(new File(path), _buffer);
    }
}