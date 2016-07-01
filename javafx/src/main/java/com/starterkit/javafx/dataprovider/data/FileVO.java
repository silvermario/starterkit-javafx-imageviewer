package com.starterkit.javafx.dataprovider.data;

import java.io.File;

/**
 * Person data.
 *
 * @author Leszek
 */
public class FileVO {

	private String fileName;
	private String directory;
	private String fullFilePath;
	
	public FileVO(String fileName, String directory) {
		super();
		this.fileName = fileName;
		this.directory = directory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFullFilePath() {
		return directory + File.separator + fileName;
	}

	@Override
	public String toString() {
		return "FileVO [fileName=" + fileName + ", directory=" + directory + ", getfullFilePath()=" + getFullFilePath() + "]";
	}
	
	


}
