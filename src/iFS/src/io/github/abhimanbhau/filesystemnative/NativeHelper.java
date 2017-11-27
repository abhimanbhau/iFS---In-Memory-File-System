//Author: Abhiman S. Kolte

package io.github.abhimanbhau.filesystemnative;

import io.github.abhimanbhau.logging.Logger;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.io.File;

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

        // FileUtils.writeByteArrayToFile(new File(path), _buffer);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(_buffer);
            out.flush();
            byte[] bytesToWrite = bos.toByteArray();
            FileUtils.writeByteArrayToFile(new File(path), bytesToWrite);
        } catch (IOException e) {
            Logger.getInstance().LogError(e.getMessage());
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }

    }
}