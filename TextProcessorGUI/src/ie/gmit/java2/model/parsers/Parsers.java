package ie.gmit.java2.model.parsers;

import java.net.URL;
import java.nio.file.Path;

/**
 * 
 * Class used to retrieve an instance of a Parser. 
 * 
 * @author Basti
 *
 */
public class Parsers {

	private Parsers(){}
	
	/**
	 * Static factory method to retrieve a FileParser. 
	 * @param source Path to the source of the text.
	 * @return A FileParser object.
	 */
	public static FileParser getFileParser(Path source){
		return new FileParser(source);
	}
	
	/**
	 * Static factory method to retrieve a UrlParser. 
	 * @param source URL of the source of the text.
	 * @return A UrlParser object.
	 */
	public static UrlParser getUrlParser(URL source){
		return new UrlParser(source);
	}
	
}
