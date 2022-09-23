import java.util.ArrayList;

//	This class generates subsets of a set represented as a string in Java

public class SubsetGen
{   
	public SubsetGen() 
   {}

   public ArrayList<String> getSubsets(String word)   
   {
      ArrayList<String> temp = new ArrayList<String>();
      ArrayList<String> subsets = new ArrayList<String>();
      int len = word.length();

      if (len > 0)
      {
         temp = getSubsets(word.substring(0, len - 1));

         for (int i = 0; i < temp.size(); i++)
         {
            subsets.add(temp.get(i));
         }

         for (int i = 0; i < temp.size(); i++)
         {
            subsets.add(temp.get(i) + word.charAt(len - 1));
         }

         return subsets;
      }
      else
      {
         subsets.add("");
         return subsets;
      }
   }

   public ArrayList<String> getGrayCode(int n)   
   {
      ArrayList<String> temp = new ArrayList<String>();
      ArrayList<String> codes = new ArrayList<String>();

      if (n > 1)
      {
         temp = getGrayCode(n - 1);

         for (int i = 0; i < temp.size(); i++)
         {
            codes.add("0" + temp.get(i));
         }

         for (int i = temp.size() - 1; i >= 0; i--)
         {
            codes.add("1" + temp.get(i));
         }

         return codes;
      }
      else
      {
         codes.add("0");
         codes.add("1");
         return codes;
      }
   }
}
