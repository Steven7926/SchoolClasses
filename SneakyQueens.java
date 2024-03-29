// Steven Hudson 
// NID: st622455
// COP 3503
// University of Central Florida

import java.util.*;

public class SneakyQueens
{
    // Checks if the Queens in the Array List are safe or not
    public static boolean allTheQueensAreSafe(ArrayList<String> coordinateStrings, int boardSize)
    {     
            // Initialize arrays to be the boards size
            boolean[] upperDiagonals = new boolean[boardSize + boardSize];
            boolean[] downDiagonals = new boolean[(boardSize + boardSize) + 1];
            boolean[] rows = new boolean[boardSize + 1];
            boolean[] columns = new boolean[boardSize + 1];
            int i;

            for (i = 0; i<coordinateStrings.size(); i++)
            {
                try 
                {
                    int columnAddress = convertColumns(coordinateStrings.get(i));
                    int rowAddress = convertRows(coordinateStrings.get(i));
                    int upperDiagAddress = convertUpDiags(rowAddress, columnAddress, boardSize);
                    int downDiagAddress = convertDownDiags(rowAddress, columnAddress);

                    // Handles diagonals hopefully
                    if (!upperDiagonals[upperDiagAddress])
                        upperDiagonals[upperDiagAddress] = true;
                    else
                        return false;

                    if (!downDiagonals[downDiagAddress])
                        downDiagonals[downDiagAddress] = true;
                    else
                        return false;

                    // When set to one, none of the other queens can go in that row, otherwise attack will commence
                    if (!rows[rowAddress])
                        rows[rowAddress] = true; 
                    else
                        return false;

                    // When set to one, none of the other queens can go in that column, otherwise attack will commence
                    if (!columns[columnAddress]) 
                        columns[columnAddress] = true;
                    else 
                        return false;
                }
                
                catch (ArrayIndexOutOfBoundsException e) 
                {
                    System.out.println("Check Arrays, the index is out of bounds.");
                }                                              
            }
        // Looped through all the addresses in the ArrayList, and none of them returned false, so return true
        return true; 
    }

    // Returns an address for down diagonals
    public static int convertDownDiags(int rowAddress, int columnAddress)
    {
        int downDiagAddy = rowAddress + columnAddress;
        return downDiagAddy;
    }

    // Returns the address for upper diagonals
    public static int convertUpDiags(int rowAddress, int columnAddress, int boardSize)
    {
        int upDiagAddy =  (rowAddress - columnAddress) + boardSize;
        return upDiagAddy;
    }

    // This will pull the letters from the string and then convert the letters to the respective integer
    public static int convertColumns(String coordinateStrings)
    {
        String colAddy = new String();
        int i;
        for (i = 0; i<coordinateStrings.length(); i++)
        {
            if(Character.isLetter(coordinateStrings.charAt(i)) == true)
            {
                colAddy += coordinateStrings.charAt(i);
            }            
        }
        int columnNum = 0;
        try
        {
            for (i = 0; i<colAddy.length(); i++)
            {
                columnNum *= 26;
                columnNum += colAddy.charAt(i) - 'a' + 1;
            }
        }
        catch(ArithmeticException e)
        {
            System.out.println("Column string could not be converted to an integer; ArithmeticException");
        }
        
        return columnNum;
    }

    // This is going to return the rows as integer instead of a string
    public static int convertRows(String coordinateStrings)
    {
        String rowAddy = new String();
        int i;
        for (i = 0; i<coordinateStrings.length(); i++)
        {
            if (Character.isLetter(coordinateStrings.charAt(i)) == false)
            {
                rowAddy += coordinateStrings.charAt(i);
            }
        }
        int rowNum = Integer.parseInt(rowAddy);

        return rowNum;
    }

    public static double difficultyRating()
    {
        return 4.8;
    }

    public static double hoursSpent()
    {
        return 31;
    }
}



