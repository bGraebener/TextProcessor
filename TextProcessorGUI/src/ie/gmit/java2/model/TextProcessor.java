/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

//XXX delete case sensitive
//XXX delete startsWith/endsWith
//TODO method to find out the most used word

/**
 * Class to process the text gathered by the Parser. Pass an instance of the
 * parsed text as a List<> to the constructor.
 * 
 * @author Basti
 *
 */
public class TextProcessor {

	private List<String> text;

	public TextProcessor(List<String> text) {
		// shallow copy of text
		this.text = text;
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
	public boolean contains(String s, BiPredicate<String, String> biPred) {
		return text.stream().filter((x) -> biPred.test(x, s)).count() > 0;
	}

	/**
	 * Returns the total number of elements in the String
	 * 
	 * @return the number of elements in the text
	 */
	public int count() {
		return text.size();
	}

	/**
	 * Returns number of occurrences of a String in the text
	 * 
	 * @param s
	 *            String to check for
	 * @param pred
	 *            BiPredicate to set whether to search case sensitive or to use
	 *            startsWith/endWith
	 * @return the number of occurrences of String s
	 */
	public int countOccurences(String s, BiPredicate<String, String> biPred) {
		return (int) text.stream().filter((x) -> biPred.test(x, s)).count();
	}

	/**
	 * Finds first match in the text.
	 * 
	 * @param s
	 *            String to look for in the text
	 * @param biPred
	 *            Define a BiPredicate whether the search should be case
	 *            sensitive or to use startsWith/endWith
	 * @return index of first match or -1 if no match found
	 */
	public int getFirstIndex(String s, BiPredicate<String, String> biPred) {

		for (int i = 0; i < text.size(); i++) {
			if (biPred.test(text.get(i), s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Finds last match in the text.
	 * 
	 * @param s
	 *            String to look for in the text
	 * @param biPred
	 *            Define a BiPredicate whether the search should be case
	 *            sensitive or to use startsWith/endWith
	 * @return index of last match or -1 if no match found
	 */
	public int getLastIndex(String s, BiPredicate<String, String> biPred) {

		for (int i = text.size() - 1; i >= 0; i--) {
			if (biPred.test(text.get(i), s)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * This methods iterates over the text and records all indices of
	 * occurrences of String s
	 * 
	 * @param s
	 *            String to search for in text
	 * @param biPred
	 *            BiPredicate to set whether to search case sensitive or to use
	 *            startsWith/endWith
	 * @return int array of indices of all occurrences of s in text or null if
	 *         no occurrences found
	 */
	public int[] getAllIndeces(String s, BiPredicate<String, String> biPred) {

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
	 * Method that searches for the word with the highest frequency in a text.
	 * Not suitable for large texts, since the order of magnitude is very high (O)n³!!!!
	 * @param biPred BiPredicate to choose whether to ignore case or not
	 * @return The word with the highest frequency
	 */
	public String getMostUsedWord(BiPredicate<String, String> biPred) {

		int highestIndex = -1;
		int highest = 0;
		
		List<String> distinct = text.stream().filter((x) -> countOccurences(x, biPred) > 1).distinct()
				.collect(Collectors.toList());

		distinct.forEach(System.out::println);
		
		List<Integer> occurenceList = text.stream().map((x) -> countOccurences(x, biPred)).collect(Collectors.toList());

		for (int i = 0; i < occurenceList.size(); i++) {
			if (occurenceList.get(i) > highest) {
				highest = occurenceList.get(i);
				highestIndex = i;
			}
		}
		return distinct.get(highestIndex);
	}

	/**
	 * Deletes elements at index
	 * 
	 * @param index
	 *            index of element to delete
	 * @return The deleted String
	 */
	public String delete(int index) {
		if (text.size() > index) {
			return text.remove(index);
		}
		return "Nothing deleted";
	}

	/**
	 * Deletes all Strings that satisfy the BiPredicate.
	 * 
	 * @param string
	 *            element to delete
	 * @param biPred
	 *            BiPredicate to set whether to set the search case-sensitive or
	 *            to use startsWith/endWith
	 * @return Amount of elements deleted
	 */
	public int delete(String string, BiPredicate<String, String> biPred) {
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

	public List<String> getText() {
		return text;
	}

}
