package io.github.abhimanbhau.exception;

import io.github.abhimanbhau.constants.GlobalConstants;

public class iFSGenericException extends Exception {
    public iFSGenericException() {
        super();
    }

    public iFSGenericException(String message) {
        super(GlobalConstants.versionCode + message);
    }

    public iFSGenericException(String message, Throwable cause) {
        super(GlobalConstants.versionCode + message, cause);
    }

    public iFSGenericException(Throwable cause) {
        super(cause);
    }
}