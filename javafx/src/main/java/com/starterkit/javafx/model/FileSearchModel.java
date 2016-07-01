package com.starterkit.javafx.model;

import java.util.ArrayList;
import java.util.List;

import com.starterkit.javafx.dataprovider.data.FileVO;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

/**
 * Data displayed on the person search screen.
 *
 * @author Leszek
 */
public class FileSearchModel {

	private final StringProperty directory = new SimpleStringProperty();
	private final ListProperty<FileVO> imageList = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public final String getDirectory() {
		return directory.get();
	}
	
	public final void setDirectory(String value) {
		directory.set(value);
	}

	public final void setName(String value) {
		directory.set(value);
	}

	public StringProperty directoryProperty() {
		return directory;
	}

	public final List<FileVO> getImageList() {
		return imageList.get();
	}

	public final void setImageList(List<FileVO> value) {
		imageList.setAll(value);
	}

	public ListProperty<FileVO> imageListProperty() {
		return imageList;
	}

	

}
