import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//class to 
//create graph, 
//add edges
//find shortest path
//and return bus stops en route
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
	ShortestPath(String filename, String filename2){

		String line = null;
		String[] street = {}; 
		String[] street2 = {}; 

		N = totalStops(filename);	
		S = totalStreets(filename, filename2);
		graph = new EWDGraph(N, S);			//create graph with N intersections and S streets

		//NOTE NEED TO take into account transfer type/cost/weight etc

		//NOTE use "stop_times.txt"
		try {

			if(filename != null)
			{
				File f = new File(filename);
				Scanner s1 = new Scanner(f);

				for(int i=0; s1.hasNextLine(); i++)
				{
					line = s1.nextLine();//

					if(i==0) continue;						//ignore first line of file with details

					street = line.trim().split(("\\s+"));	//get first lines details
					String previousLine = line;				//to not skip lines
					s1.nextLine();							//move to next line to get new details
					street2 = line.trim().split(("\\s+"));	//get second lines details
					line = previousLine;					//reset to not skip lines

					//for stop_times.txt
					if(street[0] == street2[0])				//if share the same trip_id then add edge
					{
						graph.addEdge(new DirectedEdge(Integer.parseInt(street[4]), Integer.parseInt(street2[4]), 1)); //edit for weight/cost
					}

				}s1.close();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}

		//transfers.txt
		try {

			if(filename2 != null)
			{
				File f = new File(filename2);
				Scanner s1 = new Scanner(f);

				for(int i=0; s1.hasNextLine(); i++)
				{
					line = s1.nextLine();//

					if(i==0) continue;						//ignore first line of file with details

					street = line.trim().split(("\\s+"));	//get first lines details

					if(Integer.parseInt(street[3]) == 0)	//if transfer type = 0
					{
						graph.addEdge(new DirectedEdge(Integer.parseInt(street[0]), Integer.parseInt(street[1]), 2.00));
					}
					else if(Integer.parseInt(street[3]) == 2) //if transfer type = 2
					{
						double weight = Double.parseDouble(street[4])/100;

						graph.addEdge(new DirectedEdge(Integer.parseInt(street[0]), Integer.parseInt(street[1]), weight));
					}
				}s1.close();
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	//User puts in from and to 
	//NOTE use "stop_times.txt" to use trip_ID
	//returns distance
	public double userInput(int from, int to)
	{
		double distance = 0;

		dijkstraSP(graph, from);							//computes shortest path
		distance = distTo(to);								//returns shortest distance
		
		return distance;
	}
	
	//call this method to return Shortest Path cost and stops on route
	public String output(int from, int to)
	{
		String output = "Shortest Path has cost of " + userInput(from, to) +
				" and the stops on that route are " + getListOfStops(from,to);

		return output;
	}

	//get total amount of Bus Stops
	//adds 1 to totalAmount for each line
	//remembering to subtract 1 from totalAmount as line 1 = title 
	//READ IN FILE "stops.txt"
	public int totalStops(String filename)
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

	//get total amount of streets (Edges)
	//adds 1 to totalAmount for each line
	//remembering to subtract 1 from totalAmount as line 1 = title 
	//READ IN FILE "transfers.txt" as each line is an edge, 
	//and READ IN FILE "stop_times.txt"  
	//but only add 1 to count if they are consecutive and share the same trip_id
	public int totalStreets(String filename, String filename2)
	{
		int totalAmount = 0;
		String[] street = {}; 
		String[] street2 = {}; 

		//transfers.txt
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

		totalAmount = totalAmount-1;	//subtract one for the title

		//stop_times.txt
		try {

			if(filename2 != null)
			{
				File f = new File(filename2);
				Scanner s1 = new Scanner(f);

				for(int i=0; s1.hasNextLine(); i++)
				{
					line = s1.nextLine();
					street = line.trim().split(("\\s+"));
					String previousLine = line;				//to not skip lines
					line = s1.nextLine();
					street2 = line.trim().split(("\\s+"));
					line = previousLine;					//reset to not skip a line

					if(street[0] == street2[0]) //if they share the same trip_id
					{
						totalAmount++;
					}
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		} 

		return totalAmount -1;
	}



	//takes in from and to stop id's
	//returns stops id's for stops between from and stop
	//NOTE USE stop_times.txt
	public List<String> getListOfStops(int from, int to)
	{
		List<String> list = new ArrayList<String>();	
		String[] street = {}; 
		String[] street2 = {}; 

		try {

			File f = new File("stop_times.txt");
			Scanner s1 = new Scanner(f);

			for(int i=0; s1.hasNextLine(); i++)
			{
				line = s1.nextLine();
				street = line.trim().split(("\\s+"));	//get first lines details

				//find first stop "from" and keep adding to the list until you reach "to"
				while((Integer.parseInt(street[4]) == from) && (Integer.parseInt(street[4]) != to))  
				{
					line = s1.nextLine();
					street2 = line.trim().split(("\\s+"));
					if(Integer.parseInt(street[0]) == Integer.parseInt(street2[0])) //ensuring they are on the same trip_id
					{
						String toAdd = street2[4];			
						list.add(toAdd);				//add stop_id to list
					}
					
				}
				return list;	//return here so that it does not add extra stops, (time effiency)?
			}

		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
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
