// Steven Hudson
// NID: st622455
// COP 3503, Summer 2019

import java.io.*;
import java.util.*;

// Initializing the node structure as generic for use in the BST
class Node<T extends Comparable<T>>
{
	T data;
	Node<T> left, right;

	Node(T data)
	{
		this.data = data;
	}
}

public class GenericBST<T extends Comparable<T>>
{
	private Node<T> root; // Initialize root node

	// Gives a reference to the root using the private method below 
	// with the data passed to it from where it's called
	public void insert(T data)
	{
		root = insert(root, data);
	}

	// Recursive method that inserts the node into the BST with 
	// the data passed to it and returns the root node for traversal
	private Node<T> insert(Node<T> root, T data)
	{
		if (root == null)
		{
			return new Node<T>(data);
		}
		// If data passed is smaller than roots data, go left
		else if (data.compareTo(root.data) < 0)
		{
			root.left = insert(root.left, data);
		}
		// If data passed is larger than roots data, go right
		else if (data.compareTo(root.data) > 0)
		{
			root.right = insert(root.right, data);
		}

		return root;
	}

	// Gives a reference to the root using the private function below
	// with the data passed to it from where it's called
	public void delete(T data)
	{
		root = delete(root, data);
	}
	// Recursive function that interates through 
	// the BST to find the node that needs to be deleted. 
	private Node<T> delete(Node<T> root, T data)
	{
		if (root == null)
		{
			return null;
		}
		// If data passed is smaller than roots data, go left
		else if (data.compareTo(root.data) < 0)
		{
			root.left = delete(root.left, data);
		}
		// If data passed is larger than roots data, go right
		else if (data.compareTo(root.data) > 0)
		{
			root.right = delete(root.right, data);
		}
		// Handle the scenarios for when we reach the data to delete
		else 
		{
			// Node has no children, return null
			if (root.left == null && root.right == null)
			{
				return null;
			}
			// Node has no left child but has a right child
			// return right child
			else if (root.left == null)
			{
				return root.right;
			}
			// Node has no right child, but has a left child
			// return left child
			else if (root.right == null)
			{
				return root.left;
			}
			// Node has two children; find the max value in the 
			// left sub-tree and set left nodes data to the data returned
			// when deleting the new root in the left sub-tree
			else
			{
				root.data = findMax(root.left);
				root.left = delete(root.left, root.data);
			}
		}

		return root;
	}

	// This method assumes root is non-null, since this is only called by
	// delete() on the left subtree, and only when that subtree is not empty.
	// It goes right until it finds the largest value and returns it
	private T findMax(Node<T> root)
	{
		while (root.right != null)
		{
			root = root.right;
		}

		return root.data;
	}

	// Takes the data and returns the result of the private
	// contains function below by passing both root and data to it.
	public boolean contains(T data)
	{
		return contains(root, data);
	}

	// Recursive method that handles requests on whether 
	// the BST conatains a certain element or not
	private boolean contains(Node<T> root, T data)
	{
		if (root == null)
		{
			return false;
		}
		// If data passed is smaller than roots data, go left
		else if (data.compareTo(root.data) < 0)
		{
			return contains(root.left, data);
		}
		// If data passed is larger than roots data, go right
		else if (data.compareTo(root.data) > 0)
		{
			return contains(root.right, data);
		}
		else
		{
			return true;
		}
	}

	// Passes the root to the inorder function below that will print the inorder
	public void inorder()
	{
		System.out.print("In-order Traversal:");
		inorder(root);
		System.out.println();
	}
	// Recursive method that prints the inorder traversal of the BST
	private void inorder(Node<T> root)
	{
		if (root == null)
			return;

		inorder(root.left);
		System.out.print(" " + root.data);
		inorder(root.right);
	}

	// Passes the root to the preorder function below that will print the preorder 
	public void preorder()
	{
		System.out.print("Pre-order Traversal:");
		preorder(root);
		System.out.println();
	}
	// Recursive method that prints the preorder traversal of the BST
	private void preorder(Node<T> root)
	{
		if (root == null)
			return;

		System.out.print(" " + root.data);
		preorder(root.left);
		preorder(root.right);
	}

	// Passes the root to the postorder function below that will print the postorder
	public void postorder()
	{
		System.out.print("Post-order Traversal:");
		postorder(root);
		System.out.println();
	}
	// Recursive method that prints the postorder traversal of the BST
	private void postorder(Node<T> root)
	{
		if (root == null)
			return;

		postorder(root.left);
		postorder(root.right);
		System.out.print(" " + root.data);
	}
	// Returns the diffivculty rating of the assignment
	public static double difficultyRating()
	{
		return 3.1;
	}
	//Returns the hours spent on the assignment
	public static double hoursSpent()
	{
		return 15; 
	}
}
