//Edge Weighted Directed Graph 
//Code based from Algorithms 4th Edition by Robert Sedgewick and Kevin Wayne
//https://algs4.cs.princeton.edu/44sp/EdgeWeightedDigraph.java.html

public class EWDGraph {

	private final int V;
	private int E;
	private Bag<DirectedEdge>[] adj;
	private int[] indegree; //number of edges directed into a vertex, number of streets in one street(max2?) 

	@SuppressWarnings("unchecked")
	public EWDGraph(int V, int E){
		this.V = V;
		this.E = E;
		this.indegree = new int[V];
		adj = (Bag<DirectedEdge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<DirectedEdge>();
	}

		public void addEdge(DirectedEdge e) {
			int v = e.from();
			int w = e.to();
			validateVertex(v);
			validateVertex(w);
			adj[v].add(e);
			indegree[w]++;
			E++;
		}
	
	public Iterable<DirectedEdge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
	
	public int V() {
		return V;
	}
	
//	public Iterable<DirectedEdge> edges() {
//	Bag<DirectedEdge> list = new Bag<DirectedEdge>();
//	for (int v = 0; v < V; v++) {
//		for (DirectedEdge e : adj(v)) {	
//			list.add(e);
//		}
//	}
//	return list;
//}
	
	
//	public int indegree(int v) {
//    validateVertex(v);
//    return indegree[v];
//}

}

