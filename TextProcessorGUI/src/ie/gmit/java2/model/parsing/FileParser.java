/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.net.URL;
import java.util.*;

//TODO Javadocs for classes
//TODO validate input
//TODO use BufferedReader instead of Scanner
//DONE make interface for FileParser?

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
	@Override
	public List<String> parse() {
		List<String> text = new ArrayList<>();
		List<String> text2 = new ArrayList<>();
		
		try {
			setScan(new Scanner(new FileReader(new File(getSourcePath()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(new File(getSourcePath())));
//
//			String b = null;
//			while ((b = reader.readLine()) != null) {
//				text2.add(b);
//			}
//			
//			List<String> text3 = null;
//			
//			for (int i = 0; i < text2.size(); i++) {
//						
//				String words[] = text2.get(i).split(".");
//				
//				text3 = Arrays.asList(words);
//			}
//			
//			reader.close();
//			text3.remove(0);
//			
//			
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		getScan().forEachRemaining(text::add);

		return new ArrayList<>(text);
	}

}
