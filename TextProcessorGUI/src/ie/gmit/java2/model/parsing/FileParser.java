/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.net.URL;
import java.util.*;

//TODO Javadocs for classes
//TODO validate input
//DONE use BufferedReader instead of Scanner
//DONE make interface for FileParser?
//TODO add regex

/**
 * Class to parse text from a file. Call the static getText() method and specify
 * the path and the source to collect the text in a List of Strings.
 * 
 * @author Basti
 */
public class FileParser extends Parser {

	// constructor for the FielParser Class, takes the path to the source as an
	// argument
	public FileParser(String sourcePath) {
		setSourcePath(sourcePath);
	};

	/**
	 * Method that tries to retrieve text from the specified source. It stores
	 * the found Strings in a List<String>
	 * 
	 * @return The list of Strings retrieved
	 */
	// @Override
	public List<String> parse2() {
		List<String> text = new ArrayList<>();

		try {
			setScan(new Scanner(new FileReader(new File(getSourcePath()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		getScan().forEachRemaining(text::add);

		for (int i = 0; i < text.size(); i++) {

			String replace = text.get(i).replaceAll("\\--", " ");
			text.set(i, replace);

		}

		return new ArrayList<>(text);
	}

	@Override
	public List<String> parse() {

		List<String> text2 = new ArrayList<>();
		List<String> text3 = new ArrayList<>();
		String words[];

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(getSourcePath())));

			String b = null;
			while ((b = reader.readLine()) != null) {
				text2.add(b);
			}

			for (int i = 0; i < text2.size(); i++) {

				words = text2.get(i).replace("--", " ").split("\\s+");

				text3.addAll(Arrays.asList(words));
			}

			reader.close();

			for (int i = 0; i < text3.size(); i++) {

				if (text3.get(i).equals(" ") || text3.get(i).isEmpty()) {
					text3.remove(i);
					i--;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<String>(text3);
	}

}
