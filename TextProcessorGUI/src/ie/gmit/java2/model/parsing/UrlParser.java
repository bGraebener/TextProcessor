/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.net.URL;
import java.util.*;

public class UrlParser extends Parser {

	public UrlParser(String sourcePath) {
		setSourcePath(sourcePath);
	}

	@Override
	public List<String> parse() {

		List<String> text = new ArrayList<>();


		try {
			setScan(new Scanner(new URL(getSourcePath()).openStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		getScan().forEachRemaining(text::add);

		return new ArrayList<String>(text);
	}

}
