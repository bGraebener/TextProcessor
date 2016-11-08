/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.net.URL;

public class UrlParser extends Parser {

	public UrlParser(String sourcePath) {
		try {
			setSourcePath(sourcePath);
			setReader(new BufferedReader(new InputStreamReader(new URL(getSourcePath()).openStream())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
