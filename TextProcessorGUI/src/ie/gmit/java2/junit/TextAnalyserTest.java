/*
	Created by Basti on 09.11.2016
*/

package ie.gmit.java2.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import ie.gmit.java2.model.parsing.FileParser;
import ie.gmit.java2.model.parsing.Parseable;
import ie.gmit.java2.model.processing.TextAnalyser;

public class TextAnalyserTest {
	
private List<String> text;
	
	private Parseable fileParser;
	private Parseable urlParser;
	private TextAnalyser analyser;
	
	@Before
	public void setUp() throws Exception {
		fileParser = new FileParser(new File("res/test.txt"));
		text = fileParser.parse();
		analyser = new TextAnalyser(text);
	}

		
	@Test
	public void mostUsedWordIgnoreCase(){
		String mostExpected = "the";
		String mostActual = analyser.getMostUsedWord();		
		assertTrue("Wrong word found", mostActual.equalsIgnoreCase(mostExpected));
	}
	
	@Test
	public void averageLengthTest(){
		double expected = 4.2;
		double actual = analyser.averageWordLength();
		assertTrue("Wrong eaverage", expected == actual);
	}


	@Test
	public void countSentencesTest(){
		int expected = 1;
		int actual = analyser.countSentences();
		assertTrue("Wrong length found", expected == actual);
	}
	
	@Test
	public void longestWordTest(){
		int expectedLength = 6;
		Map<Integer, String> result = analyser.longestWord();
		String actualString = result.get(6);
		assertTrue(result.containsKey(expectedLength) && result.containsValue(actualString));	
	}
	
	@Test
	public void findWordsOfLengthTest() {
		List<String> expectedList = new ArrayList<>();
		expectedList.add("The");
		expectedList.add("was");
		expectedList.add("the");
		List<String> actualList = analyser.findWordsOfLength(3);
		
		assertEquals(expectedList, actualList);
	}
}
