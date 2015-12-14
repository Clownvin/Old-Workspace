package com.ctp.io;

import com.ctp.util.ByteFormated;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface FileManagerWriteable <T extends FileManagerWriteable<?>> extends ByteFormated<FileManagerWriteable<?>> {
	public String getFileName();

	public FileType getFileType();
}
