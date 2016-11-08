/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.io.*;
import java.util.*;

public abstract class Parser implements Parseable {

	private String sourcePath;
	private BufferedReader reader;

	/**
	 * Method that tries to retrieve text from the specified source. It stores
	 * the found Strings in a List<String>
	 * 
	 * @return The list of Strings retrieved
	 */
	@Override
	public List<String> parse() {

		if(sourcePath == null || reader == null){
			throw new IllegalArgumentException();
		}
		
		List<String> text2 = new ArrayList<>();
		List<String> text3 = new ArrayList<>();
		String words[];

		try {

			String b = null;
			while ((b = reader.readLine()) != null) {
				text2.add(b);
			}

			for (int i = 0; i < text2.size(); i++) {
				words = text2.get(i).replaceAll("[-,;\"]", " ").split("\\s+");
				text3.addAll(Arrays.asList(words));
			}

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

	protected final String getSourcePath() {
		return sourcePath;
	}

	protected final void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public final BufferedReader getReader() {
		return reader;
	}

	public final void setReader(BufferedReader reader) {
		this.reader = reader;
	}

}
