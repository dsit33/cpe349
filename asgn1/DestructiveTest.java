import java.util.*;
import java.lang.Math;

public class DestructiveTest 
{
	/* this function will either return -1 if the device was safely dropped or the rung 
 	* that the first device was broken at. It will also be responsible for recording 
 	* the second and third drop locations.
 	*/
	public int dropDevice(int safest, int currentRung, int numDrops, int[] firstThree)
	{
		// record the second and third drops
		if (numDrops == 1)
		{
			firstThree[1] = currentRung;
		}
		if (numDrops == 2)
		{
			firstThree[2] = currentRung;
		}

		// we dropped one from too high and broke a device
		if (safest < currentRung)
		{	
			return currentRung;	
		}

		// we dropped a device and it didn't break, find the next rung
		return -1;
	}

	/* this function is for convenience and will serve to create the result list
	* in the correct order for output
 	*/
	public ArrayList<Integer> createList(int height, int safest, int mySafest, int numDrops,
		int first, int second, int third, int firstBreak, int secondBreak)
	{
		List<Integer> tempList = Arrays.asList(height, safest, mySafest, numDrops, first,
			second, third, firstBreak, secondBreak);
		ArrayList<Integer> resultList = new ArrayList<Integer>();

		resultList.addAll(tempList);

		return resultList;
	}

	/* algorithm to calculate the starting rung, add .5 at the end to make sure
	 * that we round up
	 */
	public int calculateFirst(int height)
	{
		return (int)Math.round((Math.sqrt(8 * height + 1) - 1) / 2 + .5);
	}

	public ArrayList<Integer> findHighestSafeRung(int height, int safest)
	{
		// declare and initialize the variables that will be added to the result list
		int currentRung, jumpSize, mySafest, numDrops, firstBreak, secondBreak, numDevices, temp;
		int[] firstThree = new int[]{-1, -1, -1};
		mySafest = firstBreak = secondBreak = -1;
		numDrops = 0;
		numDevices = 2;

		// calculate the first jump size
		jumpSize = calculateFirst(height);
		currentRung = firstThree[0] = jumpSize;

		// begin simulating dropping the device until we reach the end of the ladder
		while (jumpSize >= 0 && currentRung != height)
		{		
			firstBreak = dropDevice(safest, currentRung, numDrops++, firstThree);

			// a device was broken, exit the loop and step through more carefully
			if (firstBreak != -1)
			{
				numDevices--;
				break;
			}

			// calculate the next rung
			if ((temp = Math.min(currentRung + (--jumpSize), height)) == height)
			{
				jumpSize = height - currentRung;
			}
			currentRung = temp;		
		}

		// if we made it to the last rung, drop the device one more time and return the results
		if (currentRung == height)
		{
			firstBreak = dropDevice(safest, currentRung, numDrops++, firstThree);

			if (firstBreak != -1)
			{
				numDevices--;
			}
			else
			{
				return createList(height, safest, height, numDrops, firstThree[0],
					firstThree[1], firstThree[2], firstBreak, secondBreak);
			}
		}

		// we've broken a device, revert back to our last safe drop and step through 1 by 1
		for (int i = Math.max(firstBreak - jumpSize, 1); i < firstBreak && (secondBreak == -1); i++)
		{
			secondBreak = dropDevice(safest, i, numDrops, firstThree);
			numDrops++;
		}

		// determine if we broke the second device, record the safest height
		mySafest = (secondBreak != -1) ? (secondBreak - 1) : (firstBreak - 1);

		return createList(height, safest, mySafest, numDrops, firstThree[0],
				firstThree[1], firstThree[2], firstBreak, secondBreak);
	}
}
