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

	public List<String> parse2() {

		List<String> text = new ArrayList<>();


		try {
			setScan(new Scanner(new URL(getSourcePath()).openStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}

		
		getScan().forEachRemaining(text::add);

		return new ArrayList<String>(text);
	}
	
	
	@Override
	public List<String> parse() {

		List<String> text2 = new ArrayList<>();
		List<String> text3 = new ArrayList<>();
		String words[];

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(getSourcePath()).openStream()));

			String b = null;
			while ((b = reader.readLine()) != null) {
				text2.add(b);
			}

			for (int i = 0; i < text2.size(); i++) {

				words = text2.get(i).replace("--", " ").split("\\s+");

				text3.addAll(Arrays.asList(words));
			}

			reader.close();

			for (int i = 0; i < text3.size(); i++) {

				if (text3.get(i).equals(" ") || text3.get(i).isEmpty()) {
					text3.remove(i);
					i--;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return new ArrayList<String>(text3);
	}
	

}
