/*
	Created by Basti on 02.11.2016
*/

package ie.gmit.java2.model;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

//XXX delete case sensitive
//XXX delete startsWith/endsWith
//DONE method to find out the most used word
//DONE implement stats methods, i.e. average length of word, longest/shortest word
//TODO add regex to methods
//XXX average no of words in a sentence 
//TODO show words startWith/endWith ?

/**
 * Class to process the text gathered by the Parser. Takes an instance of the
 * parsed text as a List<> in the constructor.
 * 
 * @author Basti
 *
 */
public class TextProcessor {

	private List<String> text;

	// Constructor for the TextProcessor class, takes a List<Strings>
	public TextProcessor(List<String> text) {
		// shallow copy of text, points to the Handlers List object
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
		// retrieve a stream from the text list,
		// filter elements that don't satisfy the BiPredicate and check if
		// there's more than one
		return text.stream().filter((x) -> biPred.test(x, s)).count() > 0;
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
	
		// retrieve a stream from the text list, filter elements that don't
		// satisfy the BiPredicate and count the result
		return (int) text.parallelStream().filter((x) -> biPred.test(x, s)).count();
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
	 * This methods records all indices of occurrences of String s by iterating
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
	 * Deletes elements at index
	 * 
	 * @param index
	 *            index of element to delete
	 * @return The deleted String or message that nothing was deleted
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
	
	
	/*
	 * ===============       Stats methods    ====================
	 */

	/**
	 * Returns the total number of elements in the String
	 * 
	 * @return the number of elements in the text
	 */
	public int count() {
		return text.size();
	}
	

	/**
	 * Method that searches for the word with the highest frequency in a text.
	 * Not suitable for large texts, since the order of magnitude is high!!!!
	 * 
	 * @return The word with the highest frequency
	 */
	public String getMostUsedWord() {

		int counter = 0;
		Map<Integer, String> mapOfOccurences = new HashMap<>();

		for (int i = 0; i < text.size(); i++) {
			counter = countOccurences(text.get(i), String::equalsIgnoreCase);
			mapOfOccurences.put(counter, text.get(i));
		}

		int max = mapOfOccurences.keySet().stream().max(Integer::compareTo).get();
		return mapOfOccurences.get(max);

	}

	/**
	 * Finds the average length of all words in the text.
	 * 
	 * @return the average length or -1 if no average could be calculated
	 */
	public double averageWordLength() {
		return text.parallelStream().mapToDouble(String::length).average().orElse(-1);
	}

	/**
	 * Finds the longest word or words and their length
	 * 
	 * @param boolean
	 *            true for longest word and false for shortest word
	 * @return Map<Integer,String> with the longest length and the corresponding
	 *         words
	 */
	public Map<Integer, String> longestWord() {
		// group unique words according to their length
		Map<Integer, String> lengthMap = text.parallelStream().distinct()
				.collect(Collectors.toMap(String::length, Function.identity(), (s, k) -> s + "\\" + k));


		// find the longest key
		int length = lengthMap.keySet().stream().max(Integer::compareTo).get();

		// find the words with the calculated length
		String stringLength = lengthMap.get(length);
		stringLength = stringLength.replaceAll("\\W\\d+", "");

		// store the two results in a map
		Map<Integer, String> result = new HashMap<>();
		result.put(length, stringLength);

		return result;
	}

	/**
	 * Counts sentences that are delimited by a full stop (".")
	 * 
	 * @return number of sentences
	 */
	public int countSentences() {
		// return (int) text.stream().filter((x) -> x.endsWith(".")).count();
		return (int) text.stream().filter((x) -> x.matches("\\w.|\\w?|\\w!")).count();
	}
	

	/**
	 * Finds all words with the specified length.
	 * 
	 * @param length
	 *            int
	 * @return List of words in the specified length
	 */
	public List<String> findWordsOfLength(int length) {
		return text.stream().filter((x) -> x.length() == length).collect(Collectors.toList());
	}

	public List<String> getText() {
		return text;
	}

}
