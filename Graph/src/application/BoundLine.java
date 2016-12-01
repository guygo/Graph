package application;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

class BoundLine extends Line {

	public final String name;
    private Label lbl;
	BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY, String name) {
		startXProperty().bind(startX);
		startYProperty().bind(startY);
		endXProperty().bind(endX);
		endYProperty().bind(endY);
		setStrokeWidth(2);
		this.name = name;
		setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
		setStrokeLineCap(StrokeLineCap.BUTT);
		getStrokeDashArray().setAll(10.0, 5.0);
		setMouseTransparent(true);
		lbl=new Label(name);
		lbl.setFont(Font.font("Amazone BT", 18));
		lbl.setTextFill(Color.YELLOW);
		lbl.setStyle("-fx-background-color: #00008B;");
		lbl.setEffect(new Bloom());
		BackgroundFill fills=new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
		
		lbl.setBackground(new Background(fills));
		
		
		  lbl.setLayoutX((startXProperty().get()+endXProperty().get())/2);
		  lbl.setLayoutY((startYProperty().get()+endYProperty().get())/2);
		startXProperty().addListener(new ChangeListener<Object>() {
		      @Override
		      public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
		    	  lbl.setLayoutX((startXProperty().get()+endXProperty().get())/2);
		      }
		    });
		endXProperty().addListener(new ChangeListener<Object>() {
		      @Override
		      public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
		    	  lbl.setLayoutX((startXProperty().get()+endXProperty().get())/2);
		      }
		    });
		
		startYProperty().addListener(new ChangeListener<Object>() {
		      @Override
		      public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
		    	  lbl.setLayoutY((startYProperty().get()+endYProperty().get())/2);
		      }
		    });
		endYProperty().addListener(new ChangeListener<Object>() {
		      @Override
		      public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
		    	  lbl.setLayoutY((startYProperty().get()+endYProperty().get())/2);
		      }
		    });
		
		
		lbl.setLayoutY((startYProperty().get()+endYProperty().get())/2);
		lbl.setOnMousePressed((new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
			System.out.println("hi");
			}
			}));
		
		
	}

	public void setLbl(Label lbl) {
		this.lbl = lbl;
	}
	public Label getLbl() {
		return lbl;
	}
	
}