/*
	Created by Basti on 05.11.2016
*/

package ie.gmit.java2.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import ie.gmit.java2.controller.MainWindowController;
import ie.gmit.java2.controller.TextViewController;
import ie.gmit.java2.model.parsing.FileParser;
import ie.gmit.java2.model.parsing.Parseable;
import ie.gmit.java2.model.parsing.UrlParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

//DONE implement stats method 
//TODO Comments

/**
 * Class that retrieves the results from the users query and passes it back to
 * the Controller class. Functions as a Bufferclass for the MainWindowController
 * and the model classes.
 * 
 * @author Basti
 *
 */
public class Handler {

	public static enum Source {
		URL, FILE, OTHER
	};

	private List<String> text;
	private BiPredicate<String, String> caseSensitive, startsWith, endsWith, combined;
	private MainWindowController mwc;
	private StringBuilder statsAsString;
	private TextProcessor processor;

	private Alert alert;

	public Handler(MainWindowController mwc) {
		this.mwc = mwc;
		statsAsString = new StringBuilder();

		startsWith = String::startsWith;
		endsWith = String::endsWith;

		alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);

	}

	/**
	 * Method that attempts to parse the text from the specified source by
	 * calling the appropriate method from the FileParser class. Gets called by
	 * the parse event handler of the Controller.
	 * 
	 * @param sourcePath
	 *            the path of the text source
	 * @param sourceType
	 *            the type of the source
	 */
	public void parse(URL source) {

		Parseable urlParser = new UrlParser(source);
		text = urlParser.parse();

		alert.setContentText("Text parsed!");
		alert.show();
	}

	public void parse(File source) {

		Parseable fileParser = new FileParser(source);
		text = fileParser.parse();

		alert.setContentText("Text parsed!");
		alert.show();
	}

	/**
	 * Method that retrieves all data from the user search or delete query by
	 * calling the methods from the TextProcessor class.
	 * 
	 * @param userInput
	 *            The search String specified by the user.
	 * @return The result as a formatted String.
	 */
	public String searchForString(String userInput) {

		setOptions();

		if (!text.isEmpty()) {
			processor = new TextProcessor(text);
		}

		boolean containsUserInput = processor.contains(userInput, combined);
		int firstIndexOf = processor.getFirstIndex(userInput, combined);
		int lastIndexOf = processor.getLastIndex(userInput, combined);
		int occurencesCount = processor.countOccurences(userInput, combined);
		int[] occurencesIndices = processor.getAllIndeces(userInput, combined);

		statsAsString.append("\nUser Input: " + userInput + "\n");
		statsAsString.append("Contains Input: " + containsUserInput + "\n");
		statsAsString.append("Num of Occurences: " + occurencesCount + "\n");
		statsAsString.append("First Index: " + firstIndexOf + "\n");
		statsAsString.append("Last Index: " + lastIndexOf + "\n");
		statsAsString.append("Indices of occurences: " + Arrays.toString(occurencesIndices) + "\n\n");

		return statsAsString.toString();
	}

	/**
	 * Gathers statistical details from the TextProcessor class and builds a
	 * String for the Controller to set in the display.
	 * 
	 * @return The results of the stats operations as a String.
	 */
	public String getStats() {

		if (!text.isEmpty()) {
			processor = new TextProcessor(text);
		}

		int elementsCount = processor.count();
		Map<Integer, String> longestWord = processor.longestWord();
		double averageWordLength = processor.averageWordLength();
		int numOfSentences = processor.countSentences();

		// skip mostUsedMethod if text size is greater than 10,000 because of
		// runtime
		String mostUsed = elementsCount > 10_000 ? "Most used word: Skipped due to large text size!\n"
				: processor.getMostUsedWord();

		int mostUsedAmount = processor.countOccurences(mostUsed, String::equalsIgnoreCase);

		longestWord.forEach(
				(k, v) -> statsAsString.append("\nLongest Word(s): \"" + v + "\" with " + k + " characters\n"));
		statsAsString.append(String.format("Average word length is: %.2f\n", averageWordLength));
		statsAsString.append("Number of sentences: " + numOfSentences + "\n");
		statsAsString.append("Total amount of elements: " + elementsCount + "\n");

		if (elementsCount > 10_000) {
			statsAsString.append(mostUsed);
		} else {
			statsAsString
					.append("Most used word: \"" + mostUsed + "\" with a frequency of: " + mostUsedAmount + "\n\n");
		}

		return statsAsString.toString();
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

		processor = new TextProcessor(text);
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
			deletedString = processor.delete(index);

			alert.setContentText("Deleted Item: " + deletedString);
			alert.show();

		} else {
			deletedCount = processor.delete(userInput, combined);
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
		processor = new TextProcessor(text);

		// open the TextView Window
		try {
			FXMLLoader textViewLoader = new FXMLLoader(getClass().getResource("/ie/gmit/java2/view/TextView.fxml"));
			AnchorPane textViewPane = textViewLoader.load();

			TextViewController textViewController = textViewLoader.getController();
			textViewController.setText(processor.getText(), processor.count());

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
