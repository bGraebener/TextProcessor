package ie.gmit.java2.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.function.BiPredicate;

import org.junit.Before;
import org.junit.Test;

import ie.gmit.java2.model.parsing.FileParser;
import ie.gmit.java2.model.parsing.Parseable;
import ie.gmit.java2.model.parsing.UrlParser;
import ie.gmit.java2.model.processing.TextAnalyser;
import ie.gmit.java2.model.processing.TextSearcher;

public class TextSearcherTest {

	private TextSearcher searcher;
	private TextAnalyser analyser;
	private List<String> text;
	
	private Parseable fileParser;
	private Parseable urlParser;
	
	private BiPredicate<String, String> start = String::startsWith;
	private BiPredicate<String, String> ends = String::endsWith;
	private BiPredicate<String, String> combined = start.or(ends);

	@Before
	public void setUp() throws Exception {
		fileParser = new FileParser(new File("res/test.txt"));
		text = fileParser.parse();
		
//		urlParser = new UrlParser("https://www.gutenberg.org/files/2701/2701.txt");
//		text = urlParser.parse();
		
		searcher = new TextSearcher(text);
		analyser = new TextAnalyser(text);
	}

	@Test
	public void containTest() {
		String studio = "Studio";

		assertTrue("element not contained", searcher.contains(studio, String::equalsIgnoreCase));
		assertFalse("element contained", searcher.contains(studio, String::equals));
	}

	@Test
	public void countOccurencesTest() {
		int occurencesExpected = 2;
		int occurencesActual = searcher.countOccurences("the", String::equalsIgnoreCase);
		int occurencesActual2 = searcher.countOccurences("the", String::equals);

		assertTrue("Wrong number of occurences", occurencesActual == occurencesExpected);
		assertFalse("Wrong number of occurences", occurencesActual2 == occurencesExpected);
	}

	@Test
	public void getFirstIndexTest() {
		assertTrue("Wrong index shown", searcher.getFirstIndex("Studio", String::equalsIgnoreCase) == 1);
		assertTrue("Wrong index shown", searcher.getFirstIndex("Studio", String::equals) == -1);
	}

	@Test
	public void getLastIndexTest() {
		assertTrue("Wrong index shown", searcher.getLastIndex("the", String::equalsIgnoreCase) == 5);
		assertTrue("Wrong index shown", searcher.getLastIndex("The", String::equals) == 0);
		assertFalse("Wrong index shown", searcher.getLastIndex("The", String::equals) == 5);
		assertTrue("Wrong index shown", searcher.getFirstIndex("Studio", String::equals) == -1);
	}

	@Test
	public void getAllIndecesTest() {
		int[] expected = { 0, 5 };
		int[] actual = searcher.getAllIndeces("the", String::equalsIgnoreCase);

		assertArrayEquals(expected, actual);
	}

	@Test
	public void deleteIndexTest() {
		int expectedSize = 9;
		searcher.delete(0);
		int actualSize = analyser.count();

		assertTrue("Wrong list size: " + analyser.count(), expectedSize == actualSize);
	}

	@Test
	public void deleteObjectTest() {		
		int deletedExpected = 2;
		int deletedActual = searcher.delete("s", combined);
		int expectedSize = 8;
		int actualSize = analyser.count();
		
		assertTrue("Wrong amount of elements deleted", deletedExpected == deletedActual);
		assertTrue("Wrong list size: " + analyser.count(), expectedSize == actualSize);
	}
	
	
	
	
}
