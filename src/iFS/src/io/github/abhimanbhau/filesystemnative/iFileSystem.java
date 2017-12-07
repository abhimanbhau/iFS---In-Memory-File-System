//Author: Abhiman S. Kolte

package io.github.abhimanbhau.filesystemnative;

import com.sun.istack.internal.NotNull;
import io.github.abhimanbhau.constants.Configuration;
import io.github.abhimanbhau.constants.GlobalConstants;
import io.github.abhimanbhau.exception.*;
import io.github.abhimanbhau.logging.Logger;
import io.github.abhimanbhau.utils.CompressionProvider;
import io.github.abhimanbhau.utils.EncryptionProvider;
import io.github.abhimanbhau.utils.NativeHelperUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

public class iFileSystem {
    private byte[] _fileSystemBuffer;
    private TreeSet<Integer> freeInodes = new TreeSet<Integer>();
    private Logger logger = Logger.getInstance();
    private Configuration currentConfig;
    private TreeSet<File> _fileSystem = new TreeSet<File>();
    private TreeSet<Integer> _freeBlocks = new TreeSet<>();

    public iFileSystem(@NotNull Configuration config) {
        this.currentConfig = config;
        try {
            setupFileSystem(config.getNativeFilepath());
        } catch (IOException e) {
            logger.LogError(e.getMessage());
        }
    }

    private void setupInodes() {
        for (int i = 0; i < (currentConfig.getSize() * GlobalConstants.twoPowerTen / GlobalConstants.fileBlockSize);
             ++i) {
            freeInodes.add(i);
        }
    }

    private void initStorageSpaceBuffer() {
        _fileSystemBuffer = new byte[currentConfig.getSize() * GlobalConstants.twoPowerTen
                * GlobalConstants.twoPowerTen];
    }

    private void setupFreeFileBlocks() {
        for (int i = 0; i < (currentConfig.getSize() * GlobalConstants.twoPowerTen / GlobalConstants.fileBlockSize);
             ++i) {
            _freeBlocks.add(i);
        }
    }

    private void makeFileSystem() {
        setupInodes();
        setupFreeFileBlocks();
        logger.LogDebug("Setup Inodes and Fileblocks done");
        initStorageSpaceBuffer();
        logger.LogDebug("Setup storage area done");
    }

    private void openExistingFileSystem(String path) throws IOException {
        byte[] _fileSystemTempBuffer = NativeHelper.readFileSystemFromNativeFileSystem(path);
        throw new NotImplementedException();
    }

    public void finishFileSystem() {
        try {
            NativeHelper.writeFileSystemToNativeFileSystem(_fileSystemBuffer, currentConfig.getNativeFilepath());

        } catch (Exception e) {
            logger.LogError(e.getMessage());
        }
    }

    private void setupFileSystem(String path) throws IOException {
        if (NativeHelperUtils.fileExists(path)) {
            openExistingFileSystem(path);
            logger.LogError(" Not implemented");
        } else {
            logger.LogDebug("Creating new filesystem");
            makeFileSystem();
        }
    }

    public void createFile(byte[] _buffer, String path) throws IOException, iFSDiskFullException {
        int noOfFileBlocks;
        byte[] _compressedBuffer = CompressionProvider.CompressByteArray(_buffer);
        float blocks = _compressedBuffer.length / (GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen);
        noOfFileBlocks = (int) Math.ceil(blocks);
        if (noOfFileBlocks == 0)
            noOfFileBlocks += 1;
        if (noOfFileBlocks > _freeBlocks.size()) {
            throw new io.github.abhimanbhau.exception.iFSDiskFullException(" DISK SPACE FULL");
        }
        // Compress, Add the buffer to the file system buffer, setup iNodes and stuff

        int[] fileBlocks = new int[noOfFileBlocks];
        for (int i = 0; i < noOfFileBlocks; ++i) {
            fileBlocks[i] = _freeBlocks.first();
            _freeBlocks.remove(_freeBlocks.first());
        }
        for (int i = 0; i < noOfFileBlocks; ++i) {
            for (int j = 0; j < GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen; ++j) {
                if (j >= _compressedBuffer.length) {
                    _fileSystemBuffer[(fileBlocks[i] * GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen) + j]
                            = ((byte) 0);
                    continue;
                }
                _fileSystemBuffer[(fileBlocks[i] * GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen) + j]
                        = _compressedBuffer[(i * GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen) + j];
            }
        }

        int iNode = freeInodes.first();
        freeInodes.remove(freeInodes.first());
        File newFile = new File();
        newFile._iNode = iNode;
        newFile._fileSize = _compressedBuffer.length;
        for (int i = 0; i < noOfFileBlocks; ++i) {
            newFile._fileAllocationTable.add(fileBlocks[i]);
        }
        newFile._fileName = path;
        newFile._internalPath = path;
        newFile._createdTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newFile._lastAccessedTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newFile._modifiedTimeStamp = NativeHelperUtils.getDateOrTime(false);
        newFile.md5 = EncryptionProvider.getMD5(_compressedBuffer);
        _fileSystem.add(newFile);
    }

    public byte[] readFile(String path) throws IOException, DataFormatException {
        byte[] file;
        File f = doesFileExist(path);
        Iterator iterator = f._fileAllocationTable.iterator();
        int fileSize = f._fileSize;
        file = new byte[fileSize];
        int seekPointer = 0;
        int bytesRead = 0;
        while (iterator.hasNext()) {
            int currentBlock = (int) iterator.next();
            for (int i = 0; i < GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen; ++i) {
                if(bytesRead >= f._fileSize)
                {
                    continue;
                }
                file[seekPointer + i] = _fileSystemBuffer[currentBlock *
                        (GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen) + i];
                bytesRead++;
            }
            seekPointer = seekPointer + (GlobalConstants.fileBlockSize * GlobalConstants.twoPowerTen);
        }
        file = CompressionProvider.DecompressByteArray(file);
        return file;
    }

    public void createDirectory(String dirName, String path) {
        if (dirName.length() > currentConfig.getMaxDirectoryName()) {
            dirName = dirName.substring(0, currentConfig.getMaxDirectoryName() - 1);
        }
        File newDirectory = new File();
        newDirectory._iNode = freeInodes.first();
        freeInodes.remove(freeInodes.first());
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
        if (fileToDelete == null) {
            logger.LogError(" File not found: " + path);
            return;
        }
        int iNode = fileToDelete._iNode;
        freeUsedFileBlocks(fileToDelete);
        if (fileToDelete != null) {
            _fileSystem.remove(fileToDelete);
        }
        freeInodes.add(iNode);
    }

    public void deleteDirectory(String path) throws iFSDirectoryNotEmptyException, iFSFileNotFoundException {
        // @TODO Delete everything with that directoryname in path

        //path = path.substring(path.lastIndexOf('/'), path.length() - 1);

        // Does not work at this point
        // deleteAllFilesInGivenDirectory(path);

        // Get subfile count
        if(getSubFileCount(path) > 1)
        {
            throw new iFSDirectoryNotEmptyException("Directory Not Empty");
        }

        File dirToDelete = removeFileEntryFromTable(path);
        if(dirToDelete == null)
        {
            logger.LogError("Directory not found");
            throw new iFSFileNotFoundException("Directory not found");
        }
        int iNode = dirToDelete._iNode;
        if (dirToDelete != null) {
            _fileSystem.remove(dirToDelete);
        }
        freeInodes.add(iNode);
    }

//    private void deleteAllFilesInGivenDirectory(String path) {
//        for (File f : _fileSystem) {
//            if (f._internalPath.startsWith(path)) {
//                if (f._fileSize == -1)
//                    deleteDirectory(f._internalPath);
//                else
//                    deleteFile(f._internalPath);
//            }
//        }
//    }

    private File removeFileEntryFromTable(String path) {
        File del = null;
        for (File name : _fileSystem) {
            if (name._internalPath.equals(path)) {
                del = name;
            }
        }
        return del;
    }

    private void freeUsedFileBlocks(File f) {
        Iterator iterator = f._fileAllocationTable.iterator();
        while (iterator.hasNext()) {
            _freeBlocks.add((Integer) iterator.next());
        }
    }

    private File doesFileExist(String path) {
        File file = null;
        for (File f : _fileSystem) {
            if (f._internalPath.equals(path)) {
                file = f;
            }
        }
        return file;
    }

    public String[] listAllFiles() {
        String[] files = new String[_fileSystem.size()];
        int i = 0;
        for (File f : _fileSystem) {
            if (f._fileSize == -1)
                continue;
            files[i] = f._fileName;
            i++;
        }
        return files;
    }

    public ArrayList<String> listAllDirectories() {
        ArrayList<String> files = new ArrayList<>();
        int i = 0;
        for (File f : _fileSystem) {
            if (f._fileSize == -1) {
                files.add(f._fileName);
                i++;
            }
        }
        return files;
    }

    private int getSubFileCount(String path)
    {
        int counter = 0;
        for(File f : _fileSystem)
        {
            if(f._internalPath.startsWith(path))
            {
                counter++;
            }
        }
        return counter;
    }
}
