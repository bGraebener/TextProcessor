/*
	Created by Basti on 07.11.2016
*/

package ie.gmit.java2.model.parsing;

import java.util.Scanner;

public abstract class Parser implements Parseable{
	
	private String sourcePath;
	private Scanner scan;
	
	protected final String getSourcePath() {
		return sourcePath;
	}
	protected final void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	protected final Scanner getScan() {
		return scan;
	}
	protected final void setScan(Scanner scan) {
		this.scan = scan;
	}
	
	
	

}
