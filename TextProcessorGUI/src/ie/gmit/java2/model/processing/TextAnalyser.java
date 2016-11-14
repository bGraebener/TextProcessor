/*
	Created by Basti on 09.11.2016
*/

package ie.gmit.java2.model.processing;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextAnalyser implements Processor {

	private List<String> text;

	public TextAnalyser(List<String> text) {
		this.text = text;
	}

	@Override
	public String process(String userInput, BiPredicate<String, String> combined) {

		StringBuilder statsAsString = new StringBuilder();
		int elementsCount = count();
		Map<Integer, String> longestWord = longestWord();
		double averageWordLength = averageWordLength();
		int numOfSentences = countSentences();

		// skip mostUsedMethod if text size is greater than 10,000 because of long
		// runtime
		String mostUsed = elementsCount > 10_000 ? "Most used word: Skipped due to large text size!\n"
				: getMostUsedWord();

		int mostUsedAmount = new TextSearcher(text).countOccurences(mostUsed, String::equalsIgnoreCase);

		longestWord.forEach(
				(k, v) -> statsAsString.append("\nLongest Word(s): \"" + v + "\" with " + k + " characters\n"));
		statsAsString.append(String.format("Average word length is: %.2f\n", averageWordLength));
		statsAsString.append("Number of sentences: " + numOfSentences + "\n");
		statsAsString.append("Total amount of elements: " + elementsCount + "\n");

		if (elementsCount > 10_000) {
			statsAsString.append(mostUsed);
		} else {
			statsAsString
					.append("Most used word: \"" + mostUsed + "\" with a frequency of: " + mostUsedAmount + "\n\n");
		}

		return statsAsString.toString();
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
	 * Method that searches for the word with the highest frequency in a text.
	 * Not suitable for large texts, since the order of magnitude is high!!!!
	 * 
	 * @return The word with the highest frequency
	 */
	public String getMostUsedWord() {

		TextSearcher searcher = new TextSearcher(text);

		int counter = 0;
		Map<Integer, String> mapOfOccurences = new HashMap<>();

		//map strings to their no. of occurrences
		for (int i = 0; i < text.size(); i++) {
			counter = searcher.countOccurences(text.get(i), String::equalsIgnoreCase);
			mapOfOccurences.put(counter, text.get(i));
		}
		
		//find the largest key
		int max = mapOfOccurences.keySet().stream().max(Integer::compareTo).get();
		
		//if the largest key is one, there is no most used word
		if(max == 1){
			return "No most used word!";
		}
		
		return mapOfOccurences.get(max);

	}

	/**
	 * Finds the average length of all words in the text.
	 * 
	 * @return the average length or -1 if no average could be calculated
	 */
	public double averageWordLength() {
		return text.parallelStream().unordered().mapToDouble(String::length).average().orElse(-1);
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
		Map<Integer, String> lengthMap = text.parallelStream().unordered().distinct().filter((x) -> !x.endsWith("."))
				.collect(Collectors.toMap(String::length, Function.identity(), (s, k) -> s + "\\" + k));

		
		// find the longest key
		int length = lengthMap.keySet().stream().max(Integer::compareTo).get();

		// find the words with the calculated length, delete digits
		String stringLength = lengthMap.get(length);
		stringLength = stringLength.replaceAll("\\W\\d+", "");

		// store the two results in a map
		Map<Integer, String> result = new HashMap<>();
		result.put(length, stringLength);

		return result;
	}

	/**
	 * Counts sentences that are delimited by a full stop (".", "?" or "!")
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

	@Override
	public List<String> getText() {
		return text;
	}

}
