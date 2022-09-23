import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Item implements Comparable{
	int value;
	int weight;
	int num; // original index of the item

	public Item(int v, int w, int n)
	{
		value = v;
		weight = w;
		num = n;
	}

	// will sort items by value / weight ratio in descending order
	public int compareTo(Object other)
	{
		Item o = (Item)other;
		return (int) (o.value/o.weight - value/weight);
	}

}

class ksgs {

	private static int[] values, weights;
	private static int capacity, numItems;

	public static void main(String[] args) throws FileNotFoundException
	{
		parseFile(args[0]);
		findSolution();
	}

	private static void findSolution()
	{
		PriorityQueue pq = new PriorityQueue<Item>(numItems);
		ArrayList<Integer> solution = new ArrayList<Integer>();
		int currValue, currWeight;
		Item temp;

		currWeight = currValue = 0;

		// this will add each item to the priority queue and sort them by
		// value / weight ratio
		for (int i = 0; i < numItems; i++)
		{
			Item item = new Item(values[i], weights[i], i + 1);
			pq.add(item);
		}

		// add each item to the knapsack until the capacity is reached or the pq is empty
		temp = (Item)pq.poll();
		while (!pq.isEmpty() && currWeight < capacity)
		{
			// if the next best val to weight ratio fits in the knapsack, then we add it
			// this is the greedy part to the approach
			if (currWeight + temp.weight <= capacity)
			{
				currValue += temp.value;
				currWeight += temp.weight;
				solution.add(temp.num);
			}
			temp = (Item)pq.poll();
		}
		// sort the indices that we chose
		Collections.sort(solution);

		// print the solution details
		System.out.printf("Greedy solution (not necessarily optimal): Value %d, Weight %d\n", currValue, currWeight);
		for (int i = 0; i < solution.size(); i++)
			System.out.printf("%d ", solution.get(i));
		System.out.println();
	}

	// parse the given file and align the information into appropriate data structures
	private static void parseFile(String filename) throws FileNotFoundException
	{
		FileInputStream in = new FileInputStream(new File(filename));
     	Scanner sc = new Scanner(in);

     	numItems = sc.nextInt();
     	values = new int[numItems];
     	weights = new int[numItems];

     	for (int i = 0; i < numItems; i++)
     	{
     		sc.nextInt();
     		values[i] = sc.nextInt();
     		weights[i] = sc.nextInt();
     	}

     	capacity = sc.nextInt();
	}
}