import java.util.*;

class Inversions 
{

	public static int invCounter(int[] nums)
	{
		// begin to DIVIDE the array
		return mergeSortCount(nums, 0, nums.length - 1);
	}

	private static int merge(int[] nums, int low, int mid, int high)
	{
		int[] left = Arrays.copyOfRange(nums, low, mid + 1);
		int[] right = Arrays.copyOfRange(nums, mid + 1, high + 1);

		int l_idx = 0, r_idx = 0, main_idx = low, inversions = 0;

		// iterate through the left and right arrays to check for inversions
		while (l_idx < left.length && r_idx < right.length)
		{
			// current item is in the right spot, put it in main array
			if (left[l_idx] <= right[r_idx])
			{
				nums[main_idx++] = left[l_idx++];
			}
			// we found an inversion, swap places and calculate the
			// the number of spaces that we're moving using indices
			else
			{
				nums[main_idx++] = right[r_idx++];
				inversions += (mid + 1) - (low + l_idx);
			}
		}

		// fill out the main array with any numbers left in the subarrays
		while (l_idx < left.length)
			nums[main_idx++] = left[l_idx++];
		while (r_idx < right.length)
			nums[main_idx++] = right[r_idx++];

		return inversions;
	}

	private static int mergeSortCount(int[] nums, int low, int high)
	{
		int inversions = 0;
		int mid = (low + high) / 2;

		if (low < high)
		{
			// DIVIDE array and CONQUER both halves separately
			inversions += mergeSortCount(nums, low, mid);
			inversions += mergeSortCount(nums, mid + 1, high);

			// COMBINE the separately sorted halves and resort them
			inversions += merge(nums, low, mid, high);
		}

		return inversions;
	}
}
