package com.dew.io;

import com.dew.util.ByteFormatted;

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
