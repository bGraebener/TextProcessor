/*
	Created by Basti on 09.11.2016
*/

package ie.gmit.java2.model.processing;

import java.util.List;
import java.util.function.BiPredicate;

public interface Processor {

	String process(String userInput, BiPredicate<String,String> combined);
	List<String> getText();
	
}
