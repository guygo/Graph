package Graph;

import java.awt.Point;

public class GraphTest {

	public static void main(String[] args) {
		Graph g = new Graph();

		g.AddVertex(new Vertex("A", Integer.MAX_VALUE));
		g.AddVertex(new Vertex("B", Integer.MAX_VALUE));
		g.AddVertex(new Vertex("C", Integer.MAX_VALUE));
		g.AddVertex(new Vertex("D", Integer.MAX_VALUE));

		//g.AddUnDirectedEdge(g.GetVertex("A"), g.GetVertex("B"));
		//g.AddUnDirectedEdge(g.GetVertex("A"), g.GetVertex("C"));
		//g.AddUnDirectedEdge(g.GetVertex("B"), g.GetVertex("D"));
		//g.AddUnDirectedEdge(g.GetVertex("B"), g.GetVertex("C"));
		//g.AddUnDirectedEdge(g.GetVertex("C"), g.GetVertex("D"));
		Graph gc = g.Dfs("A");

		for (Vertex x : gc.getVertexes()) {
           
			System.out.println(x.name);
		}
	}

}
