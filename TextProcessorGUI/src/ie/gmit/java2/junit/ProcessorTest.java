package ie.gmit.java2.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.function.BiPredicate;

import org.junit.*;

import ie.gmit.java2.model.Parser;
import ie.gmit.java2.model.Parser.Source;
import ie.gmit.java2.model.TextProcessor;

public class ProcessorTest {

	private TextProcessor proc;
	private List<String> text;
	
	private BiPredicate<String, String> start = String::startsWith;
	private BiPredicate<String, String> ends = String::endsWith;
	private BiPredicate<String, String> combined = start.or(ends);

	@Before
	public void setUp() throws Exception {
		text = Parser.getText("res/test.txt", Source.FILE);
		// text = Parser.getText("https://www.gutenberg.org/files/2701/2701.txt", Source.URL);
		proc = new TextProcessor(text);
	}

	@Test
	public void containTest() {
		String studio = "Studio";

		assertTrue("element not contained", proc.contains(studio, String::equalsIgnoreCase));
		assertFalse("element contained", proc.contains(studio, String::equals));
	}

	@Test
	public void countOccurencesTest() {
		int occurencesExpected = 2;
		int occurencesActual = proc.countOccurences("the", String::equalsIgnoreCase);
		int occurencesActual2 = proc.countOccurences("the", String::equals);

		assertTrue("Wrong number of occurences", occurencesActual == occurencesExpected);
		assertFalse("Wrong number of occurences", occurencesActual2 == occurencesExpected);
	}

	@Test
	public void getFirstIndexTest() {
		assertTrue("Wrong index shown", proc.getFirstIndex("Studio", String::equalsIgnoreCase) == 1);
		assertTrue("Wrong index shown", proc.getFirstIndex("Studio", String::equals) == -1);
	}

	@Test
	public void getLastIndexTest() {
		assertTrue("Wrong index shown", proc.getLastIndex("the", String::equalsIgnoreCase) == 5);
		assertTrue("Wrong index shown", proc.getLastIndex("The", String::equals) == 0);
		assertFalse("Wrong index shown", proc.getLastIndex("The", String::equals) == 5);
		assertTrue("Wrong index shown", proc.getFirstIndex("Studio", String::equals) == -1);
	}

	@Test
	public void getAllIndecesTest() {
		int[] expected = { 0, 5 };
		int[] actual = proc.getAllIndeces("the", String::equalsIgnoreCase);

		assertArrayEquals(expected, actual);
	}

	@Test
	public void deleteIndexTest() {
		int expectedSize = 9;
		proc.delete(0);
		int actualSize = proc.count();

		assertTrue("Wrong list size: " + proc.count(), expectedSize == actualSize);
	}

	@Test
	public void deleteObjectTest() {		
		int deletedExpected = 2;
		int deletedActual = proc.delete("s", combined);
		int expectedSize = 8;
		int actualSize = proc.count();
		
		assertTrue("Wrong amount of elements deleted", deletedExpected == deletedActual);
		assertTrue("Wrong list size: " + proc.count(), expectedSize == actualSize);
	}
	
	@Test
	public void mostUsedWordIgnoreCase(){
		String mostExpected = "the";
		String mostActual = proc.getMostUsedWord(String::equalsIgnoreCase);
		
		assertTrue("Wrong word found", mostActual.equalsIgnoreCase(mostExpected));
	}

}
