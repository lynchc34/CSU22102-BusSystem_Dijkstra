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

	static String line = null;
	private int N; // total number of intersections
	private int S; // total number of streets
	private static EWDGraph graph;

	private static double[] distTo; // distTo[v] = distance of shortest s->v path
	private static DirectedEdge[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
	private static IndexMinPQ<Double> pq; // priority queue of vertices
/*
	public static void main(String args[]) {
		int fromStop = 379;
		int toStop = 1281;

		// String output = "Shortest Path has cost of " + userInput(fromStop, toStop) +
		// " and the stops on that route are " + getListOfStops(fromStop,toStop);

		String stops = "stops.txt";
		String transfers = "transfers.txt";
		String stopTimes = "stop_times.txt";

		System.out.println(totalStops(stops)); // 8757 stops
		System.out.println(totalStreets(transfers, stopTimes)); // 863646 edges
		//
		// System.out.println(getListOfStops(fromStop, toStop, stopTimes));

		 System.out.println(output(fromStop, toStop, stopTimes));

	}*/

	// constructor to populate graph (Bus Routes/city plan)
	// filename = "stop_times.txt"
	// filename2 = "transfers.txt"
	ShortestPath(String filename, String filename2, String filename3) {

		String line = null;
		String[] street = {};
		String[] street2 = {};

		String stops = filename;
		String transfers = filename2;
		String stopTimes = filename3;
		String str;
		String str2 = "";
		N = totalStops(stops);
		//System.out.println(N);
		//int temp = totalStreets2(stopTimes);
		S = totalStreets(transfers, stopTimes);
		//System.out.println(S);
		graph = new EWDGraph(N, S); // create graph with N intersections and S streets

		// NOTE NEED TO take into account transfer type/cost/weight etc

		// NOTE use "stop_times.txt"
		try {

			if (stopTimes != null) {
				File f = new File(stopTimes);
				Scanner s1 = new Scanner(f);
				Scanner s2 = new Scanner(f);
				
				for (int i = 0; s1.hasNextLine(); i++) {
					if (i == 0) {
						str = s1.nextLine(); // load title
						  str2 = s2.nextLine();    //load title line
	                      str2 = s2.nextLine();    //load 1 ahead of s2
						// line = s1.nextLine(); //load title
					}
					
					str = s1.nextLine();        //get to line 2
                    street = str.split(",");    //split
                    
                    if(s2.hasNextLine())        //checks as it will +1 ahead
                    {
                    str2 = s2.nextLine();        //move to one ahead
                    }
                    street2 = str2.split(","); 
						// for stop_times.txt
						if (street[0] == street2[0]) // if share the same trip_id then add edge
						{
							System.out.println(street[3]);
							graph.addEdge(
									new DirectedEdge(Integer.parseInt(street[3]), Integer.parseInt(street2[3]), 1.00)); // edit
																														// for
																														// weight/cost
						}
					
					/*
				   for(int i=0; s2.hasNextLine(); i++)
                {
                    
                    if(i==0)//if first line (title)
                    {    
                        line = s2.nextLine();     //load title line
                        line2 = s3.nextLine();    //load title line
                        line2 = s3.nextLine();    //load 1 ahead of s2
                    }
                    line = s2.nextLine();        //get to line 2
                    street = line.split(",");    //split
                    
                    if(s3.hasNextLine())        //checks as it will +1 ahead
                    {
                    line2 = s3.nextLine();        //move to one ahead
                    }
                    street2 = line2.split(","); 
					 */

				}
				s1.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// transfers.txt
		try {

			if (transfers != null) {
				File f = new File(transfers);
				Scanner s1 = new Scanner(f);
				
				for (int i = 0; s1.hasNextLine(); i++) {
					if (i == 0) {
						str = s1.nextLine();
					}
					str = s1.nextLine();
					street = str.split(","); // get first lines details

					if (Integer.parseInt(street[2]) == 0) // if transfer type = 0
					{
						graph.addEdge(new DirectedEdge(Integer.parseInt(street[0]), Integer.parseInt(street[1]), 2.00));
					} else if (Integer.parseInt(street[2]) == 2) // if transfer type = 2
					{
						double weight = Double.parseDouble(street[3]) / 100;

						graph.addEdge(
								new DirectedEdge(Integer.parseInt(street[0]), Integer.parseInt(street[1]), weight));
					}
				}
				s1.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	// User puts in from and to
	// NOTE use "stop_times.txt" to use trip_ID
	// returns distance
	public static double userInput(int from, int to) {
		double distance = 0;
		
		dijkstraSP(graph, from); // computes shortest path
		distance = distTo(to);

		return distance;
	}

	// call this method to return Shortest Path cost and stops on route
	public static String output(int from, int to, String filename) {
		String output = "Shortest Path has cost of " + userInput(from, to);
		// + " and the stops on that route are " + getListOfStops(from,to, filename);

		return output;
	}

	// get total amount of Bus Stops
	// adds 1 to totalAmount for each line
	// remembering to subtract 1 from totalAmount as line 1 = title
	// READ IN FILE "stops.txt"
	public static int totalStops(String filename) {
		int totalAmount = 0;

		try {
			if (filename != null) {
				File f = new File(filename);
				Scanner s1 = new Scanner(f);

				for (int i = 0; s1.hasNextLine(); i++) {
					line = s1.nextLine();
					totalAmount++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return totalAmount - 1;
	}

	// get total amount of streets (Edges)
	// adds 1 to totalAmount for each line
	// remembering to subtract 1 from totalAmount as line 1 = title
	// READ IN FILE "transfers.txt" as each line is an edge,
	// and READ IN FILE "stop_times.txt"
	// but only add 1 to count if they are consecutive and share the same trip_id
	public static int totalStreets(String filename, String filename2) {
		int totalAmount = 0;
		String[] street = {};
		String[] street2 = {};
		String line = null;

		// transfers.txt
		try {

			if (filename != null) {
				File f = new File(filename);
				Scanner s1 = new Scanner(f);
				String num;
				for (int i = 0; s1.hasNextLine(); i++) {
					num = s1.nextLine();
					totalAmount++;
				}
			}
			totalAmount = totalAmount - 1;
			System.out.println(totalAmount);
			// subtract one for the title
			int two = totalStreets2(filename2);
			System.out.println(two);
			return totalAmount + two;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return totalAmount;

	}

	public static int totalStreets2(String filename2)
    {
        //stop_times.txt
        int totalAmount = 0;
        String[] street = {}; 
        String[] street2 = {}; 
        String line = null;
        String line2 = null;

        try {

            if(filename2 != null)
            {
                File f2 = new File(filename2);
                Scanner s2 = new Scanner(f2);
                Scanner s3 = new Scanner(f2);

                for(int i=0; s2.hasNextLine(); i++)
                {
                    
                    if(i==0)//if first line (title)
                    {    
                        line = s2.nextLine();     //load title line
                        line2 = s3.nextLine();    //load title line
                        line2 = s3.nextLine();    //load 1 ahead of s2
                    }
                    line = s2.nextLine();        //get to line 2
                    street = line.split(",");    //split
                    
                    if(s3.hasNextLine())        //checks as it will +1 ahead
                    {
                    line2 = s3.nextLine();        //move to one ahead
                    }
                    street2 = line2.split(",");    //split
                    
                    if(Integer.parseInt(street[0]) == Integer.parseInt(street2[0])) //if they share the same trip_id
                    {
                        totalAmount++;
                    }
                }
            }
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        } 

        return totalAmount;
    }

	// takes in from and to stop id's
	// returns stops id's for stops between from and stop
	// NOTE USE stop_times.txt
	public static List<String> getListOfStops(int from, int to, String filename) {
		List<String> list = new ArrayList<String>();
		String[] street = {};
		String[] street2 = {};

		try {

			File f = new File(filename);
			Scanner s1 = new Scanner(f);
			String str;
			// String str2;
			for (int i = 0; s1.hasNextLine(); i++) {
				if (i == 0) {
					line = s1.nextLine(); // load title
					// line = s1.nextLine(); //load title
				}
				str = s1.nextLine();
				street = str.split(",");
				System.out.println(street[3]);
				if (Integer.parseInt(street[3]) == from) {
					while ((Integer.parseInt(street[3]) != to)) {
						str = s1.nextLine();

						street = str.split(",");
						String toAdd = street[3];
						list.add(toAdd); // add stop_id to list
					}
					return list;
				}

			}

			/*
			 * for(int i=0; s1.hasNextLine(); i++) { if(i==0) { line = s1.nextLine(); //load
			 * title //line = s1.nextLine(); //load title }
			 * 
			 * 
			 * line = s1.nextLine(); street = line.split(","); //get first lines details
			 * 
			 * 
			 * Scanner s2 = new Scanner(f); //find first stop "from" and keep adding to the
			 * list until you reach "to"
			 * 
			 * if((Integer.parseInt(street[3]) == from) && (Integer.parseInt(street[3]) !=
			 * to)) { line = s1.nextLine(); street2 = line.split(",");
			 * 
			 * String toAdd = street2[3]; list.add(toAdd); //add stop_id to list } return
			 * list; //return here so that it does not add extra stops, (time effiency)?
			 */

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	// Implementation of Dijkstra's Shortest Path Algorithm
	// Code based from Algorithms 4th Edition by Robert Sedgewick and Kevin Wayne
	// https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/DijkstraSP.java.html
	private static void dijkstraSP(EWDGraph G, int s) {
		// TODO Auto-generated method stub
		// System.out.println(G.V());
		
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
	private static void relax(DirectedEdge e) {
		int v = e.from(), w = e.to();
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
			if (pq.contains(w))
				pq.decreaseKey(w, distTo[w]);
			else
				pq.insert(w, distTo[w]);
		}
	}

	public static double distTo(int v) {
		return distTo[v];
	}

}
