package Graph;



import java.util.LinkedList;



import application.Tuple;
public class Vertex {

	public final String name;
	private int rank;
	
	public Vertex papa;
	private  LinkedList<Tuple<Integer,Vertex>> neighbors;
	private boolean Visted;
	public Vertex(String name,int rank)
	{
		this.name=name;
		this.setRank(rank);
		neighbors=new LinkedList<Tuple<Integer,Vertex>>();
	}
	
	public void AddNeighbor(int weight,Vertex v)
	{
		neighbors.add(new Tuple<Integer,Vertex>(weight,v));
	}
	
	public LinkedList<Tuple<Integer,Vertex>> getNeighbors() {
		return neighbors;
	}
	
	public boolean equals(Object name)
	{
		
		return name.equals((String)name);
	}
	public Vertex getNeighbor(String name)
	{
		Tuple<Integer,Vertex> t=GetNeighbor(name);
			if(t.y.equals(name))
				{
				return t.y;
				}
		
		return null;
	}
	private Tuple<Integer,Vertex> GetNeighbor(String name)
	{
		for(Tuple<Integer,Vertex> x:neighbors)
			if(x.y.equals(name))
				{
				return x;
				}
		
		return null;
	}
	public void RemoveNeighbor(String name)
	{
		
		neighbors.remove(GetNeighbor(name));
	}

	public Integer getWeight(String name ) {
		
		Tuple<Integer,Vertex> t=GetNeighbor(name);
		if(t==null)
			return null;
		return t.x;
		
	}

	public void setWeight(int weight) {
		Tuple<Integer,Vertex> t=GetNeighbor(name);
		if(t==null)
			return ;
		 t.x=weight;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public boolean isVisted() {
		return Visted;
	}

	public void setVisted(boolean visted) {
		Visted = visted;
	}
	
}
