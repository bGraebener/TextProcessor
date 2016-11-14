/*
	Created by Basti on 05.11.2016
*/

package ie.gmit.java2.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.BiPredicate;

import ie.gmit.java2.controller.MainWindowController;
import ie.gmit.java2.controller.TextViewController;
import ie.gmit.java2.model.processing.Processor;
import ie.gmit.java2.model.processing.TextAnalyser;
import ie.gmit.java2.model.processing.TextSearcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

//DONE implement stats method 
//DONE Comments

/**
 * Class that retrieves the results from the users query and passes it back to
 * the Controller class. Functions as a Bufferclass for the MainWindowController
 * and the model classes.
 * 
 * @author Basti
 *
 */
public class Handler {

	private List<String> text;
	private BiPredicate<String, String> caseSensitive, startsWith, endsWith, combined;
	private StringBuilder statsAsString;
	private Alert alert;

	private MainWindowController mwc;
	private Processor analyser, searcher;
	
	
	public Handler(MainWindowController mwc) {
		// get an instance of the controller to get access to the checkboxes and
		// set the BiPredicates accordingly to the users choice  
		this.mwc = mwc;
		statsAsString = new StringBuilder();

		// initialise BiPredicates
		startsWith = String::startsWith;
		endsWith = String::endsWith;

		// universal alert settings
		alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
	}

	/**
	 * Method that attempts to parse the text from the specified source by
	 * calling the appropriate method from the Parser class. Gets called by the
	 * parse event handler of the Controller.
	 * 
	 * @param source
	 *            the URL of the text source
	 */
	public void parse(URL source) {

		Parser urlParser = new Parser(source);
		text = urlParser.parse();

		alert.setContentText("Text parsed!");
		alert.show();
	}

	/**
	 * Method that attempts to parse the text from the specified source by
	 * calling the appropriate method from the Parser class. Gets called by the
	 * parse event handler of the Controller.
	 * 
	 * @param source
	 *            the text source File
	 */
	public void parse(File source) {

		Parser fileParser = new Parser(source);
		text = fileParser.parse();

		alert.setContentText("Text parsed!");
		alert.show();
	}

	/**
	 * Method that retrieves all data from the user search or delete query by
	 * calling the methods from the TextSearcher class.
	 * 
	 * @param userInput
	 *            The search String specified by the user.
	 * @return The result as a formatted String.
	 */
	public String searchForString(String userInput) {

		setOptions();

		if (!text.isEmpty() || text != null) {
			searcher = new TextSearcher(text);
			return searcher.process(userInput, combined);
		} else {
			return "No text found";
		}
	}

	/**
	 * Gathers statistical details from the TextSearcher class and builds a
	 * String for the Controller to set in the display.
	 * 
	 * @return The results of the stats operations as a String.
	 */
	public String getStats() {

		if (!text.isEmpty() || text != null) {
			analyser = new TextAnalyser(text);
			return analyser.process(null, combined);
		} else {
			return "No text found";
		}
	}

	/**
	 * Method to delete an element from the text. The user can specify an index
	 * or a String to be deleted. The method checks whether the input is an
	 * integer or not and calls the appropriate processor method.
	 * 
	 * @param userInput
	 *            Element to delete
	 */
	public void delete(String userInput) {

		TextSearcher searcher = new TextSearcher(text);
		boolean isInt = false;
		int index;
		int deletedCount;
		String deletedString;

		setOptions();

		// check if input is an integer
		for (int i = 0; i < userInput.length(); i++) {
			if (Character.isDigit(userInput.charAt(i))) {
				isInt = true;
			} else {
				isInt = false;
			}
		}

		// call appropriate delete method
		if (isInt) {
			index = Integer.parseInt(userInput);
			deletedString = searcher.delete(index);

			alert.setContentText("Deleted Item: " + deletedString);
			alert.show();

		} else {
			deletedCount = searcher.delete(userInput, combined);
			alert.setContentText("Number of deleted elements: " + deletedCount);
			alert.show();
		}
	}

	/**
	 * Method that opens the Text View Window. Gets called from the
	 * MainWindowController. Sets the text with the TextControllers setText()
	 * method.
	 */
	public void showText() {

		if (text == null || text.isEmpty()) {
			return;
		}
		
		TextAnalyser analyser;
		analyser = new TextAnalyser(text);

		// open the TextView Window
		try {
			FXMLLoader textViewLoader = new FXMLLoader(getClass().getResource("/ie/gmit/java2/view/TextView.fxml"));
			AnchorPane textViewPane = textViewLoader.load();

			TextViewController textViewController = textViewLoader.getController();
			textViewController.setText(text, analyser.count());

			Scene textViewScene = new Scene(textViewPane);

			Stage textViewStage = new Stage();
			textViewController.setStage(textViewStage);

			textViewStage.setScene(textViewScene);
			textViewStage.setResizable(false);
			textViewStage.setTitle("Text View");
			textViewStage.initModality(Modality.APPLICATION_MODAL);

			textViewStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String clearText() {
		statsAsString = new StringBuilder();
		return statsAsString.toString();
	}

	/**
	 * Sets the BiPredicats according to the settings chosen by the user. The
	 * case-sensitive option can only be chosen if the startsWith and endsWith
	 * options are disabled.
	 */
	private void setOptions() {

		caseSensitive = mwc.caseIsSelected() ? String::equals : String::equalsIgnoreCase;

		if (mwc.startsIsSelected()) {
			combined = startsWith;
			if (mwc.endsIsSelected()) {
				combined = combined.or(endsWith);
			}
		} else if (mwc.endsIsSelected()) {
			combined = endsWith;
		} else {
			combined = caseSensitive;
		}
	}

}
