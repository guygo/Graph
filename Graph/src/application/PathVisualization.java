package application;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.animation.Animation;
import javafx.animation.PathTransition;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import javafx.util.Duration;

public class PathVisualization  {

    

    private Canvas canvas;
    private Pane root;
    private Animation temp;
    public PathVisualization(Canvas c,Pane r){
        
    	root=r;
        canvas = c;
           
       
    }
    
	public Canvas getCanvas() {
		return canvas;
	}
    public void clearcanvas(){
    	
    	GraphicsContext gc = canvas.getGraphicsContext2D();
    	gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
       

    public static class Location {
        double x;
        double y;
    }

	
	public void PlayAnimation(Queue<Path> paths){
    	
		if(paths.isEmpty())
			return;
		Path pop=paths.poll();
		Animation animation;
		animation = createPathAnimation(pop, Duration.seconds(5));
        animation.play();
		animation.setOnFinished(e->{
			
			PlayAnimation(paths);
			});
		}
		
		
	
	
	private Animation createPathAnimation(Path path, Duration duration) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // move a node along a path. we want its position
        Circle pen = new Circle(0,0, 4);

        // create path transition
        PathTransition pathTransition = new PathTransition( duration, path, pen);
        pathTransition.currentTimeProperty().addListener( new ChangeListener<Duration>() {

            Location oldLocation = null;

            /**
             * Draw a line from the old location to the new location
             */
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

            	
                // skip starting at 0/0
                if( oldValue == Duration.ZERO)
                    return;

                // get current location
                double x = pen.getTranslateX();
                double y = pen.getTranslateY();

                // initialize the location
                if( oldLocation == null) {
                    oldLocation = new Location();
                    oldLocation.x = x;
                    oldLocation.y = y;
                    return;
                }

                // draw line
                Color col=Color.YELLOW;
                
                gc.setStroke(col);
                gc.setLineWidth(20);
                
                gc.strokeLine(oldLocation.x, oldLocation.y, x, y);

                // update old location with current one
                oldLocation.x = x;
                oldLocation.y = y;
            }
        });

        return pathTransition;
    }
	
	

	

    
}
