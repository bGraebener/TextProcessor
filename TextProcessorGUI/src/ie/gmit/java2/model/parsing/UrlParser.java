/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.net.URL;

public class UrlParser extends Parser {

	// constructor for the UrlParser class
	// takes an URL object as an argument and sets the BufferedReader in the
	// superclass.
	public UrlParser(URL source) {

		try {
			setReader(new BufferedReader(new InputStreamReader(source.openStream())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
