/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;

//TODO Javadocs for classes
//TODO validate input
//DONE use BufferedReader instead of Scanner
//DONE make interface for FileParser?
//DONE add regex

/**
 * Helper class to parse text from a file. Sets the source path and the file specific buffered Reader in the abstract super class.
 * 
 * @author Basti
 */
public class FileParser extends Parser {

	// constructor for the FielParser Class, takes the path to the source as an
	// argument
	public FileParser(String sourcePath) {
		try {
			setSourcePath(sourcePath);
			setReader(new BufferedReader(new FileReader(new File(getSourcePath()))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	};

}
