
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class sim
{
    static int M;
    static int N;
    static int initialState;
    static int gloHisReg;
    static float outcomeCount;
    static float missRatio;
    static float mispredictions;

    public static int convertHextoInt(String address)
    {
        double addy = 0;
        int i;
        int val = 0;
        int len;
        char[] addressChar = address.toCharArray();
        double withPow;

        len = address.length() - 3;

        for (i = 2; i < address.length(); i++)
        {
            if (addressChar[i] >= '0' && addressChar[i] <= '9')
                val = addressChar[i] - 48;
            else if (addressChar[i] >= 'a' && addressChar[i] <= 'f')
                val = addressChar[i] - 87;
            
            withPow = val * Math.pow(16, len);
            addy = addy + withPow;
            len--;
        }
        return (int)addy;
    }

    public static void simulateGshare(String address, String out, int mTable[])
    {
        int hexAddress = convertHextoInt(address);
               
        hexAddress = hexAddress/4;

        int mIndex = hexAddress % ((int)Math.pow(2, M));
        int nExt = gloHisReg << (M - N);
        int index = mIndex ^ nExt;
        int prediction  = mTable[index];

        outcomeCount++;

        if (out.equals("n"))
        {
          if (prediction == 0)
          {
            mTable[index] = prediction;
            gloHisReg = gloHisReg >> 1;
          }
          else if (prediction == 1)
          {
            mTable[index] = prediction - 1;
            gloHisReg = gloHisReg >> 1;
          }
          else if (prediction == 2 || prediction == 3)
          {
            mispredictions++;
            mTable[index] = prediction - 1;
            gloHisReg = gloHisReg >> 1;
          }
        }
        else if (out.equals("t"))
        {
          if (prediction == 3)
          {
            mTable[index] = prediction;
            gloHisReg = gloHisReg >> 1;
            gloHisReg = gloHisReg ^ (int)Math.pow(2, N - 1);
          }
          else if (prediction == 2)
          {
            mTable[index] = prediction + 1;
            gloHisReg = gloHisReg >> 1;
            gloHisReg = gloHisReg ^ (int)Math.pow(2, N - 1);
          }
          else
          {
            mispredictions++;
            mTable[index] = prediction + 1;
            gloHisReg = gloHisReg >> 1;
            gloHisReg = gloHisReg ^ (int)Math.pow(2, N - 1);
          }
        }    
    }

    static void printStats()
    {
      missRatio = (mispredictions/outcomeCount) * 100;
      System.out.println("M: " + M + ", N: " + N);
      System.out.printf("Misprediction ratio: %.2f%%", missRatio);
    }

    public static void main(String args []) throws FileNotFoundException
    {
       int GPB = Integer.parseInt(args[1]);
       int RB = Integer.parseInt(args[2]);   
       String fileString = args[3];
       File file = new File(fileString);
       Scanner scan = new Scanner(file);

       M = GPB;
       N = RB;
       initialState = 2;
       gloHisReg = 0;
       outcomeCount = 0;
       missRatio = 0;
       mispredictions = 0;

       int numberOfEntries = (int)Math.pow(2, M);
       int mTable [] = new int[numberOfEntries];

       Arrays.fill(mTable, initialState);

       while (scan.hasNext())
        {
            String line = scan.nextLine();
            String split[] = line.split(" ");
            String out = split[1];
            String address = split[0];
            simulateGshare(address, out, mTable);         
        }
        scan.close();     
        printStats(); 
    }




}
