package io.github.abhimanbhau.filesystemnative;

import java.util.LinkedList;

class File {
    int _iNode;
    LinkedList<Integer> _fileAllocationTable;
    String _fileName;
    String _internalPath;
    int _fileSize;

    boolean _isDirectory;
    String _lastAccessedTimeStamp;
    String _createdTimeStamp;
    String _modifiedTimeStamp;
}