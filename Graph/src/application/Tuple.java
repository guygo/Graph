package application;

public class Tuple<X, Y> { 
	  public  X x; 
	  public  Y y; 
	  public Tuple(X x, Y y) { 
	    this.x = x; 
	    this.y = y; 
	  
	  
	  }
	
	  public boolean equals(Object o)
	  {
		  if(o instanceof Tuple<?,?>)
		  return ((Tuple<X,Y>)o).x.equals(x)||((Tuple<X,Y>)o).y.equals(y);
		  
		  return false;
	  }
	} 