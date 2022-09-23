import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Item implements Comparable{
	int value;
	int weight;
	int num; // original index from input file

	public Item(int v, int w, int n)
	{
		value = v;
		weight = w;
		num = n;
	}

	// will sort items by value / weight ratio in descending order
	public int compareTo(Object other)
	{
		double r1, r2;
		Item o = (Item)other;

		r1 = (double)o.value/o.weight;
		r2 = (double)value/weight;

		if (r2 < r1)
			return 1;
		if (r1 < r2)
			return -1;
		return weight - o.weight;
	}

}

class Node implements Comparable{
	String scenario;
	int upperBound;
	double profit;
	int weight;

	public Node(String s)
	{
		scenario = s;
		weight = 0;
	}

	public void setUB(int ub)
	{
		upperBound = ub;
	}

	public void setProfit(double p)
	{
		profit = p;
	}

	public int compareTo(Object other)
	{
		Node o = (Node)other;

		if (upperBound < o.upperBound)
			return -1;
		if (upperBound > o.upperBound)
			return 1;
		return weight - o.weight;
	}
}

class ksbb {
	private static ArrayList<Item> items;
	private static PriorityQueue<Node> q;
	private static Node opt;
	private static int[] values, weights;
	private static int capacity, numItems;
	private static String possibility = "";

	static class Task extends TimerTask {
		public void run()
		{
			printResults(possibility, opt);
			System.exit(0);
		}
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		parseFile(args[0]);
		Timer t = new Timer();
		Task task = new Task();
		t.schedule(task, 60000); // timer will go for one minute and then will report a solution
		findSolution();
		t.cancel();
		System.exit(0);
	}

	private static double createBound(String scenario, double maximum_bound)
	{
		Node n = new Node(scenario);
		int i, currWeight;
		double upperBound, fractional;
		i = currWeight = 0;
		upperBound = 0.0;

		// CALCULATE THE UPPER BOUND VALUE (real value) of the current scenario
		while (i < scenario.length())
		{
			if (scenario.charAt(i) == '1')
			{
				upperBound = upperBound - items.get(i).value;
				currWeight += items.get(i).weight;
			}
			i++;
		}
		n.setUB((int)upperBound);
		n.weight = currWeight;

		while (currWeight < capacity && i < numItems)
		{
			// if we can't fit the whole item into the bag, add a fraction of item
			if (currWeight + items.get(i).weight > capacity)
			{
				fractional = (double)(items.get(i).value) / items.get(i).weight;
				upperBound = upperBound - (capacity - currWeight) * fractional;
				currWeight = capacity;
			}
			// the next item fits in the knapsack capacity
			else
			{
				currWeight += items.get(i).weight;
				upperBound = upperBound - items.get(i).value;
			}
			i++;
		}
		n.setProfit(upperBound);

		// add the created node to the queue to be considered for exploring or prune that subtree
		q.add(n);

		// the method will return an updated max bound
		if (n.upperBound < maximum_bound)
			return n.upperBound;
		return maximum_bound;
	}

	private static void findSolution()
	{
		int w, v;
		double maximum_bound;
		Node n;

		// initialize our queue, push a dummy node onto the top and set initial max bound
		q = new PriorityQueue<Node>(numItems);
		maximum_bound = createBound("", Double.POSITIVE_INFINITY);
		opt = q.peek();

		while (!q.isEmpty())
		{
			n = q.poll();

			// prune subtree if it's potential is lower than current real value
			if (n.profit > maximum_bound)
			{
				continue;
			}
			// if we have reached a leaf node, store the possible solution
			else if (n.scenario.length() == numItems)
			{
				possibility = n.scenario; // guaranteed to hold optimal solution when the q is empty
				opt = n;
			}
			else
			{
				// only create feasible nodes
				if (n.weight + items.get(n.scenario.length()).weight <= capacity)
					maximum_bound = createBound(n.scenario + "1", maximum_bound);
				maximum_bound = createBound(n.scenario + "0", maximum_bound);
			}
		}
		printResults(possibility, opt);
	}
	
	private static void printResults(String possibility, Node opt)
	{
		ArrayList<Integer> sol = new ArrayList<Integer>();

		// go through the bit string and find the original indices of the items
		for (int i = 0; i < possibility.length(); i++)
		{
			if (possibility.charAt(i) == '1')
			{
				sol.add((items.get(i)).num);
			}
		}

		Collections.sort(sol);
		System.out.printf("Using Branch and Bound the best feasible solution found: Value: %d, Weight: %d\n", 
			-1*opt.upperBound, opt.weight);
		for (int x : sol)
			System.out.printf("%d ", x);
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
     	items = new ArrayList<Item>();

     	for (int i = 0; i < numItems; i++)
     	{
     		sc.nextInt();
     		values[i] = sc.nextInt();
     		weights[i] = sc.nextInt();
     		items.add(new Item(values[i], weights[i], i + 1));
     	}
     	// sort the items in increasing order of value / weight ratio, similar to
     	// greedy approach
     	Collections.sort(items);

     	capacity = sc.nextInt();
	}
}