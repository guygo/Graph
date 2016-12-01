package application;


import java.util.Optional;

import application.WindowBuilder.Mode;
import javafx.beans.property.DoubleProperty;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.control.TextInputDialog;
class Anchor extends Circle {

	WindowBuilder call;
	Tuple<DoubleProperty, DoubleProperty> coor;
	Pane pan;
    
	public final String name;
	private Label lbl;
	private double lbwidth, lbheight;

	Anchor(Color color, DoubleProperty x, DoubleProperty y, WindowBuilder call, Pane pane, String name) {
		super(x.get(), y.get(), 20);
		pan = pane;
		this.name = name;
		
		
		
		lbl = new Label(name);
		lbl.setFont(Font.font("Cambria", 16));
		lbl.setTextFill(Color.BLACK);
		
		pane.getChildren().add(lbl);
		pane.applyCss();
		pane.layout();
		lbwidth = lbl.getWidth();
		lbheight = lbl.getHeight();
		double max = lbwidth > lbheight ? lbwidth : lbheight;

		super.setRadius(max * 3 / 4);

		lbl.relocate(super.getCenterX() - lbwidth * 3 / 7, super.getCenterY() - lbheight * 4 / 7);

		setFill(color.deriveColor(1, 1, 1, 0.2));
		setStroke(color);
		
		setStrokeWidth(2);
		setStrokeType(StrokeType.OUTSIDE);
		this.call = call;
		x.bind(centerXProperty());
		y.bind(centerYProperty());
		coor = new Tuple<DoubleProperty, DoubleProperty>(x, y);

		enableDrag();
	}
	public Label getLbl() {
		return lbl;
	}
	public void setLbl(Label lbl) {
		this.lbl = lbl;
	}
	
	private void connectnodes()
	{
		setStroke(Color.PURPLE);
		if (call.start != null && call.end != null) {

			call.start = null;
			call.end = null;
		}

		if (call.start == null){
			call.edgename.x=name;
			call.start = coor;
			}
		else if (call.start != coor) {
			call.end = coor;
			call.edgename.y=name;
			if (call.Boundlinelist.get(call.edgename.x+call.edgename.y) == null && 
					call.Boundlinelist.get(call.edgename.y+call.edgename.x)==null) {
				//add first selected node to second node
				TextInputDialog dialog = new TextInputDialog("A");
				
				dialog.setTitle("Graph Editor");

				int weight=0;
				dialog.setContentText("Please enter your Weight:");
				Object result = dialog.showAndWait();
				
				if(result==null){
					result = new String("0");
				}
				else
				{
					if(!((Optional<String>)result).isPresent())
						return;
					
					result=new String(((Optional<String>)result).get());
				}
				try{
				 weight=Integer.parseInt(result.toString());
				}catch (NumberFormatException e) {
				 weight=0;
				}
				call.graph.AddUnDirectedEdge(call.graph.GetVertex(call.edgename.x),call.graph.GetVertex(call.edgename.y),weight);
				
				Line line = new BoundLine(call.start.x, call.start.y, call.end.x, call.end.y, result.toString());
				
				call.Boundlinelist.put (((BoundLine)line).name,(BoundLine)line);
				pan.getChildren().addAll(line,((BoundLine)line).getLbl());
				call.edgename.x="";
				call.edgename.y="";
			}
			else{
				for (String key : call.anchorlist.keySet()) {
					call.anchorlist.get(key).setStroke(Color.PALEGREEN);
				}
				call.start=null;
				call.end=null;
			}
		}
	}
	// make a node movable by dragging it around with the mouse.
	private void enableDrag() {
		final Delta dragDelta = new Delta();
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				if (call.mode.equals(Mode.ADDNODE)||call.mode.equals(Mode.SELECT))
					return;
				dragDelta.x = getCenterX() - mouseEvent.getX();
				dragDelta.y = getCenterY() - mouseEvent.getY();
				
				if (call.mode.equals(Mode.DRAWEDGE))		
					connectnodes();
				
				getScene().setCursor(Cursor.MOVE);

			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (call.mode.equals(Mode.ADDNODE)||call.mode.equals(Mode.SELECT))
					return;
				getScene().setCursor(Cursor.HAND);
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (call.mode.equals(Mode.ADDNODE)||call.mode.equals(Mode.SELECT))
					return;
				double newX = mouseEvent.getX() + dragDelta.x;
				if (newX > 0 && newX < getScene().getWidth()) {
					setCenterX(newX);
				}
				double newY = mouseEvent.getY() + dragDelta.y;
				if (newY-getRadius() >0 && newY+getRadius() <getScene().getHeight()) {
					setCenterY(newY);
				}
				lbl.relocate(getCenterX() - lbwidth * 3 / 7, getCenterY() - lbheight * 4 / 7);

			}
		});
		setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent mouseEvent) {
				if (call.mode.equals(Mode.ADDNODE)||call.mode.equals(Mode.SELECT))
					return;
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.HAND);

				}
				call.setMousein(true);
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (call.mode.equals(Mode.ADDNODE)||call.mode.equals(Mode.SELECT))
					return;
				call.setMousein(false);
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}

	// records relative x and y co-ordinates.
	private class Delta {
		double x, y;
	}

	public void removelbl() {
	  pan.getChildren().remove(lbl);
		
	}

}