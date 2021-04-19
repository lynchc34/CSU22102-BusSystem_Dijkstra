import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream ;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class tripSearch {

	static int arraySize(String filename) throws IOException {
		int answer=0;

		try {
			String charset = "UTF-8";
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(filename), charset)); 
			br.mark(1);
			if (br.read() != 0xFEFF)
				br.reset();
			String line;
			while((line = br.readLine() ) != null) {
				answer++;
			}
		}
		catch(FileNotFoundException e) {

		}
		return answer;
	}

	static String[] textTo2D(String filename, int size) throws IOException {
		String[] answer = new String[size];
		try {
			String charset = "UTF-8";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), charset)); 
			br.mark(1);
			if (br.read() != 0xFEFF) {
				br.reset();
			}
			String line;
			for(int i=0;i<size;i++) {
				answer[i]=br.readLine();
			}
		}
		catch(FileNotFoundException e) {

		}
		return answer;
	}

	static String[][] fullGrid(String[] string,int size){
		int columns = 0;
		String[] arr = string[1].split(",");
		columns = arr.length+1;
		String[][] grid = new String[size][columns];
		for(int i=0;i<size;i++) {
			string[i]=string[i].replaceAll(", ", ",");
			arr = string[i].split(",");
			/*for(int k=0;k<arr.length-1;k++) {
				arr[k]= arr[k].replaceAll(", ", "");
			}*/
			for(int j=0;j<arr.length;j++) {
				grid[i][j] = arr[j];
			}
		}
		return grid;
	}
	
	static boolean validArrivalTime(String[][] grid, int size, String input) {
		for(int i=0;i<size;i++) {
			int j=1;
			String str = grid[i][j];
			if(str != null && str.equals(input)) {
				return true;
			}
		}
		return false;
	}

	static String getStopID(String[] string,String[] textStops,String[][] gridStopTimes, String[][] gridStops, int stopTimesSize,int stopsSize, String input) {
		String answer="";
		int duplicate=1;
		for(int i=0;i<stopTimesSize;i++) {
			int j=1;
			String str = gridStopTimes[i][j];
			if(str != null && str.equals(input)) {
				String[] test;
				str = gridStopTimes[i][3];
				test = stopData(textStops, gridStops, str,stopsSize);
				answer = "Match number "+ duplicate+ " to arrival time " + input+" is:\nStop ID: " + str + "\n"+ "Stop Code: "+ test[0] +
						"\nStop Name: "+test[1]+"\nStop Destination: "+test[2]+"\nStop Latitude: "+test[3]+"\nStop Longitude: "+ test[4]+
						"\nZone ID: "+ test[5];
				return answer;
			}
		}
		return null;
	}

	static String[] stopData(String[] textStops,String[][] gridStops, String stopID, int stopsSize) {
		String[] arr = textStops[1].split(",");
		int columns = arr.length+1;
		String[] stopInfo=new String[columns];
		
		for(int i=0;i<stopsSize;i++) {
			int j=0;
			String ID = gridStops[i][j];
			if(ID != null && ID.equals(stopID)) {
				for(int k=0;k<columns-1;k++) {
					stopInfo[k]=gridStops[i][k+1];
				}

			}
		}
		return stopInfo;
	}
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		//stop times
		String stopTimes="stop_times.txt";
		int sizeStopTimes = arraySize(stopTimes);
		//System.out.println("Size: "+sizeStopTimes);
		String[] textStopTimes = textTo2D(stopTimes, sizeStopTimes);
		//System.out.println("First Line: "+ textStopTimes[1]);
		String[][] gridStopTimes = fullGrid(textStopTimes, sizeStopTimes);
		//System.out.println("At point [1][1]: " + gridStopTimes[1][0]);

		//stops
		String stops="stops.txt";
		int sizeStops = arraySize(stops);
		//System.out.println("Size: "+sizeStops);
		String[] textStops = textTo2D(stops, sizeStops);
		//System.out.println("First Line: "+ textStops[3]);
		String[][] gridStops = fullGrid(textStops, sizeStops);
		//System.out.println("At point [1][1]: " + gridStops[1][1]);

		String answer,anotherOne;
		boolean valid=false;
		while(!valid) {
			answer ="";
			System.out.println("Enter your arrival time in 24hr format now (hh:mm:ss): ");
			answer = scan.nextLine();
			answer = answer.replaceAll(" ", "");
			System.out.println("you entered: " + answer);
			valid = validArrivalTime(gridStopTimes,sizeStopTimes,answer);
			System.out.println(gridStopTimes[1][1]);
			if(valid==true) {
				System.out.println("Yay! Arrival time is valid!\n ");
				String stopInformation = getStopID(textStops,textStopTimes,gridStopTimes, gridStops, sizeStopTimes,sizeStops, answer);
				System.out.println(stopInformation);
				System.out.println("\nDo you wish to check another arrival time?(yes or no)");
				anotherOne = scan.nextLine();
				anotherOne.toLowerCase();
				anotherOne = anotherOne.replaceAll(" ", "");
				if(anotherOne.equals("yes")) {
					valid=false;
				}
				else {
					System.out.println("No Problem! Have a nice Day! ");
					System.exit(1);
				}
			}
			else {
				System.out.println("Sorry, there are no arrival times that suit what you entered, try again!");
			}
		}
		 
	}

}
