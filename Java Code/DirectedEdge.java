//Directed Edge API
//Code based from Algorithms 4th Edition by Robert Sedgewick and Kevin Wayne
//https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/DirectedEdge.java.html
public class DirectedEdge {
	
	private final int v;
	private final int w;
	private final double weight;
//	private final int transferType;
	
	//edited to add transfer type
	// taking into account "transfers.txt"
	public DirectedEdge(int v, int w, double weight) {
		this.v = v;
		this.w = w;
		this.weight = weight;
//		this.transferType = transferType;
	}
	
	public int from() {
		return v;
	}
	
	public int to() {
		return w;
	}
	
	public double weight() {
		return weight;
	}
	

}
