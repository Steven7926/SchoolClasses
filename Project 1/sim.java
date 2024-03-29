import java.math.BigInteger;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.String;
import java.util.Scanner;
import java.lang.Math; 

public class sim
{
    static int cacheSize;
    static int association;
    static int repPolicy;
    static int writeBack;
    static int num_sets;
    static float hit = 0;
    static float miss = 0;
    static String filesString;
    static String sim;
    static int writesToMem;
    static int readsFromMem;
    
    //Arrays [row][column]
    static BigInteger[][] tag_arr;
    static BigInteger[][] lru;
    static boolean[][] dirty;

    static void parse_stuff(String input)
    {
        String cache = null;
        String assoc = null;
        String rep = null;
        String write = null;
        String[] splitted;

        splitted = input.split(" ");
       
        for (int i = 0; i < splitted.length; i++)
        {
            if (i == 0)
                sim = splitted[i];
            if (i == 1)
                cache = splitted[i];
            if (i == 2)
                assoc = splitted[i];
            if (i == 3)
                rep = splitted[i];
            if (i == 4)
               write = splitted[i];
            if (i == 5)
                filesString = splitted[i];
        }

        cacheSize = Integer.parseInt(cache);
        association = Integer.parseInt(assoc);
        repPolicy = Integer.parseInt(rep);
        writeBack = Integer.parseInt(write);
    }

    public static BigInteger convertHextoInt(String address)
    {
        BigInteger addy = new BigInteger("0");
        int i;
        int val = 0;
        int len;
        char[] addressChar = address.toCharArray();
        double withPow;
        long castPow;

        len = address.length() - 3;

        for (i = 2; i < address.length(); i++)
        {
            if (addressChar[i] >= '0' && addressChar[i] <= '9')
                val = addressChar[i] - 48;
            else if (addressChar[i] >= 'a' && addressChar[i] <= 'f')
                val = addressChar[i] - 87;
            
            withPow = val * Math.pow(16, len);
            castPow = (long)withPow;
            addy = addy.add(BigInteger.valueOf(castPow));
            len--;
        }
        return addy;
    }


    static void simulate_access(String op, BigInteger add)
    {
        BigInteger set = (add.divide(BigInteger.valueOf((long)64))).mod(BigInteger.valueOf((long)num_sets));
        BigInteger tag = add.divide(BigInteger.valueOf((long)64));
        int setInt = set.intValue();
        int i;
        int didhit = 0;

        for (i = 0; i < association; i++)
        {
            if (tag.equals(tag_arr[setInt][i]))
            {
                hit++;
                didhit++;

                if (op.equals("W"))
                {
                    if (writeBack == 0)
                        writesToMem++;
                    else if (writeBack == 1)
                        dirty[setInt][i] = true;                   
                }
                if (op.equals("R"))
                    readsFromMem++;

                if (repPolicy == 0)
                {
                    lru[setInt][i].equals(BigInteger.valueOf(association - 1));
                } 
                i = association;
            }
        }
        if (didhit == 0)
        {
            miss++;
            if (repPolicy == 0)
                    updateLRU(add, i, setInt, tag);
            else if (repPolicy == 1)
                    updateFIFO(add, i, setInt, tag);
        }

    }

    static void updateLRU(BigInteger add, int i, int setInt, BigInteger tag)
    {
        // If its a miss, then one of two things happened, data either does not exist in this spot, or data
        // does exist in this spot and its ***possibly*** been altered

            for (int k = 0; k < association; k++)
            {
                if (tag_arr[setInt][k] == null)
                {
                    tag_arr[setInt][k] = tag;
                    lru[setInt][k] = BigInteger.valueOf(k);
                    k = association;
                }
                else
                {
                    if (lru[setInt][k].equals(BigInteger.valueOf(0))) // Evict the lowest priority (i.e the one with 0 in lru because this is the first to make into the cache)
                    {
                        if (dirty[setInt][k] == true) //If its a miss but there is dirty data there we have to clear the dirty bit and write the data to memory
                        {
                            writesToMem++;
                            dirty[setInt][k] = false;
                        }
                        tag_arr[setInt][k] = tag;
                        lru[setInt][k] = BigInteger.valueOf(association - 1);  
                    }
                    else
                        lru[setInt][k].subtract(BigInteger.valueOf(1));
                }
  
            }

    
    }
    static void updateFIFO(BigInteger add, int i, int setInt, BigInteger tag)
    {
        for (int k = 0; k < association; k++)
        {
            if (tag_arr[setInt][k] == null)
            {
                tag_arr[setInt][k] = tag;  // {[1,2,3,4,5,6,7,8], [1,2,3,4,5,6,7,8], [1,2,3,4,5,6,7,8]} --> 8 - way association
                k = association;
            }
            else
            {
                    if (dirty[setInt][k] == true) // If its a miss but there is dirty data there we have to clear the dirty bit and write the data to memory
                    {
                        writesToMem++;
                        dirty[setInt][k] = false;
                    }
                    tag_arr[setInt][k] = tag;  
            }   
        }
    }

    static void printStats()
    {
        float missRatio;
        if (repPolicy == 0)
        {    
            missRatio = (miss/4)/(miss+hit);

            System.out.println("The total miss ratio for L1 cache: " + missRatio);
            System.out.println("The total misses: " + miss);
            System.out.println("The total hits: " + hit);
            System.out.println("The number of writes to memory: " + writesToMem*2);
            System.out.println("The number of reads from memory: " + readsFromMem/2);
        }
        else if (repPolicy == 1)
        {
            missRatio = (miss)/(miss+hit);

            System.out.println("The total miss ratio for L1 cache: " + missRatio);
            System.out.println("The total misses: " + miss);
            System.out.println("The total hits: " + hit);
            System.out.println("The number of writes to memory: " + writesToMem);
            System.out.println("The number of reads from memory: " + readsFromMem/4);
        }
    }


    public static void main(String args[]) throws FileNotFoundException
    {
        String op = "";
        String input = "";
        String address = "";
        BigInteger add;

        input = args[0];
        parse_stuff(input);
        num_sets = (cacheSize/64)/association;

        File file = new File(filesString);
        Scanner scan = new Scanner(file);

        tag_arr = new BigInteger[num_sets][association];
        lru = new BigInteger[num_sets][association];
        dirty = new boolean[num_sets][association];

        while (scan.hasNext())
        {
            String line = scan.nextLine();
            String split[] = line.split(" ");
            op = split[0];
            address = split[1];
            add = convertHextoInt(address);
            add.shiftRight(6);
            simulate_access(op, add);
        }

       for (int i = 0; i < num_sets; i++)
       {
         System.out.print("[");
           for (int j = 0; j < association; j++)
                System.out.print(lru[i][j] +" ");
         System.out.print("], ");
       }

       System.out.println(" ");

       for (int i = 0; i < num_sets; i++)
       {
            System.out.print("[");
           for (int j = 0; j < association; j++)
                System.out.print(tag_arr[i][j] +" ");
            System.out.print("], ");
       }
       System.out.println(" ");

        printStats();
        scan.close();
    }

}
