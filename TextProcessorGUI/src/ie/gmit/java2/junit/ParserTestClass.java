/*
	Created by Basti on 04.11.2016
*/

package ie.gmit.java2.junit;

import org.junit.Test;

import ie.gmit.java2.model.Parser;
import ie.gmit.java2.model.Parser.Source;

public class ParserTestClass {

	@Test(expected = IllegalArgumentException.class)
	public void getTextTest() {
		Parser.getText(null, Source.FILE);
		Parser.getText(null, Source.URL);
	}

}
