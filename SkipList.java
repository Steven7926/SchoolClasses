// Steven Hudson
// NID: st622455
// COP 3503, Summer 2019

import java.io.*;
import java.util.*;

class Node<T extends Comparable<T>>
{
    LinkedList<Node<T>> nextNode = new LinkedList<Node<T>>();  // Reference next node
    private T value;
    private int height;
    
    // Create a node with no data (head node)
    Node(int height)
    {   
        this.height = height;
        for (int i = 0; i < height; i++)
            nextNode.add(null);
    }
    // Create a node that has data (not the head node)
    Node(T data, int height)
    {
        this.height = height; 
        value = data; 
        for (int i = 0; i < height; i++)
            nextNode.add(null);
    }
    // Return the value in the node
    public T value()
    {
        return value;
    }
    // Return the height of the node
    public int height()
    {
        return height;
    }
    // Gets the next node at the level passed
    public Node<T> next(int level)
    {
        if (level > (height - 1) || level < 0)
            return null;
        
        Node<T> next = nextNode.get(level);
        return next;
    }
    // Sets the nodes next reference at the level passed. 
    public void setNext(int level, Node<T> node)
    {
       nextNode.set(level, node); 
    }
    // Grow the node
    public void grow()
    {
        nextNode.add(height, null);
        height++;
    }
    // Toss a coin, 50% chance it'll grow
    public void maybeGrow()
    {
        Random rand = new Random();
        boolean growPossibly = rand.nextBoolean();
        if (growPossibly)
        {
            nextNode.add(height, null);
            height++;
        }
    }
    // Trim down the height of the node to the height that is passed
    // by removing the links from the top of the node
    public void trim(int height)
    {

    }
}

public class SkipList<T extends Comparable<T>>
{
    private Node<T> head; 
    private int height; 
    private int size; 

    // Make the head node with a height of 1; Constructor.
    SkipList()
    {
        height = 1;
        head = new Node<T>(height);
    }
    // If we recieve a height make a head node with that height; Constructor.
    SkipList(int height)
    {
        if(height < 1)
            this.height = 1; 
        else
            this.height = height; 

        head = new Node<T>(height);
    }
    // Return the number of node (size) of the skip list.
    public int size()
    {
        return size;
    }
    // Return how tall the tallest node in the skip list is.
    public int height()
    {
        return height;
    }
    // Return the head node of the skip list.
    public Node<T> head()
    {
        return head;
    }
    // Insert the data parameter passed into the skip list, with no height specified,
    // we generate a random height with respect to the max height of skip lists and the 
    // rules of node distribution in skip lists. 
    public void insert(T data)
    {
        int ranHeight = generateRandomHeight(this.height);
        insert(data, ranHeight);
    }
    // Similar to the top function, I insert data into the skip list, 
    // except this time our height of the node is specified.
    public void insert (T data, int height)
    {
        int level = height - 1;
        size++;
        int maxH = getMaxHeight(size); 
        Node<T> nodeNew = head;
        LinkedHashMap<Integer, Node<T>> nodeBefore = new LinkedHashMap<Integer, Node<T>>();   
        // If our height is smaller than the max height then we need to grow
        // the skip list
        if (this.height < maxH)
        {
            this.height = maxH;
            growSkipList();
        }
        // Begin Traversing the list
        while (nodeNew != null && level >= 0)
        {
            // We looked ahead and saw we would overshoot where we want to add
            // so slap the node in our list and 
            // drop down a level
            if (nodeNew.next(level) == null || (nodeNew.next(level).value()).compareTo(data) >= 0)
            {
                nodeBefore.put(level, nodeNew);
                level--;
            }
            // The value at the current level is less than data, so we have not overshot 
            // where we insert yet, thus we need to keep going to the next node at the 
            // same level.
            else if ((nodeNew.next(level).value()).compareTo(data) < 0)
                nodeNew = nodeNew.next(level);
        }
        int x = nodeBefore.size();
        Node<T> dataNode = new Node<T>(data, height);
        // Officially insert our data filled node
        int updated = height - 1;
        while (updated >= 0)
        {
            Node<T> node = nodeBefore.get(updated).next(updated);
            dataNode.setNext(updated, node);
            nodeBefore.get(updated).setNext(updated, dataNode);
            updated --;
        }
    }
    // Delete the data from the skip list
    public void delete(T data)
    {

    }
    // Traverse through the list until the element is found, or you pass where it would be
    // if it is not found, return false, otherwise return true.
    public boolean contains(T data)
    {
        Node<T> isContain = head;
        int level = height - 1;
        while (level >= 0 && isContain != null )
        {
            // Here we say the opposite, if we have gone over the data (compareTo returns > 0 
            // in this scenario) or were pointing to null, then go down a level and check again
            if (isContain.next(level) == null || (isContain.next(level).value()).compareTo(data) > 0)
               level--;
            // Use compareTo for Generics, similar to how it's was used in Generic BSTs.
            // compareTo returns < 0 if the value at the current level is less than data.
            // this means we have not hit the data we are looking for yet, we are before it.
            // If that is the case then we go to the next node at the same level.
            else if ((isContain.next(level).value()).compareTo(data) < 0)
                isContain = isContain.next(level);
            // If we come into the loop and the value is equivalent to the data
            // then we've found the node we were looking for. 
            else if ((isContain.next(level).value()).compareTo(data) == 0)
                return true; 
        }
        // If we broke out of the loop then we've gone below our specified level 
        // and we've hit the end of the list, so all tracks have been covered and 
        // true has not been returned so it must not be in the skip list
        return false;
    }
    // Pretty Similar to the contains method (see contains for comments), except once we find the node
    // we are looking for, we are gonna return that instead of a boolean.
    // So were gonna set head to our target node and traverse
    // until we find the node with the data we passed. 
    public Node<T> get(T data)
    {  
        Node<T> targetNode = head;
        int level = height - 1; 
        // Our default height is one, leaving our default level to be at smallest 1 - 1 = 0
        // so we say level >= 0 when traversing
        while (level >= 0 && targetNode != null )
        {
            if (targetNode.next(level) == null || (targetNode.next(level).value()).compareTo(data) > 0)
                level--;

            else if ((targetNode.next(level).value()).compareTo(data) < 0)
                targetNode = targetNode.next(level);

            else if ((targetNode.next(level).value()).compareTo(data) == 0)
                return targetNode; 
        }
        // If we broke out of the loop then we've gone below our specified level 
        // and we've hit the end of the list, so all tracks have been covered and 
        // our nodde has not been returned so it must not be in the skip list
        
      return null;  
    }
    // Perform the change of base formula to get log base 2 of n
    // Take the Ceiling of that to get max height.
    private static int getMaxHeight(int n)
    {
        if (n == 0 || n == 1)
            return 1;
        double baseTwoN = (Math.log(n) / Math.log(2));
        int maxHeight = (int)Math.ceil(baseTwoN);
        return maxHeight;
    }
    // Returns a random height as described; 1 with a 50% chance
    // 2 with a 25% chance, 3 with a 12.5 % chance. 
    private static int generateRandomHeight(int maxHeight)
    {
        int randHeight = 0;
        for (int i = 1; i < maxHeight; i++)
        {
            Random rando = new Random();
            boolean coinToss = rando.nextBoolean();
            if (coinToss) 
                randHeight++;
            else 
                break;
        }
        return randHeight; 
    }

    // Grow the skip list for insert function
    private void growSkipList()
    {
        // We have to grow the head first. 
        head.grow();
        Node<T> nNode = head; 
        int level = height -1;
        while (nNode.next(level) != null)
        {
            // Give a 50% chance it'll grow
            nNode.maybeGrow();
        }
    }

    // Trim the skip list for delete function
    private void trimSkipList()
    {

    }
    
    // Returns the difficulty rating
    public static double difficultyRating()
    {
        return 5.0;
    }

    // Returns the hours spent on the assignment
    public static double hoursSpent()
    {
        return 25.0;
    }
}