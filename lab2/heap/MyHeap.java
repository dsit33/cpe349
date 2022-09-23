import java.util.*;
import java.lang.Math;

class MyHeap
{
	private int[] heap;
	private int cap;
	private int numItems = 0;
  

	public MyHeap()
	{
		cap = 50;
		heap = new int[cap + 1];
	}

	public MyHeap(int capacity)
	{
		cap = capacity;
		heap = new int[cap + 1];
	}

	// the following private methods calculate the relating nodes based the index passed
	private int getParent(int idx)
	{return idx / 2;}

	private int getRight(int idx)
	{return 2 * idx + 1;}

	private int getLeft(int idx)
	{return 2 * idx;}

	// method to tell if node at idx is a leaf
	private boolean isLeafNode(int idx)
	{return (idx > (numItems / 2) && idx <= numItems);}

	// swap nodes at input indices
	private void swap(int idx1, int idx2)
	{
		int temp = heap[idx1];
		heap[idx1] = heap[idx2];
		heap[idx2] = temp;
	}

	// build heap using bottom up construction
	public boolean buildHeap(int[] nums)
	{
		// if size of nums array is beyond cap, can't build
		if (nums.length > cap)
			return false;
		numItems = nums.length;

		// copy nums array into heap
		for (int i = 0; i < nums.length; i++)
		{
			heap[i + 1] = nums[i];
		}

		// bottom up construction
		for (int i = nums.length / 2; i > 0; i--)
		{
			driftDown(i);
		}
		
		return true;
	}

	public boolean insert(int num)
	{
		int idx;

		// we are at capacity
		if (numItems >= cap)
			return false;

		// add node as leaf and drift up
		heap[++numItems] = num;

		driftUp(numItems);

		return true;
	}

	public int findMin()
	{return heap[1];}

	public int deleteMin()
	{
		int result = heap[1];
		heap[1] = heap[numItems--]; // put the last node at the root and drift down
		if (!isEmpty())
			driftDown(1);

		return result;
	}

	public boolean isEmpty()
	{return numItems == 0;}

	public boolean isFull()
	{return numItems == cap;}

	public void driftDown(int idx)
	{
		int swapIdx, left, right;

		// cannot drift down from leaf
		if (isLeafNode(idx))
			return;

		left = getLeft(idx);
		right = getRight(idx);

		// a parent node is bigger than one of their children, switch nodes
		if (heap[idx] > heap[left] || (left != numItems && heap[idx] > heap[right]))
		{
			swapIdx = (left == numItems || heap[left] <= heap[right]) ? (left) : (right);
			swap(idx, swapIdx);
			driftDown(swapIdx); // recursively call drift to check new family of nodes
		}
	}

	public void driftUp(int idx)
	{
		int swapIdx;

		// cannot drift up from root
		if (idx ==  1)
			return;

		// if a child node is smaller than parent, swap
		swapIdx = getParent(idx);

		if (heap[idx] < heap[swapIdx])
		{
			swap(idx, swapIdx);
			driftUp(swapIdx); //recursively call drift to check new family of nodes
		}
	}

	public static int[] heapSortDecreasing(int[] nums)
	{
		int[] sorted;
		int total;
		MyHeap h = new MyHeap();
		h.buildHeap(nums); // build the binary tree

		total = h.numItems; // save total number of items as numItems will be decremented
		sorted = new int[h.numItems];
		for (int i = 0; i < total; i++)
		{
			sorted[total - 1 - i] = h.deleteMin(); // get root off the tree and add it to end spot in array
		}
		
		return sorted;
	}

	public int getHeapCap()
	{return cap;}

	public int getHeapSize()
	{return numItems;}
}
