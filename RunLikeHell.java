// Steven Hudson
// NID: st622455
// COP 3503, Summer 2019

import java.util.*;

public class RunLikeHell
{
    // Gets the maximum amount of knowledge the algorithmist can gain while
    // running like hell. 
    public static int maxGain(int [] blocks)
    {
        // Check if the array is null first, this hopefully
        // will prevent against any null pointer exceptions.
        if (blocks == null)
            return 0;

        // Length of the input array never changes, so this prevents us 
        // from calling "blocks.length" everywhere over
        // and over. 
        int length = blocks.length; 

        if (length == 0)
            return 0;
 
        if (length == 1)
            return blocks[0];

        // If there are only 2 blocks, the algorithmist only
        // has the choice between one or the other, so he'll just
        // take the one with more knowledge. 
        if (length == 2)
            return Math.max(blocks[0], blocks[1]);

        int subAns [] = new int [length];

        // The goal of this loop is to build up our sub answers array to contain
        // the maximum amount of knowledge by pushing the max to the end of 
        // the sub answers array. 
        for (int i = 0; i < length; i++)
        {
            if (i == 0)
                subAns[i] = blocks[i];
            
            else if (i == 1)
                subAns[i] = Math.max(blocks[0], blocks[i]);

            // Once we hit index 2 or greater, we fill the sub answers
            // array with the greatest option. This slowly pushes the
            // max value for the algorithmist to get to the end of the array.
            // i.e if blocks = [15, 3, 6, 17, 2, 1, 20] then at i = 2 we need to find 
            // [15, 15, blank, ...], the largest possible value to fill at i = 2 
            // is 15 + 6 = 21. So at i = 2 subProb would be [15, 15, 21, ...].
            else
                subAns[i] = Math.max(subAns[i - 1], (subAns[i - 2] + blocks[i]));
        }

        // The last index in the array will hold the max value.  
        return subAns[length - 1];         
    }

    // Returns the difficulty rating of the assignment.
    public static double difficultyRating()
    {
        return 3.5;
    }

    // Returns the hours spent on the assignment.
    public static double hoursSpent()
    {
        return 10.5;
    }
}