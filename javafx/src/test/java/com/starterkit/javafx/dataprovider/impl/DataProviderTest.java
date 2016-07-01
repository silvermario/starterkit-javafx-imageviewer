package com.starterkit.javafx.dataprovider.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.starterkit.javafx.dataprovider.DataProvider;

public class DataProviderTest {

	DataProvider dataProvider;
	
	@Before
	public void setUp() throws Exception {
		
	dataProvider = DataProvider.INSTANCE;
	}

	@Test
	public void test() throws IOException {
		
		
		List<File> files = (List<File>) dataProvider.getFiles("C:\\projects\\starterkit\\javafx-imageviewer\\javafx\\images");
		assertEquals(8, files.size());
		//assertEquals(8, files.get(0));
	}

}
