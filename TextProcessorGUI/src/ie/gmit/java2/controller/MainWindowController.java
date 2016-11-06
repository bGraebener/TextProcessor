package ie.gmit.java2.controller;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import ie.gmit.java2.model.Handler;
import ie.gmit.java2.model.Parser.Source;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

//XXX comment
//DONE search method
//DONE delete method
//DONE Javadocs

/**
 * The controller class for the main view. It handles the button events and
 * checks if user input is present. If the required input is missing it prompts
 * for input. It passes the input the to Handler Class that performs all
 * operations. It then updates the view with the results retrieved from the Handler.
 * 
 * @author Basti
 *
 */
public class MainWindowController {

	@FXML
	private TextField fileTextField, urlTextField, searchTextField, deleteTextField;
	@FXML
	private Button browseButton, parseButton, showTextButton, clearButton;
	@FXML
	private Button searchButton, deleteButton;
	@FXML
	private CheckBox caseCheckBox, startsWithCheckBox, endsWithCheckBox;
	@FXML
	private TextArea textArea;

	private Alert alert;

	private Handler handler;

	@FXML
	private void initialize() {

		handler = new Handler(this);

		// set universal alert settings
		alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(null);

		// Automatically uncheck and disable case-sensitive option if one of
		// the two others is selected.
		startsWithCheckBox.selectedProperty().addListener((x) -> {
			if (caseCheckBox.isDisabled() && !endsWithCheckBox.isSelected()) {
				caseCheckBox.setDisable(false);

			} else {
				caseCheckBox.setSelected(false);
				caseCheckBox.setDisable(true);
			}
		});

		endsWithCheckBox.selectedProperty().addListener((x) -> {
			if (caseCheckBox.isDisabled() && !startsWithCheckBox.isSelected()) {
				caseCheckBox.setDisable(false);

			} else {
				caseCheckBox.setSelected(false);
				caseCheckBox.setDisable(true);
			}
		});
	}

	/**
	 * Method to browse for a file from the file system to use as a source for
	 * parsing the text.
	 * 
	 * @param event
	 */
	@FXML
	private void browseForFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		Optional<File> chosenOptional = Optional.ofNullable(fc.showOpenDialog(null));

		File chosenFile = chosenOptional.orElse(new File("res/test.txt"));

		fileTextField.setText(chosenFile.getAbsolutePath());
	}

	/**
	 * Event handler for the Browse button. Checks if a text source was
	 * specified and calls the appropriate Handler method with the provided
	 * source.
	 * 
	 * @param event
	 */
	@FXML
	private void parse(ActionEvent event) {

		if (fileTextField.getText().isEmpty() && urlTextField.getText().isEmpty()) {
			alert.setContentText("Choose a source!");
			alert.show();
		}

		else if (!fileTextField.getText().isEmpty()) {
			handler.parse(fileTextField.getText(), Source.FILE);

		} else if (!urlTextField.getText().isEmpty()) {
			handler.parse(urlTextField.getText(), Source.URL);
		}
	}

	/**
	 * Event handler for the Search button. Checks whether a search string was
	 * provided and passes it to the Handler getStats() method.
	 * 
	 * @param event
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@FXML
	private void search(ActionEvent event) throws InterruptedException, ExecutionException, TimeoutException {

		if (searchTextField.getText().isEmpty()) {
			alert.setContentText("No search string specified!");
			alert.show();
			return;
		}

		String userInput = searchTextField.getText();
		String statsResults = handler.getStats(userInput);

		textArea.setText(statsResults);
	}

	/**
	 * Event handler for the Delete button. Checks whether a delete string was
	 * provided and passes it to the Handler delete() method.
	 * 
	 * @param event
	 */
	@FXML
	private void delete(ActionEvent event) {

		if (deleteTextField.getText().isEmpty()) {
			alert.setContentText("Nothing to delete!");
			alert.show();
			return;
		}

		handler.delete(deleteTextField.getText());
	}
	
	/**
	 * Event handler for the clear window button.  
	 */
	@FXML
	private void clearTextArea(){		
		textArea.setText(handler.clearText());
	}
	
	/**
	 * Event handler for the show text button. Delegates to the Handlers
	 * showText() method.
	 */
	@FXML
	private void showText() {
		handler.showText();
	}

	public boolean caseIsSelected() {
		return caseCheckBox.isSelected();
	}

	public boolean startsIsSelected() {
		return startsWithCheckBox.isSelected();
	}

	public boolean endsIsSelected() {
		return endsWithCheckBox.isSelected();
	}

}
