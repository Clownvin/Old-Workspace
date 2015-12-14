package com.v2.io;

import com.v2.util.ByteFormatted;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface FileManagerWriteable extends
	ByteFormatted<FileManagerWriteable> {
    public String getFileName();

    public FileType getFileType();
}
