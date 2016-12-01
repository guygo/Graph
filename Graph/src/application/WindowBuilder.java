package application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import Graph.Graph;
import Graph.Vertex;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.elements.*;

public class WindowBuilder {

	public Mode mode;

	Tuple<DoubleProperty, DoubleProperty> start;
	Tuple<DoubleProperty, DoubleProperty> end;

	Graph graph;
	HashMap<String, Anchor> anchorlist = new HashMap();
	HashMap<String, BoundLine> Boundlinelist = new HashMap();
	PathVisualization paths;
	Tuple<String, String> edgename;
	WindowBuilder ref;
	private boolean mousein;
	public EventHandler<MouseEvent> handler;
	private TextInputDialog dialog = new TextInputDialog("A");
	private Optional<String> result;
	private Canvas c;
	private Queue<Path> pathlist = new LinkedList();
	private Pane root;

	public enum Mode {
		DRAG, ADDNODE, DRAWEDGE, SELECT
	};

	public WindowBuilder(Stage primaryStage) {

		c = new Canvas();
		graph = new Graph();
		dialog.setTitle("Graph Editor");
		mode=Mode.SELECT;
		edgename = new Tuple<String, String>("", "");
		dialog.setContentText("Please enter your Vertex name:");
		ref = this;
		primaryStage.setTitle("Line Manipulation Sample");
		primaryStage.setScene(scenebuilder());
		primaryStage.show();
	}

	private void clearboard() {
		paths.clearcanvas();
		for (Path p : pathlist) {
			root.getChildren().remove(p);
		}
		pathlist.clear();

	}

	private void constructButton(String name, EventHandler<ActionEvent> handler, Image image, HBox hbox) {

		hbox.getChildren().add(new ButtonBuilder(name, image, handler));
		
	}

	private void constructButton(String name, EventHandler<ActionEvent> handler, HBox hbox) {

		Button b=new ButtonBuilder(name, handler);
		BackgroundImage bi=new BackgroundImage(createimage("/Images/graph.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background back=new Background(bi);
		b.setBackground(back);
		b.setPrefHeight(140);
		b.setPrefWidth(140);
		 Light.Distant light = new Light.Distant();
	        light.setAzimuth(-135.0f);
	        light.setElevation(30.0f);
	 
	        Lighting l = new Lighting();
	        l.setLight(light);
	        l.setSurfaceScale(5.0f);
	 
	        final Text t = new Text();
	        t.setText("DistantLight");
	        t.setFill(Color.RED);
	        t.setFont(Font.font(null, FontWeight.BOLD, 70));
	        t.setX(10.0f);
	        t.setY(10.0f);
	        t.setTextOrigin(VPos.TOP);
	 
	        t.setEffect(l);
	        b.setTextFill(Color.RED);
	        b.setFont(Font.font(null, FontWeight.BOLD, 30));
	        b.setEffect(l);
		hbox.getChildren().add(b);
	}

	
	private void AddNode(double x, double y)
	{
		DoubleProperty X = new SimpleDoubleProperty(x);
		DoubleProperty Y = new SimpleDoubleProperty(y);
		Anchor anchor;

		result = dialog.showAndWait();
		if (result.isPresent()) {
			String name = result.get();

			if (anchorlist.get(name) == null) {
				anchor = new Anchor(Color.PALEGREEN, X, Y, ref, root, name);
				double centerx = anchor.getCenterX();
				double centery = anchor.getCenterY();
				double r = anchor.getRadius();
				
				
				
				if (centerx - r > 0 && centerx + r < root.getWidth() && centery-r  > 0
						&& centery + r < root.getHeight()) {

					graph.AddVertex(new Vertex(name, Integer.MAX_VALUE));
					anchorlist.put(name, anchor);
					root.getChildren().add(anchor);
				} else {
					anchor.removelbl();
					anchor = null;
				}
			}
		}
		
	}
	private Scene scenebuilder() {

		BorderPane border = new BorderPane();
		HBox hbox = new HBox();
		createallbuttons(hbox);
		border.setTop(hbox);
		root = new Pane();
		root.getStyleClass().add("pane");
		root.getChildren().add(c);
		border.setCenter(root);

		root.setStyle("-fx-background-color: #FAE68E");
		root.setBackground(new Background(new BackgroundFill(Color.web("#FAE68E"), CornerRadii.EMPTY, Insets.EMPTY)));
		paths = new PathVisualization(c, root);
		// Line line = new BoundLine(startX, startY, endX, endY);
		Scene scene = new Scene(border, 800, 600, Color.ALICEBLUE);

		handler = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mode.equals(Mode.SELECT)||mode.equals(Mode.DRAG)) {

					if (!isMousein()) {
						for (String key : anchorlist.keySet()) {
							anchorlist.get(key).setStroke(Color.PALEGREEN);
						}
						start = null;
						end = null;
					}

					return;
				}

				clearboard();
				
				if(mode.equals(Mode.ADDNODE))
				AddNode(mouseEvent.getX(),mouseEvent.getY());
			}
		};
		root.addEventFilter(MouseEvent.MOUSE_PRESSED, handler);
		return scene;
	}

	private Image createimage(String path) {

		
		return new Image(getClass().getResourceAsStream(path));
	}

	private void createallbuttons(HBox hbox) {

		

		constructButton("", e -> {
			mode = Mode.DRAG;
		}, createimage("/Images/drag.png"), hbox);
		
		constructButton("", e -> {
			mode = Mode.ADDNODE;
		}, createimage("/Images/plus.png"), hbox);
				
		constructButton("", e -> {
			mode = Mode.DRAWEDGE;
		}, createimage("/Images/doublearrow.png"), hbox);
		
		constructButton("dfs", e -> {
			mode = Mode.SELECT;
			result = dialog.showAndWait();
			paths.getCanvas().setHeight(root.getHeight());
			paths.getCanvas().setWidth(root.getWidth());
			if (result.isPresent()) {
				clearboard();
				String name = result.get();
				Graph g = graph.Dfs(name);
                if(g==null)
                	return;
				for (Vertex v : g.getVertexes()) {

					Anchor A = anchorlist.get(v.name);

					for (Tuple<Integer, Vertex> x : g.GetVertex(v.name).getNeighbors()) {

						Path path = new Path();

						Anchor B = anchorlist.get(x.y.name);
						path.getElements().addAll(new MoveTo(A.getCenterX(), A.getCenterY()),
								new LineTo(B.getCenterX(), B.getCenterY()));
						pathlist.add(path);

					}

				}
				root.getChildren().addAll(pathlist);
				paths.PlayAnimation(new LinkedList<Path>(pathlist));
			}
		}, hbox);

		constructButton("bfs", e -> {
			mode = Mode.SELECT;
			result = dialog.showAndWait();
			paths.getCanvas().setHeight(root.getHeight());
			paths.getCanvas().setWidth(root.getWidth());
			if (result.isPresent()) {
				clearboard();
				String name = result.get();
				Graph g = graph.Bfs(name);
				 if(g==null)
	                	return;
				for (Vertex v : g.getVertexes()) {

					Anchor A = anchorlist.get(v.name);

					for (Tuple<Integer, Vertex> x : g.GetVertex(v.name).getNeighbors()) {

						Path path = new Path();

						Anchor B = anchorlist.get(x.y.name);
						path.getElements().addAll(new MoveTo(A.getCenterX(), A.getCenterY()),
								new LineTo(B.getCenterX(), B.getCenterY()));
						pathlist.add(path);

					}

				}
				root.getChildren().addAll(pathlist);
				paths.PlayAnimation(new LinkedList<Path>(pathlist));
			}

		}, hbox);

		

	}

	public boolean isMousein() {
		return mousein;
	}

	public void setMousein(boolean mousein) {
		this.mousein = mousein;
	}
}
