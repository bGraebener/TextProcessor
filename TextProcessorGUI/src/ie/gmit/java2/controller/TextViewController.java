package ie.gmit.java2.controller;

import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * Controller Class for the Text view. Gets called by the Handler Class as a result of a button event on the main window. 
 * @author Basti
 *
 */
public class TextViewController {
	
	@FXML
	private TextArea textArea;
	@FXML
	private Button closeButton;

	private Stage textViewStage;


	// Closes the text view window
	@FXML
	public void closeWindow(ActionEvent event) {
		textViewStage.close();
	}
	

	//Fills the text area with the provided text;
	public void setText(List<String> text, int count) {		
		String textAsString = text.stream().collect(Collectors.joining(" "));		
		textArea.setText(textAsString + "\nCount: " + count);
	}
	
	
	public void setStage(Stage textViewStage){
		this.textViewStage = textViewStage;
	}
}
