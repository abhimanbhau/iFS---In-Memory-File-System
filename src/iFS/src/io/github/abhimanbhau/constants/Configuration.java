//Author: Abhiman S. Kolte

package io.github.abhimanbhau.constants;

import io.github.abhimanbhau.utils.NativeHelperUtils;

public class Configuration {
    int size;
    int maxDirectoryName;
    int maxFileName;
    String nativeFilepath;
    String ownerUsername;

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getNativeFilepath() {
        return nativeFilepath;
    }

    public int getSize() {
        return size;
    }

    public int getMaxDirectoryName() {
        return maxDirectoryName;
    }

    public int getMaxFileName() {
        return maxFileName;
    }

    public Configuration()
    {
        size = 256;
        maxDirectoryName = 16;
        maxFileName = 16;
        nativeFilepath = NativeHelperUtils.getUserHomeDirectory();
        ownerUsername = GlobalConstants.defaultUsername;
    }

    public Configuration(int size, int maxDirectoryName, int maxFileName, String path, String userName)
    {
        this.size = size;
        this.maxDirectoryName = maxDirectoryName;
        this.maxFileName = maxFileName;
        this.nativeFilepath = path;
        this.ownerUsername = userName;
    }
}
