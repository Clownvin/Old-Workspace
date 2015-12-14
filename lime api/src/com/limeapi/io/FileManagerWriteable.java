package com.limeapi.io;

import com.limeapi.util.ByteFormatted;

public interface FileManagerWriteable extends ByteFormatted<FileManagerWriteable> {
    public FileType getFileType();
    
    public String getFileName();
}
