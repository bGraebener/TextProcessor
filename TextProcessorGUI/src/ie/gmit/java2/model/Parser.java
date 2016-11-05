/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model;

import java.io.*;
import java.net.URL;
import java.util.*;

//DONE Javadocs for classes

/**
 * Class to parse text from a source. Call the static getText() method and
 * specify the path and the source to collect the text in a List of Strings.
 * 
 * @author Basti
 */
public class Parser {

	//Enumeration to specify the source
	public static enum Source {
		URL, FILE, OTHER
	};

	private static Scanner scan;

	//private constructor to prevent initialisation
	private Parser() {
	};

	/**
	 * Method that tries to retrieve text from the specified source. It stores
	 * the found Strings in a List<String>
	 * 
	 * @param sourcePath
	 *            Source to retrieve text from
	 * @param source
	 *            Specify source of text to parse
	 * @return The list of Strings retrieved
	 */
	public static List<String> getText(String sourcePath, Source source) {
		List<String> text = new ArrayList<>();

		if (sourcePath == null || sourcePath.isEmpty()) {
			source = Source.OTHER;
		}

		switch (source) {
		case FILE:
			try {
				scan = new Scanner(new FileReader(new File(sourcePath)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;

		case URL:
			try {
				scan = new Scanner(new URL(sourcePath).openStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		default:
			throw new IllegalArgumentException("Invalid source specified!");
		}

		scan.forEachRemaining(text::add);

		return new ArrayList<>(text);
	}

}
