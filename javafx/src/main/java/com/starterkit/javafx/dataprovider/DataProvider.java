package com.starterkit.javafx.dataprovider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.starterkit.javafx.dataprovider.data.FileVO;
import com.starterkit.javafx.dataprovider.impl.DataProviderImpl;

/**
 * Provides data.
 *
 * @author Leszek
 */
public interface DataProvider {

	/**
	 * Instance of this interface.
	 */
	DataProvider INSTANCE = new DataProviderImpl();

	
	Collection<File> getFiles(String directoryName) throws IOException;


	Collection<FileVO> getFilesVO(String directoryName) throws IOException;


}
