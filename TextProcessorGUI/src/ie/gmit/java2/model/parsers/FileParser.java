package ie.gmit.java2.model.parsers;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Class responsible for setting the BufferedReaders' source to a File. 
 *  
 * @author Basti
 *
 */
public class FileParser extends Parser {

	/**
	 * Constructor that takes a File as a source and initialises the
	 * BufferedReader of the superclass to take a FileReader.
	 * 
	 * @param source
	 */			
	protected FileParser(File source){
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
