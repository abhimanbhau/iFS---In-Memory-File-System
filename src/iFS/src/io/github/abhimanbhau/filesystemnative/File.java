package io.github.abhimanbhau.filesystemnative;

import java.util.LinkedList;

class File implements Comparable<File> {
    int _iNode;
    LinkedList<Integer> _fileAllocationTable;
    String _fileName;
    String _internalPath;
    int _fileSize;

    String _lastAccessedTimeStamp;
    String _createdTimeStamp;
    String _modifiedTimeStamp;

    String md5;

    public File() {
        _fileAllocationTable = new LinkedList<>();
    }

    @Override
    public int compareTo(File o) {
        if (o == null)
            return 0;
        return this._internalPath.compareTo(o._internalPath);
    }
}