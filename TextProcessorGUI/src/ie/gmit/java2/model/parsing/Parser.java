/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.util.*;

/**
 * Abstract superclass for all classes responsible for parsing text. The
 * subclasses only responsibility is to set the BuffererdReader with the
 * suitable arguments for the source type.
 * 
 * @author Basti
 *
 */
public abstract class Parser implements Parseable {

	private BufferedReader reader;

	/**
	 * Method that tries to retrieve text from the specified source. It stores
	 * the found Strings in a List<String>.  
	 * 
	 * @return The list of Strings retrieved
	 */
	@Override
	public List<String> parse() {

		if (reader == null) {
			throw new IllegalArgumentException();
		}

		List<String> listOfines = new ArrayList<>();
		List<String> listOfWords = new ArrayList<>();
		String wordsArray[];

		try {

			String b = null;
			while ((b = reader.readLine()) != null) {
				listOfines.add(b);
			}

			for (int i = 0; i < listOfines.size(); i++) {
				wordsArray = listOfines.get(i).replaceAll("[-,;\"]", " ").split("\\s+");
				listOfWords.addAll(Arrays.asList(wordsArray));
			}

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


	public final BufferedReader getReader() {
		return reader;
	}

	public final void setReader(BufferedReader reader) {
		this.reader = reader;
	}

}
