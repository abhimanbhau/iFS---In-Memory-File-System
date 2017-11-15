package io.github.abhimanbhau.filesystemnative;

import io.github.abhimanbhau.constants.Configuration;

public class iFileSystem {
    Configuration currentConfig;


    public iFileSystem()
    {
        currentConfig = new Configuration();
    }

    public iFileSystem(Configuration config)
    {
        this.currentConfig = config;
    }
}
