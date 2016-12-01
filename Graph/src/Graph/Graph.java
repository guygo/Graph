package Graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import application.Tuple;

public class Graph {

	private LinkedList<Vertex> vertexes;

	public Graph() {
		vertexes = new LinkedList<Vertex>();
	}

	public LinkedList<Vertex> getVertexes() {
		return vertexes;
	}

	public void AddDirectedEdge(Vertex v1, Vertex v2,int x) {

		v1.AddNeighbor(0, v2);

	}

	public void AddVertex(Vertex v) {
		vertexes.add(v);
	}

	public void RemoveVertex(String name) {
		vertexes.remove(name);
	}

	public Vertex GetVertex(String name) {
		for (Vertex x : vertexes)
			if (x.name.equals(name))
				return x;
		return null;
	}

	public void AddUnDirectedEdge(Vertex v1, Vertex v2,int x) {
		v1.AddNeighbor(x, v2);
		v2.AddNeighbor(x, v1);

	}

	public void RemoveUnDirectedEdge(Vertex v1, Vertex v2) {
		v1.RemoveNeighbor(v2.name);
		v2.RemoveNeighbor(v1.name);

	}

	public void RemoveDirectedEdge(Vertex v1, Vertex v2) {
		v1.RemoveNeighbor(v2.name);

	}

	public Graph Bfs(String start) {
		Graph g = new Graph();

		Queue<Vertex> queue = new LinkedList<Vertex>();

		for (Vertex v : vertexes) {
			v.setRank(Integer.MAX_VALUE);

		}
		Vertex vtemp = GetVertex(start);
		 if(vtemp==null)
	        	return null;
		vtemp.setRank(0);

		queue.add(vtemp);
		g.vertexes.add(new Vertex(vtemp.name, vtemp.getRank() + 1));
		Vertex new_v;

		while (!queue.isEmpty()) {
			vtemp = queue.poll();

			for (Tuple<Integer, Vertex> t : vtemp.getNeighbors()) {
				new_v = t.y;
				if (new_v.getRank() == Integer.MAX_VALUE) {
					new_v.setRank(vtemp.getRank() + 1);

					g.vertexes.add(new Vertex(new_v.name, vtemp.getRank() + 1));
					g.AddDirectedEdge(g.GetVertex(vtemp.name), g.GetVertex(new_v.name),0);

					queue.add(new_v);
				}
			}

		}

		return g;

	}

	public Graph Dfs(String name) {
		Stack<Vertex> stack = new Stack<Vertex>();
		Stack<Vertex> parents = new Stack<Vertex>();
		Graph g = new Graph();
		for (Vertex v : vertexes) {
			v.setVisted(false);
		}
		Vertex v = GetVertex(name);
        if(v==null)
        	return null;
		g.vertexes.add(new Vertex(v.name, 0));
		stack.add(v);
		v.setVisted(true);
		int counter = 0;
		while (!stack.isEmpty()) {
			Vertex element = stack.pop();
			element.setVisted(true);
			counter = 0;
			LinkedList<Tuple<Integer, Vertex>> temp = element.getNeighbors();

			for (Tuple<Integer, Vertex> t : temp) {
				if (!t.y.isVisted()) {
					t.y.papa = element;
					stack.add(t.y);
					g.vertexes.add(new Vertex(t.y.name, element.getRank() + 1));
					g.AddDirectedEdge(g.GetVertex(element.name), g.GetVertex(t.y.name),0);
					parents.add(t.y.papa);
					counter++;
					break;
				}
			}
			if (stack.isEmpty()) {

				if (!parents.isEmpty()) {
					if (counter == 0)

						stack.push(parents.pop());
					else
						stack.push(parents.peek());
				}
			}

		}
		// g.vertexes.add(new Vertex(element.name, element.getRank() + 1));
		// g.AddDirectedEdge(g.GetVertex(element.name), g.GetVertex(n.name));
		return g;

	}

}
