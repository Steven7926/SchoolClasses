// Steven Hudson
// NID: st622455
// COP 3503, Summer 2019

import java.io.*;
import java.util.*;

public class ConstrainedTopoSort
{
    // Using a boolean matrix to represent the non-sparce,
    // unweighted, directed graph.
    // Declaring length here prevents redundant code, redundant calls
    // in the loops, and allows the last 2 methods to use it.
    private boolean matrix [][];
    private int verticiesN;
    private int length;

    // Opens the file g1.txt and reads it into an adjacency matrix
    // of size 4x4. For g1.txt the matrix will look like this;
    // false false true false
    // false flase true true
    // false false false true
    // false false false false
    public ConstrainedTopoSort(String filename) throws IOException
    {
        Scanner inFile = new Scanner(new File(filename));
        verticiesN = inFile.nextInt();
        matrix = new boolean [verticiesN][verticiesN];

        for (int i = 0; i < verticiesN; i++)
        {
            int edgesN = inFile.nextInt();
            for (int j = 0; j < edgesN; j++)
            {
                int edgeTo = inFile.nextInt() - 1;
                matrix[i][edgeTo] = true; 
            }
        }  
        inFile.close(); 
    }

    // Returns true if it is valid for x to come before y.
    public boolean hasConstrainedTopoSort(int x, int y)
    {
        // Set length for loops to use.
        length = matrix.length;
        // x and y cannot be bigger than length
        // cause then we'd have a non-existant node.
        if (x > length || y > length)
            return false;
        // x cannot equal y
        if (x == y)
            return false;
        // There cannot be a cycle in the graph.
        // We check this below using Dr.Szumlanski's
        // Topological Sort method.
        if (isCycle())
            return false;

        // Implement a Queue using an ArrayDeque
        Queue<Integer> queue = new ArrayDeque<Integer>();
        // Implements an ArrayList for visited nodes. 
        ArrayList<Integer> list = new ArrayList<>();
        // Add x to our visited list and to the queue.
        list.add(x);
        queue.add(x);

        while (!queue.isEmpty())
        {
            x = queue.remove();
            if (matrix[y-1][x-1])
                return false;

            for (int i = 1; i <= length; i++)
            {
                if (matrix[i - 1][x - 1] && !list.contains(i))
                    queue.add(i);
            }
        }
        // We've made it through all scenarios
        // and haven't returned false.
        return true;
    }

    // Using Professor Szumlanski's Topological
    // Sort code, this function determines 
    // if there is a cycle in the graph.
    public boolean isCycle()
    {
        int counter = 0;
        int incomingE [] = new int [length];

        // We first need to get the incoming edges for each vertex.
        for (int i = 0; i < length; i++)
            for (int j = 0; j < length; j++)
                incomingE[j] += (matrix[i][j] ? 1 : 0);

        // Implement a Queue using an ArrayDeque.
        Queue<Integer> queue = new ArrayDeque<Integer>();

        // Add all the nodes with 0 incoming
        // edges to the queue.
        for (int i = 0; i < length; i++)
        {
             if (incomingE[i] == 0)
                queue.add(i);
        }

        while (!queue.isEmpty())
        {
            int node = queue.remove();
            ++counter;
            for (int i = 0; i < length; i++)
            {
                if (matrix[node][i] && --incomingE[i] == 0)
                    queue.add(i);             
            }
        }
        if (counter != length)
            return true;

        return false;
    }  

    // Returns the difficulty rating of the assignment.
    public static double difficultyRating()
    {
        return 2.9;
    }
    // Returns the hours spent on the assignment.
    public static double hoursSpent()
    {
        return 13.5;
    }

}