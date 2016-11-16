/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
public class Parser {

	private BufferedReader reader;

	/**
	 * Constructor that takes a File as a source and initialises the
	 * BufferedReader to take a FileReader.
	 * 
	 * @param source
	 */
	public Parser(File source) {
		if (!source.exists() || !source.canRead()) {
			throw new IllegalArgumentException("Invalid file");
		}

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor that takes an URL as a source and initialises the
	 * BufferedReader to take an InputStreamReader with the URL's InputStream.
	 * 
	 * @param source
	 */
	public Parser(URL source) {
		try {
			reader = new BufferedReader(new InputStreamReader(source.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

		// retrieve a list of lines from the source text
		List<String> listOfLines = reader.lines().collect(Collectors.toList());
		
		
		// retrieve a list of words from the list of lines
		List<String> listOfWords = new ArrayList<>();
		String wordsArray[];
		for (int i = 0; i < listOfLines.size(); i++) {
			wordsArray = listOfLines.get(i).replaceAll("[-,;\"]", " ").split("\\s+");
			listOfWords.addAll(Arrays.asList(wordsArray));
		}

		
		// cleaning up the list of words
		listOfWords.removeIf((i) -> i.equals(" ") || i.isEmpty());
		

		return new ArrayList<String>(listOfWords);
	}

}
