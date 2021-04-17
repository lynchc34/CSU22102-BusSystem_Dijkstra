import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShortestPath {

	private String from;
	private String to;
	String line = null;

	//constructor to populate graph (Bus Routes/city plan)
	ShortestPath(String filename){

		try {

			if(filename != null)
			{
				File f = new File(filename);
				Scanner s1 = new Scanner(f);





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
					totalAmount++;
				}
			}
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}


		return totalAmount-1;	//return total amount of lines -1 (first line is the title) 
	}

}
