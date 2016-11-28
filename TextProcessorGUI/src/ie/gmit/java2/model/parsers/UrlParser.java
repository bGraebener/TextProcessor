package ie.gmit.java2.model.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UrlParser extends Parser {

	/**
	 * Constructor that takes an URL as a source and initialises the
	 * BufferedReader of the superclass to take an InputStreamReader with the
	 * URL's InputStream and the UTF-8 characterset.
	 * 
	 * @param source Source of the URL to be read
	 */
	protected UrlParser(URL source) {
		try {
			reader = new BufferedReader(new InputStreamReader(source.openStream(), StandardCharsets.UTF_8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
