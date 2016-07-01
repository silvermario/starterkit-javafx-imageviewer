package com.starterkit.javafx.dataprovider.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.starterkit.javafx.dataprovider.DataProvider;
import com.starterkit.javafx.dataprovider.data.FileVO;

/**
 * Provides data. Data is stored locally in this object. Additionally a call
 * delay is simulated.
 *
 * @author Leszek
 */
public class DataProviderImpl implements DataProvider {

	private static final Logger LOG = Logger.getLogger(DataProviderImpl.class);
	
	private final Collection<String> imageDataTypes = Arrays.asList("jpg", "png", "gif"); 

	@Override
	public Collection<FileVO> getFilesVO(String directoryName) throws IOException {
		
		Collection<FileVO> filesVO = new ArrayList<>();
		Collection<File> files = getFiles(directoryName);
		
		for(File file: files){
			filesVO.add(new FileVO(file.getName(), directoryName));
		}
		
		return filesVO;
	}
	
	public List<File> getFiles(String directoryName) throws IOException {
		File directory = new File(directoryName);
				
		FilenameFilter extensionFilter = new FilenameFilter() {
			
		    public boolean accept(File dir, String name) {
		    	for(String ext: imageDataTypes){
		    		if(name.toLowerCase().endsWith(ext)){
		    			return true;
		    		}
		    	}
		    	return false; 
		    }
		};
        
		File[] files = directory.listFiles(extensionFilter);

		return Arrays.asList(files).stream().filter(File::isFile).collect(Collectors.toList());
	}
}
