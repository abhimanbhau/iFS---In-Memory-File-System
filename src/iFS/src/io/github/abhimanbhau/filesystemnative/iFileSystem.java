//Author: Abhiman S. Kolte

package io.github.abhimanbhau.filesystemnative;

import io.github.abhimanbhau.constants.Configuration;
import io.github.abhimanbhau.constants.GlobalConstants;
import io.github.abhimanbhau.utils.CompressionProvider;
import io.github.abhimanbhau.utils.NativeHelperUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class iFileSystem {

    Configuration currentConfig;
    TreeSet<File> _fileSystem = new TreeSet<File>();
    TreeSet<Integer> _freeBlocks = new TreeSet<>();

    byte[] _fileSystemBuffer;

    public iFileSystem() {
        currentConfig = new Configuration();
        setupInodes();
    }

    public iFileSystem(Configuration config) {
        this.currentConfig = config;
        setupInodes();
    }

    public void setupInodes() {
        for (int i = 0; i < (currentConfig.getSize() * GlobalConstants.twoPowerTen / GlobalConstants.fileBlockSize);
             ++i) {
            currentConfig.freeInodes.add(i);
        }
    }

    public void setupFreeFileBlocks() {
        for (int i = 0; i < (currentConfig.getSize() * GlobalConstants.twoPowerTen / GlobalConstants.fileBlockSize);
             ++i) {
            _freeBlocks.add(i);
        }
    }

    public void makeFileSystem() {

    }

    public void openFileSystem() {

    }

    public void createFile(byte[] _buffer, String path) throws IOException {
        // Compress, Add the buffer to the file system buffer, setup iNodes and stuff

        byte[] _compressedBuffer = CompressionProvider.CompressByteArray(_buffer);
        int noOfFileBlocks;
        float blocks = _compressedBuffer.length / (GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen);
        noOfFileBlocks = (int) Math.ceil(blocks);

        int[] fileBlocks = new int[noOfFileBlocks];
        for (int i = 0; i < noOfFileBlocks; ++i) {
            fileBlocks[i] = _freeBlocks.first();
            _freeBlocks.remove(_freeBlocks.first());
        }
        for (int i = 0; i < noOfFileBlocks; ++i) {
            for (int j = 0; j < GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen; ++j) {
                _fileSystemBuffer[fileBlocks[i] + j] = _compressedBuffer[i + j];
            }
        }

        int iNode = currentConfig.freeInodes.first();
        currentConfig.freeInodes.remove(currentConfig.freeInodes.first());
        File newFile = new File();
        newFile._iNode = iNode;
        newFile._fileSize = _compressedBuffer.length;
        // newDirectory._fileAllocationTable = null;

        for (int i = 0; i < noOfFileBlocks; ++i) {
            newFile._fileAllocationTable.add(fileBlocks[i]);
        }

        newFile._internalPath = path;
        newFile._createdTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newFile._lastAccessedTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newFile._modifiedTimeStamp = NativeHelperUtils.getDateOrTime(false);
        _fileSystem.add(newFile);
    }

    public byte[] readFile(String path) {
        byte[] file;
        File f = doesFileExist(path);
        Iterator iterator = f._fileAllocationTable.iterator();
        int fileSize = f._fileSize;
        file = new byte[fileSize];
        int seekPointer = 0;
        int currentBytes = 0;
        while (iterator.hasNext()) {
            int currentBlock = (int) iterator.next();
            for (int i = 0; i < GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen; ++i) {
                file[seekPointer + i] = _fileSystemBuffer[currentBlock + i];
                currentBytes++;
            }
            seekPointer = currentBytes;
            currentBytes = 0;
        }

        // @ TODO:
        // Decompress and read


        return file;
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

    public File doesFileExist(String path) {
        File file = null;
        for (File f : _fileSystem) {
            if (f._internalPath.equals(path)) {
                file = f;
            }
        }
        return file;
    }
}