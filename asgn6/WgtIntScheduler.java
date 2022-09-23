import java.util.*;

class WgtIntScheduler {

	public WgtIntScheduler(){}

	public static int[] getOptSet(int[] stime, int[] ftime, int[] weight)
	{
		int wgtW, idx, numJobs = weight.length;
		int[] set, table = new int[numJobs];
		Job[] jobs = new Job[numJobs];
		ArrayList<Integer> temp = new ArrayList<Integer>();

		// create an array of Jobs that holds the start, finish, and weight in one object
		// and sort by increasing finish time
		for (int i = 0; i < numJobs; i++)
			jobs[i] = new Job(stime[i], ftime[i], weight[i], i + 1);
		Arrays.sort(jobs);

		// initialize the table with the first job containing its own weight
		table[0] = jobs[0].weight;

		// iterate and use our recurrence relation to calculate all the other
		// spots in the table
		for (int j = 1; j < numJobs; j++)
		{
			wgtW = jobs[j].weight;
			idx = bestNonConflict(jobs, j);

			if (idx != -1)
				wgtW += table[idx];

			// store the maximum of the weight including current job and the weight
			// without including current job
			table[j] = Math.max(wgtW, table[j - 1]);
		}

		// our table is complete, trace back and find the optimal solution
		getSolution(table, jobs, numJobs - 1, temp);

		printTable(table, numJobs);
		
		set = toPrimitive(temp);
		Arrays.sort(set);

		return set;
	}


	// debugging method to print out table
	private static void printTable(int[] table, int numJobs)
	{
		for (int i = 0; i < numJobs; i++)
			System.out.printf("%d ", table[i]);
		System.out.println();
	}

	// method to convert ArrayList<Integer> to int[]
	private static int[] toPrimitive(ArrayList<Integer> set)
	{
		int[] result = new int[set.size()];

		for (int i = 0; i < set.size(); i++)
			result[i] = set.get(i);

		return result;
	}

	// method to find the set of optimal jobs by using the same steps
	// used to create the table but stepping backwards now
	private static void getSolution(int[] table, Job[] jobs, int idx, ArrayList<Integer> set)
	{
		int temp = bestNonConflict(jobs, idx);

		// there were no earlier compatible jobs with this one,
		// do not recurse
		if (temp == -1)
		{
			set.add(jobs[idx].num);
		}
		else if (idx == 0)
			System.out.println();
		// find the previous job that made the optimal solution
		else if (jobs[idx].weight + table[temp] >= table[idx])
		{
			set.add(jobs[idx].num);
			getSolution(table, jobs, temp, set);
		}
		else
			getSolution(table, jobs, idx - 1, set);
	}

	// returns idx of best compatible job
	private static int bestNonConflict(Job[] jobs, int idx)
	{
		int curWgt, result = -1;

		curWgt = 0;

		// iterate through the jobs starting at the end and find the
		// optimal job that has a finish time before our previous start
		for (int i = idx - 1; i >= 0; i--)
		{
			if (jobs[i].finish <= jobs[idx].start && jobs[i].weight > curWgt)
			{
				curWgt = jobs[i].weight;
				result = i;
			}
		}

		// no compatible jobs
		return result;
	}

}

class Job implements Comparable {

	int start, finish, weight, num;

	public Job(int s, int f, int w, int n)
	{
		start = s;
		finish = f;
		weight = w;
		num = n;
	}

	public int compareTo(Object other)
	{
		if (this.finish - ((Job)other).finish == 0)
			return this.weight - ((Job)other).weight;
		return this.finish - ((Job)other).finish;
	}
}




