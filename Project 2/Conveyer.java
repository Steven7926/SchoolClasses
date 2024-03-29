/*
Name: Steven Hudson
Course: CNT 4714 Spring 2020
Assignment title: Project 2 – Multi-threaded programming in Java
Date: February 12, 2020
Class: Conveyer
*/

import java.util.concurrent.locks.ReentrantLock;

public class Conveyer 
{
	int conveyorNum;
 	ReentrantLock accessLock = new ReentrantLock();		// Allows to lock the conveyers
	public Conveyer(int numC)
	{
		conveyorNum = numC;
	}
}