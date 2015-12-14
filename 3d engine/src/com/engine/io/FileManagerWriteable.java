package com.engine.io;

import com.engine.util.ByteFormatted;

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
