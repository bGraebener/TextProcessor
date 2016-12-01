/*
	Created by Basti on 09.11.2016
*/

package ie.gmit.java2.model.processing;

import java.util.function.BiPredicate;

/**
 * Interface to be implemented by classes that do text processing
 * 
 * @author Bast
 */
public interface Processor {

	/**
	 * Method that processes the text.
	 * 
	 * @param userInput
	 *            String input by the user.  
	 * @param combined
	 *            BiPredicate that can be used to set search options like
	 *            startsWith/endsWith, substring and case sensitivity
	 * @return A String that represents the result of the processing operation
	 */
	String process(String userInput, BiPredicate<String, String> combined);

}
