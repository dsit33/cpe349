import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class EditDistance {

	private static final int gap = 2;
	private static final int swap = 1;

	public EditDistance(){}

	public static void main(String[] args) throws FileNotFoundException{
		int[][] opt;
		boolean p = false;
		String known, unknown;
		
		FileInputStream in = new FileInputStream(new File("testEcoli3000.txt"));
     	Scanner sc = new Scanner(in);

     	unknown = sc.nextLine();
     	known = sc.nextLine();

		if (args.length >= 1 && args[0].equals("-p"))
			p = true;
		
		opt = buildTable(known, unknown, known.length(), unknown.length());

		//printTable(opt, known.length(), unknown.length());

		traceBack(opt, p, known, unknown, known.length(), unknown.length());
	}

	private static int match(char a, char b)
	{
		// if the characters are the same, no penalty for using
		// if they are not the same, cost is 1
		if (a == b)
			return 0;
		else
			return swap;
	}

	// debugging method to visually print table
	private static void printTable(int[][] opt, int len_known, int len_new)
	{
		int i, j;

		for (i = 0; i <= len_known; i++)
		{
			for (j = 0; j <= len_new; j++)
				System.out.printf("%2d ", opt[i][j]);
			System.out.println();
		}
	}

	public static int[][] buildTable(String known, String unknown, int len_known, int len_new)
	{
		int[][] opt = new int[len_known + 1][len_new + 1];
		int i, j;

		// initialize the base case if i == 0 || j == 0
		for (i = 0; i <= len_known; i++)
			opt[i][0] = i * gap;
		for (j = 0; j <= len_new; j++)
			opt[0][j] = j * gap;

		// dynamically fill the table with results
		for (i = 1; i <= len_known; i++)
		{
			for (j = 1; j <= len_new; j++)
			{
				// calculate the minimum cost of using the current letter here, or
				// adding a gap and trying the previous letter in unknown against the
				// letter in known or vice versa
				opt[i][j] = Math.min(match(known.charAt(i - 1), unknown.charAt(j - 1)) + opt[i - 1][j - 1], 
					Math.min(gap + opt[i - 1][j], gap + opt[i][j - 1]));
			}
		}

		return opt;
	}

	public static void traceBack(int[][] opt, boolean p, String known, String unknown, int len_known, int len_new)
	{
		int i, j;

		i = len_known;
		j = len_new;
	
		System.out.printf("Edit distance = %d\n", opt[i][j]);

		// trace until we are at the starting point
		while (i > 0 || j > 0)
		{	
			// we didn't use the character in the second string here
			if (i != 0 && opt[i][j] == opt[i - 1][j] + 2)
			{
				if (p)
					System.out.printf("%c - 2\n", known.charAt(i - 1));
				i--;
			}
			// we didn't use the character in the first string here
			else if (j != 0 && opt[i][j] == opt[i][j - 1] + 2)
			{
				if(p)
					System.out.printf("- %c 2\n", unknown.charAt(j - 1));
				j--;
			}
			// we used the characters from both strings here
			else
			{
				if (p)
					System.out.printf("%c %c %d\n", known.charAt(i - 1), unknown.charAt(j - 1), 
						match(known.charAt(i - 1), unknown.charAt(j - 1)));
				i--;
				j--;
			}
		}
	}
}


