//Author: Abhiman S. Kolte

package io.github.abhimanbhau.filesystemnative;

import io.github.abhimanbhau.constants.Configuration;
import io.github.abhimanbhau.utils.NativeHelperUtils;

import java.util.Iterator;
import java.util.TreeSet;

public class iFileSystem {

    Configuration currentConfig;
    TreeSet<File> _fileSystem = new TreeSet<File>();
    TreeSet<Integer> _freeBlocks = new TreeSet<>();

    public iFileSystem() {
        currentConfig = new Configuration();
        setupInodes();
    }

    public iFileSystem(Configuration config) {
        this.currentConfig = config;
        setupInodes();
    }

    public void setupInodes() {
        for (int i = 0; i < (currentConfig.getSize() * 1024 / 4); ++i) {
            currentConfig.freeInodes.add(i);
        }
    }

    public void makeFileSystem() {

        for (int i = 0; i < (currentConfig.getSize() * 1024 / 4); ++i) {
            _freeBlocks.add(i);
        }
    }

    public void openFileSystem() {

    }

    public void createFile() {

    }

    public void readFile() {

    }

    public void createDirectory(String dirName, String path) {
        if (dirName.length() > currentConfig.getMaxDirectoryName()) {
            dirName = dirName.substring(0, currentConfig.getMaxDirectoryName() - 1);
        }
        File newDirectory = new File();
        newDirectory._iNode = currentConfig.freeInodes.first();
        currentConfig.freeInodes.remove(currentConfig.freeInodes.first());
        newDirectory._fileName = dirName;
        newDirectory._fileSize = -1;
        newDirectory._fileAllocationTable = null;
        newDirectory._internalPath = path;
        newDirectory._createdTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newDirectory._lastAccessedTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newDirectory._modifiedTimeStamp = NativeHelperUtils.getDateOrTime(false);

        _fileSystem.add(newDirectory);
    }

    public void deleteFile(String path) {
        File fileToDelete = removeFileEntryFromTable(path);
        int iNode = fileToDelete._iNode;
        freeUsedFileBlocks(fileToDelete);
        if (fileToDelete != null) {
            _fileSystem.remove(fileToDelete);
        }
        currentConfig.freeInodes.add(iNode);
    }

    public void deleteDirectory(String path) {
        //path = path.substring(path.lastIndexOf('/'), path.length() - 1);
        File dirToDelete = removeFileEntryFromTable(path);
        int iNode = dirToDelete._iNode;
        if (dirToDelete != null) {
            _fileSystem.remove(dirToDelete);
        }
        currentConfig.freeInodes.add(iNode);
    }

    public File removeFileEntryFromTable(String path) {
        File del = null;
        for (File name : _fileSystem) {
            if (name._internalPath.equals(path)) {
                del = name;
            }
        }
        return del;
    }

    public void freeUsedFileBlocks(File f) {
        Iterator iterator = f._fileAllocationTable.iterator();
        while (iterator.hasNext()) {
            _freeBlocks.add((Integer) iterator.next());
        }
    }
}