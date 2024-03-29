// Steven Hudson
// NID: st622455
// COP 3503, Summer 2019

import java.util.*;
import java.awt.Point;

public class SneakyKnights
{
    // Checks if the all the knights in the array list are safe from attack
    // and returns true if they are.
    public static boolean allTheKnightsAreSafe(ArrayList<String> coordinateStrings, int boardSize)
    {
        // To achieve the goal, use a HashSet that stores points,
        // as (x, y) coordinates.
        // This can be used to identify the current knights point
        // as well as the knights current attack range.
        HashSet<Point> knightPoints = new HashSet<Point>(); 

        for (int i = 0; i < coordinateStrings.size(); i++)
        {
            int columnAddress = convertColumns(coordinateStrings.get(i));
            int rowAddress = convertRows(coordinateStrings.get(i));
            Point current = new Point(columnAddress, rowAddress);
            boolean isKnight = knightPoints.contains(current);
           
            if (isKnight)
                return false; 

            knightPoints.add(current);

            // Start Checking all possibilities around the current knight.
            // I have to check every move and ensure it wouldn't fall off the board. 
            if ((columnAddress + 1) <= boardSize && (rowAddress + 2) <= boardSize)
            {
                Point upperRight = new Point(columnAddress + 1, rowAddress + 2);
                boolean isUpperRight = knightPoints.contains(upperRight);
                if (isUpperRight)
                    return false;
            }
            if ((columnAddress - 1) > 0 && (rowAddress + 2) <= boardSize)
            {
                Point upperLeft = new Point(columnAddress - 1, rowAddress + 2);
                boolean isUpperLeft = knightPoints.contains(upperLeft);
                if (isUpperLeft)
                    return false;
            }
            if ((columnAddress - 2) > 0 && (rowAddress + 1) <= boardSize)
            {
                Point leftUp = new Point(columnAddress - 2, rowAddress + 1);
                boolean isLeftUp = knightPoints.contains(leftUp);
                if (isLeftUp)
                    return false;
            }
            if ((columnAddress - 2 ) > 0 && (rowAddress - 1) > 0)
            {
                Point leftDown = new Point(columnAddress - 2, rowAddress - 1);
                boolean isLeftDown = knightPoints.contains(leftDown);
                if (isLeftDown)
                    return false; 
            }
            if ((columnAddress + 2) <= boardSize && (rowAddress + 1) <= boardSize)
            {
                Point rightUp = new Point(columnAddress + 2, rowAddress + 1);
                boolean isRightUp = knightPoints.contains(rightUp);

                if (isRightUp)
                    return false;
            }
            if ((columnAddress + 2) <= boardSize && (rowAddress - 1) > 0)
            {
                Point rightDown = new Point(columnAddress + 2, rowAddress - 1);
                boolean isRightDown = knightPoints.contains(rightDown);
                if (isRightDown)
                    return false;
            }
            if ((columnAddress + 1) <= boardSize && (rowAddress - 2 > 0))
            {
                Point downRight = new Point(columnAddress + 1, rowAddress - 2);
                boolean isDownRight = knightPoints.contains(downRight);
                if (isDownRight)
                    return false;
            }
            if ((columnAddress - 1) > 0 && (rowAddress - 2) > 0)
            {
                Point downLeft = new Point(columnAddress - 1, rowAddress -2);
                boolean isDownLeft = knightPoints.contains(downLeft); 
                if (isDownLeft)
                    return false;  
            }   
        }

        return true; 
    }

    // Takes a string from the array list and turns it into the column address as an integer
    public static int convertColumns(String coordinateString)
    {
        StringBuilder columnAddy = new StringBuilder();
        for (int i = 0; i < coordinateString.length(); i++)
        {
            if (Character.isLetter(coordinateString.charAt(i)) == true)
                columnAddy.append(coordinateString.charAt(i)); 
        }
        int columnNum = 0; 
            for (int i = 0; i < columnAddy.length(); i++ )
            {
                columnNum *= 26;
                columnNum += columnAddy.charAt(i) - 'a' + 1;
            }     
        return columnNum;
    }

    // Takes a string from the array list and turns it into the rows address as an integer
    public static int convertRows(String coordinateString)
    {
        StringBuilder rowAddy = new StringBuilder();
        
        for (int i = 0; i < coordinateString.length(); i++){
            if (Character.isLetter(coordinateString.charAt(i)) == false)
                rowAddy.append(coordinateString.charAt(i));
        }
        int rowNum = 0;
        for (int i = 0; i < rowAddy.length(); i++)
        {
            rowNum *= 10;
            rowNum += rowAddy.charAt(i) - '0';
        } 
        return rowNum;
    }

    // Returns the difficulty rating
    public static double difficultyRating()
    {
        return 3.9;
    }

    // Returns the hours spent on the assignment
    public static double hoursSpent()
    {
        return 18;
    }
}
