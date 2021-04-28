import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPath {

	private String from;
	private String to;
	String line = null;
	private int N;		//total number of intersections
	private int S;		//total number of streets
	private EWDGraph graph;

	//constructor to populate graph (Bus Routes/city plan)
	ShortestPath(String filename){

		try {

			if(filename != null)
			{
				File f = new File(filename);
				Scanner s1 = new Scanner(f);

				N = totalIntersections(filename);
				S = totalStreets(filename);

				graph = new EWDGraph(N, S);			//create graph with N intersections and S streets

			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}

	}


	//user inputs two bus stops
	//checks if they are on the same line/ trip I.D
	public String twoStops(String from, String to)
	{

		this.from = from;
		this.to = to;

		return to;

	}

	//get total amount of Intersections
	//NOTE - Might need to edit as some intersections are the same
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
	
	

}
