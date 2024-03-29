/*
Name: Steven Hudson
Course: CNT 4714 Spring 2020
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 12, 2020
Class: Driver
*/

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Driver 
{
    static String configFile = "config.txt";
	public static void main(final String[] args) throws FileNotFoundException 
	{
		File file = new File(configFile);
		Scanner scan = new Scanner(file);
		int stationCount = Integer.parseInt(scan.nextLine());
		HashMap<Integer, Integer> statNumWork = new HashMap<>();	
        ArrayList<Conveyer> conveyers = new ArrayList<>(stationCount);
		ArrayList<Station> stations = new ArrayList<>(stationCount);
		ExecutorService ship = Executors.newFixedThreadPool(stationCount);
		PrintStream fileOutput = new PrintStream("./output.txt");
		System.setOut(fileOutput);
		
		for (int i = 0; i < stationCount; i++)
			conveyers.add(new Conveyer(i));												// Add Conveyers
			
		for (int i = 0; i < stationCount; i++) 
		{
            int amountOfWork = Integer.parseInt(scan.nextLine());
            statNumWork.put(i, amountOfWork);											
			stations.add(new Station(i, statNumWork.get(i), stationCount));				// Add Station
			stations.get(i).setInCon(conveyers.get(stations.get(i).getInCon()));		// Set the input Conveyer of the Station
            stations.get(i).setOutCon(conveyers.get(stations.get(i).getOutCon()));		// Set the output Conveyer of the Station
			try
			{
				ship.execute(stations.get(i));
			}
			catch (final Exception e)
			{
                System.out.println("Something wrong here, shipping cannot execute.");
			}
        }	
        scan.close();	
		ship.shutdown();	
	}
}