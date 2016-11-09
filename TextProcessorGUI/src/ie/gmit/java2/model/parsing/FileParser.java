/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;

//TODO Javadocs for classes
//DONE validate input
//DONE use BufferedReader instead of Scanner
//DONE make interface for FileParser?
//DONE add regex

/**
 * Helper class to parse text from a file. Takes a File object and Sets the file
 * specific buffered Reader in the abstract super class.
 * 
 * @author Basti
 */
public class FileParser extends Parser {

	// constructor for the FileParser class
	// takes the path to the source as an argument and sets the source path and
	// the BufferedReader in the superclass.
	public FileParser(File source) {

		if (!source.exists() || !source.canRead()) {
			throw new IllegalArgumentException("Invalid file");
		}

		try {
			setReader(new BufferedReader(new FileReader(source)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	};

}
