/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsers;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that is responsible for parsing text. The Class uses a BuffererdReader
 * with the suitable arguments for the source type to retrieve text from the
 * specified source.
 * 
 * @author Basti
 *
 */
public abstract class Parser {

	protected BufferedReader reader;

	/**
	 * Method that tries to retrieve text from the specified source. It stores
	 * the found Strings in a List<String>.
	 * 
	 * @return A new list of Strings retrieved from the source, null if the
	 *         BufferedReader is not set
	 */
	public List<String> parse() {

		if (reader == null) {
			return null;
		}

		// retrieve a list of lines from the source
		List<String> listOfLines = reader.lines().collect(Collectors.toList());
		
		listOfLines.replaceAll((i) -> i.replaceAll("[-;\"]", " "));
		
		// retrieve a list of words from the list of lines
		List<String> listOfWords = new ArrayList<>();
		String wordsArray[];
		
		for (String line : listOfLines) {
			wordsArray = line.split("\\s+");
			listOfWords.addAll(Arrays.asList(wordsArray));
		}

		// clean up the list of words
		listOfWords.removeIf((i) -> i.equals(" ") || i.isEmpty());

		//return deep copy of List
		return new ArrayList<String>(listOfWords);
	}

}
