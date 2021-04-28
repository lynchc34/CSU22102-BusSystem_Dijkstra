import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//class to create graph, add edges
//and find shortest path
//by Matthew Dowse 19333534
public class ShortestPath {

	String line = null;
	private int N;		//total number of intersections
	private int S;		//total number of streets
	private EWDGraph graph;

	private double[] distTo;          // distTo[v] = distance  of shortest s->v path
	private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
	private IndexMinPQ<Double> pq;    // priority queue of vertices

	//constructor to populate graph (Bus Routes/city plan)
	//NOTE use "stop_times.txt"
	ShortestPath(String filename){

		N = totalIntersections(filename);	//gets total Intersections N 
		S = totalStreets(filename);
		graph = new EWDGraph(N, S);			//create graph with N intersections and S streets
		String line = null;
		String[] street = {}; 
		String[] street2 = {}; 
		//NOTE NEED TO take into account transfer type/cost/weight etc

		try {

			if(filename != null)
			{
				File f = new File(filename);
				Scanner s1 = new Scanner(f);

				for(int i=0; s1.hasNextLine(); i++)
				{

					if(i==0) continue;			//ignore first line of file with details

					street = line.trim().split(("\\s+"));
					street2 = line.trim().split(("\\s+"));

					if(street[0] == street2[0])	//share the same trip_id
					{
						graph.addEdge(new DirectedEdge(Integer.parseInt(street[4]), Integer.parseInt(street2[4]),
								Double.parseDouble(street[9]))); //edit for weight/cost
					}

				}s1.close();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	//NOTE use "stop_times.txt" to use trip_ID
	public double twoIntStops(int from, int to)
	{
		double distance = 0;

		//get trip_id to check if they share the same trip_id

		dijkstraSP(graph, from);
		distance = distTo(to);

		return distance;
	}

	//get total amount of Intersections
	//NOTE - Might need to edit as some intersections are the same
	//NOTE NOTE might not be needed ^
	//READ IN FILE "stops.txt"
	//i.e from_stop = to_stop
	public int totalIntersections(String filename)
	{
		int totalAmount = 0;

		try {
			if(filename != null)
			{
				File f = new File(filename);
				Scanner s1 = new Scanner(f);

				for(int i=0; s1.hasNextLine(); i++)
				{
					line = s1.nextLine();
					//if statement to check if stop does have a transfer type
					totalAmount++;
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return totalAmount-1;	//return total amount of lines -1 (first line is the title) 
	}


	//get total amount of streets
	//adds 1 to totalAmount for each line
	//remembering to subtract 1 from totalAmount as line 1 = title 
	//READ IN FILE "transfers.txt" as each line is an edge
	public int totalStreets(String filename)
	{
		int totalAmount = 0;

		try {

			if(filename != null)
			{
				File f = new File(filename);
				Scanner s1 = new Scanner(f);

				for(int i=0; s1.hasNextLine(); i++)
				{
					line = s1.nextLine();
					totalAmount++;
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return totalAmount-1;
	}

	//Implementation of Dijkstra's Shortest Path Algorithm
	//Code based from Algorithms 4th Edition by Robert Sedgewick and Kevin Wayne
	//https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/DijkstraSP.java.html
	private void dijkstraSP(EWDGraph G, int s) {
		// TODO Auto-generated method stub

		distTo = new double[G.V()];
		edgeTo = new DirectedEdge[G.V()];

		for (int v = 0; v < G.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[s] = 0.0;

		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Double>(G.V());
		pq.insert(s, distTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (DirectedEdge e : G.adj(v))
				relax(e);
		}
	}

	// relax edge e and update pq if changed
	private void relax(DirectedEdge e) {
		int v = e.from(), w = e.to();
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
			else                pq.insert(w, distTo[w]);
		}
	}

	public double distTo(int v) {
		return distTo[v];
	}


}
