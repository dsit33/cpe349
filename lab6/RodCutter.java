import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class RodCutter {
	public RodCutter(){}

	public static void main(String[] args) throws FileNotFoundException
	{
		int cases, length, counter;
		int[] values;
		int[][] results;
		FileInputStream in = new FileInputStream(new File("rodOptTest.txt"));
     	Scanner sc = new Scanner(in);

    	cases = sc.nextInt();

    	for (int i = 0; i < cases; i++)
    	{
    		counter = 0;
    		length = sc.nextInt(); // get the total length of the test rod

    		// create and populate new array to hold the values of each length cut
    		values = new int[length];
    		while (counter < length)
    		{
    			values[counter++] = sc.nextInt();
    		}

    		// create a table of the optimal results
    		results = findOptimalCuts(length, values);

    		// print out our findings
    		System.out.printf("Case %d: \n", i + 1);
    		printResults(results, length);
    		System.out.println();
    	}
	}

	private static void printResults(int[][] results, int length)
	{
		int n, m, count = 0;

		// print the maximum profit possible from a rod of each length
		for (int i = 1; i <= length; i++)
			System.out.printf("total for length %2d = %d\n", i, results[length][i]);

		// trace through the table starting at the bottom corner and find which cuts we made
		n = length;
		m = length;
		while (n != 0) 
		{
			if (results[n][m] != results[n - 1][m]) 
			{
				count++; // we made another cut of this length
				m = m - n; // offset the possible length we are now looking at
			}
			else
			{
				// we made one or more cuts of length m, print results and reset
				if (count != 0)
					System.out.printf("Number of rods of length %2d = %d\n", n, count);
				n--;
				count = 0;
			}
		}
	}

	private static int[][] findOptimalCuts(int length, int[] values)
	{
		int temp;
		int[][] table = new int[length + 1][length + 1]; // create the table to fill in with optimal values

		// iterate over the table to use our recurrence relation to fill
		for (int i = 1; i <= length; i++)
		{
			for (int j = 1; j <= length; j++)
			{
				// if the length we are trying to cut is longer than we actually have
				if (i > j)
					table[i][j] = table[i - 1][j]; // use the same cut as if we were trying one size lower
				else
					// our recurrence relation to calculate optimal cuts
					table[i][j] = Math.max(values[i - 1] + (table[i][j - i]), (table[i - 1][j]));
			}
		}
		return table;
	}

	// test function to visually create the table
	private static void printTable(int[][] results, int length)
	{
		for (int i = 0; i <= length; i++)
		{
			for (int j = 0; j <= length; j++)
				System.out.printf("%3d ", results[i][j]);
			System.out.println("");
		}
		System.out.println("");
	}

}