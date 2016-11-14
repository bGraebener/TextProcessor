/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			reader = new BufferedReader(new FileReader(source));
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

		List<String> listOfLines = new ArrayList<>();
		List<String> listOfWords = new ArrayList<>();
		String wordsArray[];

		try {

			// retrieve a list of lines from the source text
			String b = null;
			while ((b = reader.readLine()) != null) {
				listOfLines.add(b);
			}

			// retrieve a list of words from the list of lines
			for (int i = 0; i < listOfLines.size(); i++) {
				wordsArray = listOfLines.get(i).replaceAll("[-,;\"]", " ").split("\\s+");
				listOfWords.addAll(Arrays.asList(wordsArray));
			}

			// cleaning up the list of words
			for (int i = 0; i < listOfWords.size(); i++) {
				if (listOfWords.get(i).equals(" ") || listOfWords.get(i).isEmpty()) {
					listOfWords.remove(i);
					i--;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<String>(listOfWords);
	}

}
