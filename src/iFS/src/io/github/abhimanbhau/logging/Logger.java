//Author: Abhiman S. Kolte

package io.github.abhimanbhau.logging;

import io.github.abhimanbhau.constants.GlobalConstants;
import io.github.abhimanbhau.utils.NativeHelperUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

    // Implementing Singleton Design Pattern
    private static Logger _instance = null;

    private FileWriter _writer;
    private BufferedWriter _buffer;

    private String path;

    private Logger() throws IOException {
        path = NativeHelperUtils.getUserHomeDirectory() + GlobalConstants.pathEscape + GlobalConstants.libName +
                NativeHelperUtils.getDateOrTime(true) + NativeHelperUtils.getDateOrTime(false) + GlobalConstants.logFileExtension;
        _buffer = new BufferedWriter(new FileWriter(path));
    }

    public static Logger getInstance() throws IOException {
        if (_instance == null) {
            _instance = new Logger();
        }
        return _instance;
    }

    public void Finish() throws IOException {
        _buffer.flush();
        _buffer.close();
    }

    public void LogError(String error) throws IOException {
        _buffer.write(GlobalConstants.ErrorString + error);
    }

    public void LogDebug(String debug) throws IOException {
        _buffer.write(GlobalConstants.DebugString + debug);
    }
}
