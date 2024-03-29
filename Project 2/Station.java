/*
Name: Steven Hudson
Course: CNT 4714 Spring 2020
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 12, 2020
Class: Station
*/

import java.util.*;

public class Station implements Runnable
{
    Random ran = new Random();
    Conveyer out;
	Conveyer in;
	int allStat;
    int workload;
    int outConveyer;
	int inConveyer;
    int statNum;
    int sleep = ran.nextInt(2000);
    	
	public Station(int curStation, int workAmount, int totalStat)
	{
        statNum = curStation;
		workload = workAmount;
        allStat = totalStat;
        
		if (statNum == 0) 					// If station number is 0 then the inConveyer is 0 as per example
            inConveyer = 0;       
		else								// If station numver greater than 0 then the inConveyer is the station number -1 as per example
            inConveyer = (statNum - 1);
        System.out.println("Station " + statNum + ": In-Connection set to conveyer " + inConveyer + ".");
        
		if (statNum == 0)					// If station number is 0 then the outConveyer is the max number of stations -1 as per example
            outConveyer = (allStat - 1);
		else								// If station number is greater than 0 then the outConveyer is the station number as per example
            outConveyer = statNum;
        System.out.println("Station " + statNum + ": Out-Connection set to conveyer " + outConveyer + ".");      	
		System.out.println("Station " + curStation + ": Workload set. Station " + statNum + " has " + workload + " package groups to move.");
	}
	
	@Override
	public void run() { 	
		while(workload > 0) 	// This loops until the work is complete
		{
			if (in.accessLock.tryLock())  // If we can aquire the input conveyer lock
			{
				System.out.println("Station " + statNum + ": Holds lock on (granted access) to conveyer " + inConveyer + ".");
				if (out.accessLock.tryLock()) // Both in conveyer and out conveyer have aquired locks, we can now do work
				{
					System.out.println("Station " + statNum + ": Holds lock on (granted access) to conveyer " + outConveyer + ".");
					doWork();
					System.out.println("Station " + statNum + ": Has " + workload + " package groups left to move.");
				}
				else	// We cannot do work right now, the output conveyer could not be aquired
				{	
					System.out.println("Station " + statNum + ": Released access to conveyer " + inConveyer + ".");
					in.accessLock.unlock();
					try 						
					{
						Thread.sleep(sleep);			// Sleep the thread by a random amount
					} 
					catch (Exception e) 
					{
						System.out.println("Thread would not sleep.");
					}
				}			
				if (out.accessLock.isHeldByCurrentThread())
				{
					out.accessLock.unlock();			// Unlock the output conveyer
					System.out.println("Station " + statNum + ": Unlocks (released access) to conveyer " + outConveyer + ".");
                }
                if (in.accessLock.isHeldByCurrentThread())
				{
					in.accessLock.unlock();				// Unlock the input conveyer
					System.out.println("Station " + statNum + ": Unlocks (released access) to conveyer " + inConveyer + ".");
                }	    	
                try
                {
                    Thread.sleep(sleep);				// Sleep the thread by a random amount
                }
                catch(Exception a)
                {
                    System.out.println("Thread would not sleep.");
                }
			}
		}	
		System.out.println("\n* * Station " + statNum + ": Workload successfully completed. * *\n"); // All workloads finished, print out the stations work done. 
	}
	
	public void doWork() // Simulates the station doing work, which is really just decreasing the workload
	{	
		workload--;                         // Decrease workload
		System.out.println("Station " + statNum + ": Successfully moves packages on conveyer " + inConveyer + ".");				// prints input conveyer success
        System.out.println("Station " + statNum + ": Successfully moves packages on conveyer " + outConveyer + ".");			// Prints output conveyer success
	}

	public int getInCon() 				// Return the in Conveyer number of the Station thats calculated in Station()
	{
		return inConveyer;
	}
	public int getOutCon() 				// Return the out Conveyer number of the Station thats calculated in Station()
	{
		return outConveyer;
	}
	public void setInCon(Conveyer input) // Set the input Conveyer variable of the Station Class
	{
		in = input;
	}
	public void setOutCon(Conveyer output) // Set the output Conveyer variable of the Station Class
	{
		out = output;
	}
}