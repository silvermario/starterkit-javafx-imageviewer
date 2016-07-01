package com.starterkit.javafx.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.starterkit.javafx.dataprovider.DataProvider;
import com.starterkit.javafx.dataprovider.data.FileVO;
import com.starterkit.javafx.model.FileSearchModel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

/**
 * Controller for the person search screen.
 * <p>
 * The JavaFX runtime will inject corresponding objects in the @FXML annotated
 * fields. The @FXML annotated methods will be called by JavaFX runtime at
 * specific points in time.
 * </p>
 *
 * @author MAKOPEC
 */
public class ImageViewerController {

	private static final Logger LOG = Logger.getLogger(ImageViewerController.class);

	private static final Image SPINNER_IMAGE = new Image("/com/starterkit/javafx/img/loading_spinner.gif");

	/**
	 * Resource bundle loaded with this controller. JavaFX injects a resource
	 * bundle specified in {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code resources}.
	 * </p>
	 */
	@FXML
	private ResourceBundle resources;

	/**
	 * URL of the loaded FXML file. JavaFX injects an URL specified in
	 * {@link FXMLLoader#load(URL, ResourceBundle)} call.
	 * <p>
	 * NOTE: The variable name must be {@code location}.
	 * </p>
	 */
	@FXML
	private URL location;

	@FXML
	private ScrollPane scrollPane;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button searchButton;

	@FXML
	private ImageView imageView;

	@FXML
	private ListView<FileVO> imageList;

	@FXML
	private Button chooseDirectoryButton;

	@FXML
	private Button slideshowStartButton;

	@FXML
	private Label pathLabel;

	@FXML
	private Tooltip pathTooltip;

	private final DataProvider dataProvider = DataProvider.INSTANCE;

	private final FileSearchModel model = new FileSearchModel();

	private Timer timer;
	
	private Boolean startButtonSwitchedOn = false;


	public ImageViewerController() {
	}

	@FXML
	private void initialize() {

		pathLabel.textProperty().bind(model.directoryProperty());
		pathTooltip.textProperty().bind(model.directoryProperty());

		imageList.itemsProperty().bind(model.imageListProperty());

		initializeResultTable();

		/*
		 * Bind width and height of the imageView node parent container to
		 * scrollPane width and height
		 */
		anchorPane.prefHeightProperty().bind(scrollPane.heightProperty());
		anchorPane.prefWidthProperty().bind(scrollPane.widthProperty());

	}

	private void initializeResultTable() {

		imageList.setCellFactory(cell -> {
			return new ListCell<FileVO>() {

				@Override
				protected void updateItem(FileVO fileItem, boolean empty) {

					super.updateItem(fileItem, empty);

					if (empty) {
						setText(null);
						setStyle("");
					} else {
						setText(fileItem.getFileName());
					}
				}
			};
		});

		imageList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FileVO>() {

			@Override
			public void changed(ObservableValue<? extends FileVO> observable, FileVO oldValue, FileVO newValue) {
				LOG.debug(newValue + " selected");

				displayImage(newValue.getFullFilePath());
			}
		});

	}

	@FXML
	public void chooseImageDirectory(ActionEvent event) {
		DirectoryChooser directoryChooser = new DirectoryChooser();

		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File outputFolder = directoryChooser.showDialog(scrollPane.getScene().getWindow());

		if (outputFolder != null) {
			String selectedDirectory = outputFolder.getAbsolutePath();
			
			getImages(selectedDirectory);
		}
	}

	private void getImages(String selectedDirectory) {

		Task<Collection<FileVO>> backgroundTask = new Task<Collection<FileVO>>() {

			@Override
			protected Collection<FileVO> call() throws Exception {
				LOG.debug("call() in getImages() called");

				return dataProvider.getFilesVO(selectedDirectory);

			}

			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");

				Collection<FileVO> result = getValue();

				if (result == null || result.isEmpty()) {
					showMessageBox(resources.getString("msgbox.msg.folderempty"),
							resources.getString("msgbox.title.error"));

				} else {
					model.setImageList(new ArrayList<FileVO>(result));
					model.directoryProperty().set(selectedDirectory);
					// imageList.setSelection;
				}
			}

			@Override
			protected void failed() {
				showMessageBox(resources.getString("msgbox.msg.fileserror"), resources.getString("msgbox.title.error"));
			}
		};
		new Thread(backgroundTask).start();
	}

	private void showMessageBox(String message, String title) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void displayImage(String fullFilePath) {
		Task<Image> backgroundTask = new Task<Image>() {

			@Override
			protected Image call() throws Exception {
				LOG.debug("call() in displayImage called");

				imageView.setImage(SPINNER_IMAGE);

				Image image = new Image(new File(fullFilePath).toURI().toString());

				return image;
			}

			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");

				Image image = getValue();

				if (image != null) {
					imageView.setImage(image);
				}
			}

			@Override
			protected void failed() {

				showMessageBox(resources.getString("msgbox.msg.imageerror"), resources.getString("msgbox.title.error"));
				imageView.setImage(null);
			}
		};
		new Thread(backgroundTask).start();
	}

	@FXML
	private void startSlideshowAction(ActionEvent event) {
		LOG.debug(model.getImageList().isEmpty());

		if (!model.getImageList().isEmpty()) {
			if(!startButtonSwitchedOn) {
				slideShow();
				slideshowStartButton.setText(resources.getString("button.stop"));
				startButtonSwitchedOn = true;
			} else {
				timer.cancel();
				slideshowStartButton.setText(resources.getString("button.stop"));
				startButtonSwitchedOn = false;
			}
		}
	}	

	private void slideShow() {

		long delay = 3000;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				if (imageList.getSelectionModel().getSelectedIndex() == imageList.getItems().size() - 1) {
					imageList.getSelectionModel().selectFirst();
				} else {
					imageList.getSelectionModel().selectNext();
				}
			}
		}, delay, delay);
	}
}
