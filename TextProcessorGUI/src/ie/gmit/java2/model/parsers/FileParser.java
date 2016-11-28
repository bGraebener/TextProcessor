package ie.gmit.java2.model.parsers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileParser extends Parser {

	/**
	 * Constructor that takes a File as a source and initialises the
	 * BufferedReader of the superclass to take a FileReader.
	 * 
	 * @param source
	 */			
	protected FileParser(Path source){
		try {
			reader = Files.newBufferedReader(source, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
