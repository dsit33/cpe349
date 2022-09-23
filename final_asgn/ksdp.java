import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class ksdp {

	private static int[][] table;
	private static int[] values, weights;
	private static int capacity, numItems;

	public static void main(String[] args) throws FileNotFoundException
	{
		parseFile(args[0]);
		buildTable();
		traceBack();
	}

	// fill the table taking either the max of including item in the solution
	// or not including it all with the previous scenario given from the table
	private static void buildTable()
	{
		table = new int[numItems + 1][capacity + 1];

		for (int i = 1; i <= numItems; i++)
		{
			for (int j = 1; j <= capacity; j++)
			{
				// this item could potentially fit in the sub-knapsack
				if (weights[i - 1] <= j)
				{
					table[i][j] = Math.max(values[i - 1] + table[i - 1][j - weights[i - 1]],
						table[i - 1][j]);
				}
				// this item can't fit in this subproblem of the given knapsack
				else
					table[i][j] = table[i - 1][j];
			}
		}
	}

	// trace through the table to find the solution and weight carried
	private static void traceBack()
	{
		ArrayList<Integer> solution = new ArrayList<Integer>();

		int i, j, totalWeight = 0;
		i = numItems;
		j = capacity;

		while (i > 0 && j > 0)
		{
			// this item was not used in the solution
			if (table[i][j] == table[i - 1][j])
				i--;
			// this item was used, decrease the remaining capacity to search for
			// next item
			else
			{
				solution.add(i);
				totalWeight += weights[i - 1];
				j -= weights[i - 1];
				i--;
			}
		}

		// sort and print the solution to the terminal
		Collections.sort(solution);
		System.out.printf("Dynamic Programming solution: Value %d, Weight %d\n", table[numItems][capacity], totalWeight);
		for (int k = 0; k < solution.size(); k++)
			System.out.printf("%d ", solution.get(k));
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