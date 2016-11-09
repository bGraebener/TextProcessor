/*
	Created by Basti on 04.11.2016
*/

package ie.gmit.java2.junit;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.*;

import ie.gmit.java2.model.parsing.FileParser;
import ie.gmit.java2.model.parsing.UrlParser;

public class ParserTestClass {

	private FileParser fileParser;
	private UrlParser urlParser;

	@Ignore
	@Before
	public void setUp() {
		fileParser = new FileParser(new File("res/test.txt"));
		try {
			urlParser = new UrlParser(new URL("res/test.txt"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getTextTest() {
		fileParser.parse();
		urlParser.parse();
	}
	
	
	@Test(expected = UnsupportedOperationException.class)
	public void testList() {
		fileParser = new FileParser(new File("res/test.txt"));
		fileParser.parse();
	}

}
