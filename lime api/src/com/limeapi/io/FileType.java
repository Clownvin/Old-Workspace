package com.limeapi.io;

import java.util.ArrayList;

public final class FileType {
    private static final ArrayList<FileType> definedFileTypes = new ArrayList<FileType>();
    private final String fileExtension;
    private final String normalFileDirectory;
    
    public FileType(final String fileExtension, final String normalFileDirectory) {
	this.fileExtension = fileExtension;
	this.normalFileDirectory = normalFileDirectory;
	if (!definedFileTypes.contains(this))
	    definedFileTypes.add(this);
    }
    
    public String getFileExtension() {
	return fileExtension;
    }
    
    public String getNormalFileDirectory() {
	return normalFileDirectory;
    }
}
