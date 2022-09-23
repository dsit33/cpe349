import java.util.*;
import java.math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class ksfe{

	private static int[] values, weights;
	private static int capacity, numItems;

	public static void main(String[] args) throws FileNotFoundException
	{
		parseFile(args[0]);
		findOptimal();
	}

	private static void findOptimal()
	{
		int currValue, currWeight, maxValue, maxWeight, optScenario;

		// calculate the number of possible combinations of items
		int numScenarios = (int)Math.pow(2, numItems); 

		maxValue = maxWeight = optScenario = 0;

		// this loop will consider every possible combination of items to grab
		for (int i = 1; i <= numScenarios; i++)
		{
			currValue = currWeight = 0;

			// calculate the value and weight of the current scenario
			for (int j = 0; j < log2(i); j++)
			{
				int mask = 1 << j;

				if ((i & mask) != 0)
				{
					currValue += values[j];
					currWeight += weights[j];
				}
			}

			// if the current scenario is better than our previous optimal, set
			// new maxes
			if (currValue > maxValue && currWeight <= capacity)
			{
				maxValue = currValue;
				maxWeight = currWeight;
				optScenario = i;
			}
		}

		// print the found solution
		System.out.printf("Using Brute force the best feasible solution found: Value %d, Weight %d\n", maxValue, maxWeight);
		for (int i = 0; i < numItems; i++)
		{
			int mask = 1 << i;

			if ((optScenario & mask) != 0)
				System.out.printf("%d ", i + 1);
		}
		System.out.println();
	}

	// return the result of log base 2 on a certain number
	private static int log2(int x)
	{return (int) (Math.log(x) / Math.log(2) + .5);}

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