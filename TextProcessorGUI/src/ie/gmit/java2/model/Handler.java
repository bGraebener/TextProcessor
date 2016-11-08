/*
	Created by Basti on 05.11.2016
*/

package ie.gmit.java2.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

import ie.gmit.java2.controller.MainWindowController;
import ie.gmit.java2.controller.TextViewController;
import ie.gmit.java2.model.parsing.FileParser;
import ie.gmit.java2.model.parsing.UrlParser;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

//TODO implement stats method 

/**
 * Class that retrieves the results from the users query and passes it back to
 * the Controller class.
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
		// statsAsString = new StringBuilder(
		// "UserInput\t\t\tcontains\t\t\tfirst index\t\t\tlast index\t\t\tocc.
		// count\t# elements\n");
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
	public void parse(String sourcePath, Source sourceType) {

		switch (sourceType) {
		case FILE:
			FileParser fileParser = new FileParser(sourcePath);
			text = fileParser.parse();
			break;
		case URL:
			UrlParser urlParser = new UrlParser(sourcePath);
			text = urlParser.parse();
			break;
		case OTHER:
		default:
			throw new IllegalArgumentException("Invalid source");
		}

		alert.setContentText("Text parsed!");
		alert.show();

	}

	/**
	 * Method that retrieves all data from the user query by calling the methods
	 * from the TextProcessor class.
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
		statsAsString.append("Indices of occurences: " + Arrays.toString(occurencesIndices) + "\n");

		return statsAsString.toString();
	}

	public String getStats() {

		if (!text.isEmpty()) {
			processor = new TextProcessor(text);
		}

		int elementsCount = processor.count();
		String mostUsed = processor.getMostUsedWord();
		Map<Integer, String> longestWord = processor.longestWord();
		int shortestWord = processor.shortestWord();
		double averageWordLength = processor.averageWordLength();
		int numOfSentences = processor.countSentences();
		int mostUsedAmount = processor.countOccurences(mostUsed, String::equalsIgnoreCase);

		longestWord
				.forEach((k, v) -> statsAsString.append("Longest Word(s): \"" + v + "\" with " + k + " characters\n"));
		statsAsString.append("Shortest word has " + shortestWord + " characters\n");
		statsAsString.append("Average word length is: " + averageWordLength + "\n");
		statsAsString.append("Number of sentences: " + numOfSentences + "\n");
		statsAsString.append("Total amount of elements: " + elementsCount + "\n");
		statsAsString.append("Most used word: \"" + mostUsed + "\" with a frequency of: " + mostUsedAmount + "\n\n");
		// statsAsString.append("Most used word frequency: " + mostUsedAmount +
		// "\n\n");

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
	 * Case-Sensitive option can only be chosen if the startsWith and endsWith
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
