package ui.elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonBuilder extends Button {

	
	public ButtonBuilder(String name,Image icon,EventHandler<ActionEvent> Handler) {
	
		super(name,new ImageView(icon));
		setOnAction(Handler);
	}
	public ButtonBuilder(String name,EventHandler<ActionEvent> Handler) {
		
		super(name);
		setOnAction(Handler);
	}
}
