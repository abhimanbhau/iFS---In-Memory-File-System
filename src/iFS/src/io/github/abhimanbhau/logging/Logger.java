//Author: Abhiman S. Kolte

package io.github.abhimanbhau.logging;

import io.github.abhimanbhau.constants.GlobalConstants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private FileWriter _writer;
    private BufferedWriter _buffer;

    public Logger(String path) throws IOException {
        _buffer = new BufferedWriter(new FileWriter(path));
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
