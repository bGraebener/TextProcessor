/*
	Created by Basti on 04.11.2016
*/

package ie.gmit.java2.junit;

import org.junit.*;

import ie.gmit.java2.model.parsing.FileParser;
import ie.gmit.java2.model.parsing.UrlParser;

public class ParserTestClass {

	private FileParser fileParser;
	private UrlParser urlParser;

	@Ignore
	@Before
	public void setUp() {
		fileParser = new FileParser("res/test.txt");
		urlParser = new UrlParser("res/test.txt");
	}
	
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void getTextTest() {
		fileParser.parse();
		urlParser.parse();
	}
	
	
	@Test(expected = UnsupportedOperationException.class)
	public void testList() {
		fileParser = new FileParser("res/test.txt");
		fileParser.parse();
	}

}
