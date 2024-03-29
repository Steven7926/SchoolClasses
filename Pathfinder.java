// Steven Hudson
// NID: st622455
// COP 3503, Summer 2019

import java.io.*;
import java.util.*;

public class Pathfinder
{
	// Used to toggle "animated" output on and off (for debugging purposes).
	private static boolean animationEnabled = false;

	// "Animation" rate (frames per second).
	private static double frameRate = 4.0;

	// Setters. Note that for testing purposes you can call enableAnimation()
	// from your backtracking method's wrapper method if you want to override
	// the fact that the test cases are disabling animation. Just don't forget
	// to remove that method call before submitting!
	public static void enableAnimation() { Pathfinder.animationEnabled = true; }
	public static void disableAnimation() { Pathfinder.animationEnabled = false; }
	public static void setFrameRate(double fps) { Pathfinder.frameRate = frameRate; }

	// Maze constants.
	private static final char WALL       = '#';
	private static final char PERSON     = '@';
	private static final char EXIT       = 'e';
	private static final char BREADCRUMB = '.';  // visited
	private static final char SPACE      = ' ';  // unvisited

	// Takes a 2D char maze and returns true if it can find a path from the
	// starting position to the exit. Assumes the maze is well-formed according
	// to the restrictions above.
	public static boolean solveMaze(char [][] maze, HashSet<String> paths)
	{
		int height = maze.length;
		int width = maze[0].length;
		StringBuilder str = new StringBuilder();
		// The visited array keeps track of visited positions. It also keeps
		// track of the exit, since the exit will be overwritten when the '@'
		// symbol covers it up in the maze[][] variable. Each cell contains one
		// of three values:
		//
		//   '.' -- visited
		//   ' ' -- unvisited
		//   'e' -- exit
		char [][] visited = new char[height][width];
		for (int i = 0; i < height; i++)
			Arrays.fill(visited[i], SPACE);

		// Find starting position (location of the '@' character).
		int startRow = -1;
		int startCol = -1;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				if (maze[i][j] == PERSON)
				{
					startRow = i;
					startCol = j;
				}
			}
		}

		// Let's goooooooo!!
		return solveMaze(maze, visited, startRow, startCol, height, width, str, paths);
	}

	// FInds the paths through the maze.
	private static boolean solveMaze(char [][] maze, char [][] visited,
	                                 int currentRow, int currentCol,
	                                 int height, int width, StringBuilder str, HashSet<String> paths)
	{
		// This conditional block prints the maze when a new move is made.
		if (Pathfinder.animationEnabled)
		{
			printAndWait(maze, height, width, "Searching...", Pathfinder.frameRate);
		}
		// Hooray!
		if (visited[currentRow][currentCol] == 'e')
		{
			if (Pathfinder.animationEnabled)
			{
				for (int i = 0; i < 3; i++)
				{
					maze[currentRow][currentCol] = '*';
					printAndWait(maze, height, width, "Hooray!", Pathfinder.frameRate);

					maze[currentRow][currentCol] = PERSON;
					printAndWait(maze, height, width, "Hooray!", Pathfinder.frameRate);
				}
			}
			// Delete the space at the end of the string and 
			// then add it to the hashset.
			str.deleteCharAt(str.length() - 1);
			paths.add(str.toString());
			str.append(' ');

			// Return false to keep moving
			// through the maze and find other paths.
			return false;
		}
		// Moves: left, right, up, down
		int [][] moves = new int[][] 
		{
			{0, -1}, 
			{0, 1}, 
			{-1, 0}, 
			{1, 0}
		};

		for (int i = 0; i < moves.length; i++)
		{
			int newRow = currentRow + moves[i][0];
			int newCol = currentCol + moves[i][1];

			// Check move is in bounds, not a wall, and not marked as visited.
			if (!isLegalMove(maze, visited, newRow, newCol, height, width))
				continue;

			// Change state. Before moving the person forward in the maze, we
			// need to check whether we're overwriting the exit. If so, save the
			// exit in the visited[][] array so we can actually detect that
			// we've gotten there.
			//
			// NOTE: THIS IS OVERKILL. We could just track the exit position's
			// row and column in two int variables. However, this approach makes
			// it easier to extend our code in the event that we want to be able
			// to handle multiple exits per maze.

			//Append our valid moves to our StringBuilder.
			if (i == 0)
				str.append("l ");
			if (i == 1)
				str.append("r ");
			if (i == 2)
				str.append("u ");
			if (i == 3)
				str.append("d ");

			if (maze[newRow][newCol] == EXIT)
				visited[newRow][newCol] = EXIT;

			maze[currentRow][currentCol] = BREADCRUMB;
			visited[currentRow][currentCol] = BREADCRUMB;
			maze[newRow][newCol] = PERSON;

			// Perform recursive descent.
			if (solveMaze(maze, visited, newRow, newCol, height, width, str, paths))
				return true;

			// Undo state change. Note that if we return from the previous call,
			// we know visited[newRow][newCol] did not contain the exit, and
			// therefore already contains a breadcrumb, so I haven't updated
			// that here.

			// Once we start backtracking, we must begin deleting moves from our 
			// string so as to find a unique path through the maze. 
			maze[newRow][newCol] = BREADCRUMB;
			maze[currentRow][currentCol] = PERSON;
			visited[currentRow][currentCol] = SPACE;
			
			if (str.length() > 1)
			{
				str.deleteCharAt(str.length() - 2);
				str.deleteCharAt(str.length() - 1);
			}

			// This conditional block prints the maze when a move gets undone
			// (which is effectively another kind of move).
			if (Pathfinder.animationEnabled)
			{
				printAndWait(maze, height, width, "Backtracking...", frameRate);
			}
		}

		return false;
	}

	// Returns true if moving to row and col is legal (i.e., we have not visited
	// that position before, and it's not a wall).
	// We also have to check that the move is in bounds of the 2d array.
	private static boolean isLegalMove(char [][] maze, char [][] visited,
	                                   int row, int col, int height, int width)
	{
		if (maze[row][col] == WALL || visited[row][col] == BREADCRUMB)
			return false;

		if (row == (height - 1) || row == 0 || col == (width - 1) || col == 0)
			return false;

		return true;
	}

	// This effectively pauses the program for waitTimeInSeconds seconds.
	private static void wait(double waitTimeInSeconds)
	{
		long startTime = System.nanoTime();
		long endTime = startTime + (long)(waitTimeInSeconds * 1e9);

		while (System.nanoTime() < endTime)
			;
	}

	// Prints maze and waits. frameRate is given in frames per second.
	private static void printAndWait(char [][] maze, int height, int width,
	                                 String header, double frameRate)
	{
		if (header != null && !header.equals(""))
			System.out.println(header);

		if (height < 1 || width < 1)
			return;

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				System.out.print(maze[i][j]);
			}

			System.out.println();
		}
		System.out.println();
		wait(1.0 / frameRate);
	}

	// Read maze from file. This function dangerously assumes the input file
	// exists and is well formatted according to the specification above.
	private static char [][] readMaze(String filename) throws IOException
	{
		Scanner in = new Scanner(new File(filename));

		int height = in.nextInt();
		int width = in.nextInt();

		char [][] maze = new char[height][width];

		// After reading the integers, there's still a new line character we
		// need to do away with before we can continue.

		in.nextLine();

		for (int i = 0; i < height; i++)
		{
			// Explode out each line from the input file into a char array.
			maze[i] = in.nextLine().toCharArray();
		}
		return maze;		
	}

	// Returns a hashset of possible paths through the maze.
	public static HashSet<String> findPaths(char[][] maze)
	{
		HashSet<String> paths = new HashSet<>();
		solveMaze(maze, paths);
		return paths;
	}
	
	// Returns the difficulty rating
	public static double difficultyRating()
	{
		return 4.1;
	}

	// Returns the hours spent on the assignment
	public static double hoursSpent()
	{
		return 23.5;
	}
}
