/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model.processing;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Class to process the text gathered by the Parser. Takes an instance of the
 * parsed text as a List<> in the constructor.
 * 
 * @author Basti
 */
public class TextSearcher implements Processor {

	private List<String> text;

	// Constructor for the TextSearcher class, takes a List<Strings>
	public TextSearcher(List<String> text) {
		// shallow copy of text, points to the Handlers List object
		this.text = text;
	}

	@Override
	public String process(String userInput, BiPredicate<String, String> combined) {

		StringBuilder statsAsString = new StringBuilder();

		boolean containsUserInput = contains(userInput, combined);
		int firstIndexOf = getFirstIndex(userInput, combined);
		int lastIndexOf = getLastIndex(userInput, combined);
		int occurencesCount = countOccurences(userInput, combined);
		int[] occurencesIndices = getAllIndeces(userInput, combined);

		statsAsString.append("\nUser Input: " + userInput + "\n");
		statsAsString.append("Contains Input: " + containsUserInput + "\n");
		statsAsString.append("Num of Occurences: " + occurencesCount + "\n");
		statsAsString.append("First Index: " + firstIndexOf + "\n");
		statsAsString.append("Last Index: " + lastIndexOf + "\n");
		statsAsString.append("Indices of occurences: " + Arrays.toString(occurencesIndices) + "\n\n");

		return statsAsString.toString();
	}

	/**
	 * Check whether a text contains a String
	 * 
	 * @param s
	 *            String to check for
	 * @param biPred
	 *            BiPredicate to set whether to search case sensitive or to use
	 *            startsWith/endWith
	 * @return whether the text contains the String
	 */
	private boolean contains(String s, BiPredicate<String, String> biPred) {
		// retrieve a stream from the text list,
		// filter elements that don't satisfy the BiPredicate and check if
		// there's at least one.
		return text.stream().filter((x) -> biPred.test(x, s)).count() > 0;
	}

	/**
	 * Returns number of occurrences of a String in the text
	 * 
	 * @param s
	 *            String to check for
	 * @param pred
	 *            BiPredicate to set whether to search case sensitively or to
	 *            use startsWith/endWith
	 * @return the number of occurrences of String s
	 */
	public int countOccurences(String s, BiPredicate<String, String> biPred) {
		// retrieve a stream from the text list, filter elements that don't
		// satisfy the BiPredicate and count the result
		return (int) text.parallelStream().unordered().filter((x) -> biPred.test(x, s)).count();
	}

	/**
	 * Finds the first match in the text that satisfies the BiPredicate.
	 * 
	 * @param s
	 *            String to look for in the text
	 * @param biPred
	 *            BiPredicate whether the search should be case sensitively or
	 *            to use startsWith/endWith
	 * @return index of first match or -1 if no match found
	 */
	private int getFirstIndex(String s, BiPredicate<String, String> biPred) {
		// iterate over the list and return the index of the first element that
		// satisfies the BiPredicate
		// return -1 if no element is found
		for (int i = 0; i < text.size(); i++) {
			if (biPred.test(text.get(i), s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds last match in the text that satisfies the BiPredicate.
	 * 
	 * @param s
	 *            String to look for in the text
	 * @param biPred
	 *            Define a BiPredicate whether the search should be case
	 *            sensitive or to use startsWith/endWith
	 * @return index of last match or -1 if no match found
	 */
	private int getLastIndex(String s, BiPredicate<String, String> biPred) {
		// iterate over list backwards and return index of first element to
		// satisfy the BiPredicate
		// return -1 if no element is found
		for (int i = text.size() - 1; i >= 0; i--) {
			if (biPred.test(text.get(i), s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This methods records all indices of occurrences of a String by iterating
	 * over the text.
	 * 
	 * @param s
	 *            String to search for in text
	 * @param biPred
	 *            BiPredicate to set whether to search case sensitive or to use
	 *            startsWith/endWith
	 * @return int array of indices of all occurrences of s in text or null if
	 *         no occurrences found
	 */
	private int[] getAllIndeces(String s, BiPredicate<String, String> biPred) {

		int count = countOccurences(s, biPred);

		if (count == 0) {
			return null;
		}

		int[] allIndeces = new int[count];
		int i = 0;

		for (int j = 0; j < text.size(); j++) {
			if (biPred.test(text.get(j), s)) {
				allIndeces[i++] = j;
			}
		}
		return allIndeces;
	}

	/**
	 * Deletes elements at an index.
	 * 
	 * @param index
	 *            index of element to delete
	 * @return The deleted String or message that nothing was deleted
	 */
	public String delete(int index) {

		if (text == null || text.isEmpty()) {
			return "No text found";
		}

		if (text.size() > index) {
			return text.remove(index);
		}
		return "Nothing deleted";
	}

	/**
	 * Deletes all String elements that satisfy the BiPredicate.
	 * 
	 * @param string
	 *            element to delete
	 * @param biPred
	 *            BiPredicate to set whether to set the search case-sensitive or
	 *            to use startsWith/endWith
	 * @return Amount of elements deleted
	 */
	public int delete(String string, BiPredicate<String, String> biPred) {

		if (text.isEmpty() || text == null) {
			return -1;
		}

		int deleted = 0;

		for (int i = 0; i < text.size(); i++) {
			if (biPred.test(text.get(i), string)) {
				text.remove(i);
				i--;
				deleted++;
			}
		}
		return deleted;
	}
}
