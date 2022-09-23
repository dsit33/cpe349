import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

class TopSorter  {	
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
   boolean directed; 
   int nvertices;
   int nedges; 
   int numComp;	

   public TopSorter(){
   }

   public ArrayList<Integer> topSortGenerator(String filename) throws FileNotFoundException
   {
         Queue<Vertex> degZ = new LinkedList<>(); // used to store any vertices that have in deg of 0
         ArrayList<Integer> sorted = new ArrayList<Integer>(); // return array
         readfile_graph(filename); // load graph info
         int vRemaining = nvertices; // keep track of how many vertices have been visited
         Vertex current;

         Comparator<Vertex> byDegree = (Vertex v1, Vertex v2) -> Integer.compare(v1.inDegree, v2.inDegree);
         Collections.sort(vertices, byDegree); // sort the vertices arraylist by degree | ascending

         for (int i = 0; i < nvertices && (vertices.get(i).inDegree == 0); i++)
         {
            degZ.add(vertices.get(i)); // add any valid starting vertex to the queue
         }

         // run a while loop that executes until the degZ queue is empty
         while ((current = degZ.poll()) != null)
         {
            sorted.add(current.key); // add the next vertex to the topSort list

            // reduce the degree of all target vertices connected to current
            for (Vertex target : current.edges)
            {
               if (--target.inDegree == 0)
                  degZ.add(target);
            }

            // remove the current vertex from the list to properly find the next
            vRemaining--;
         }

         // for all vertices that weren't included in the sort, add a -1
         for (int i = 0; i < vRemaining; i++)
         {
            sorted.add(-1);
         }

         return sorted;
   }

   // helper function to add terminal vertices to the correct set
   private void adjustSet(Vertex v, HashSet<Integer> workingSet)
   {
   		// acccount for a single vertex with no connecting vertices
   		if (v.edges.isEmpty())
   		{
   			workingSet.add(v.key);
   			return;
   		}

   		for (Vertex edge : v.edges)
   		{
   			workingSet.add(edge.key);
   		}
   }

   public ArrayList<HashSet<Integer>> connectCheck()
   {
   		boolean found = false;
   		int numSets = 0; // start at one to account for thisvalue  taking a spot in list
   		ArrayList<HashSet<Integer>> results = new ArrayList<HashSet<Integer>>();
   		HashSet<Integer> workingSet;

   		// visit each vertex in arraylist and add the terminal vertex to a set
   		for (Vertex v : vertices)
   		{
   			// if it is in an existing set, add the terminal vertex to the same set
   			for (int i = 0; i < numSets && numSets != 0; i++)
   			{
   				if (results.get(i).contains(v.key))
   				{
   					workingSet = results.get(i);
   					adjustSet(v, workingSet);
   					found = true;
   				}
   			}

   			// if vertex we're currently visiting isn't in any of the exisiting sets,
   			// make a new one and add both vertex and terminal.
   			if (!found)
   			{
   				workingSet = new HashSet<Integer>();
   				adjustSet(v, workingSet);
   				results.add(workingSet);
   				numSets++;
   			}
   			found = false;
   		}
   		// add the number of sets to the front of the list
   		workingSet = new HashSet<Integer>();
   		workingSet.add(numSets);
   		results.add(0, workingSet);

   		return results;
   }

   private boolean colorVertices(Vertex v, int color)
   {
   		v.color = color; // set vertex equal to color passed through

   		// color each adjacent vertex to the source the opposite color
   		for (Vertex edge : v.edges)
   		{
   			if (edge.color == -1)
   			{
   				edge.color = (color == 1) ? (0) : (1);
   			}
   			// if the adjacent vertex is already colored and its the same
   			// as the current node, return false
   			else if (edge.color == v.color)
   				return false;
   		}
   		// we made it all the way through coloring the source vertex's neighbors
   		return true;
   }

   public boolean bipartiteCheck()
   {
   		// call colorVertices on all independent sections of the graph
   		// if unconnected
   		for (Vertex v : vertices)
   		{
   			// if current vertex is uncolored, set to first color
   			// and color neighbors accordingly
   			if (v.color == -1 && colorVertices(v, 1) == false)
   			{
   				return false;
   			}
   			// call method with existing color to see if adjacent vertices
   			// are the same color
   			else if (colorVertices(v, v.color) == false)
   				return false;
   		}
   		return true;
   }
	
   void readfile_graph(String filename) throws FileNotFoundException  {
      int x,y;
        //read the input
      FileInputStream in = new FileInputStream(new File(filename));
      Scanner sc = new Scanner(in);
      int temp = sc.nextInt(); // if 1 directed; 0 undirected
      directed = (temp == 1);
    	nvertices = sc.nextInt();
      for (int i=0; i<=nvertices-1; i++){
         Vertex tempv = new Vertex(i+1);   // kludge to store proper key starting at 1
         vertices.add(tempv);
      }

		nedges = sc.nextInt();   // m is the number of edges in the file
      int nedgesFile = nedges;
		for (int i=1; i<=nedgesFile ;i++)	{
			// System.out.println(i + " compare " + (i<=nedges) + " nedges " + nedges);
         x=sc.nextInt();
			y=sc.nextInt();
         //  System.out.println("x  " + x + "  y:  " + y  + " i " + i);
			insert_edge(x,y,directed);
		}  
		   // order edges to make it easier to see what is going on
		for(int i=0;i<=nvertices-1;i++)	{
			Collections.sort(vertices.get(i).edges);
		}  
	}
   
   static void process_vertex_early(Vertex v)	{
		timer++;
		v.entry_time = timer;
		//     System.out.printf("entered vertex %d at time %d\n",v.key, v.entry_time);
	}
	
	static void process_vertex_late(Vertex v)	{
		timer++;
		v.exit_time = timer;
		//     System.out.printf("exit vertex %d at time %d\n",v.key , v.exit_time);
	}

	static void process_edge(Vertex x,Vertex y) 	{
		int c = edge_classification(x,y);
		if (c == BACK) System.out.printf("back edge (%d,%d)\n",x.key,y.key);
		else if (c == TREE) System.out.printf("tree edge (%d,%d)\n",x.key,y.key);
		else if (c == FORWARD) System.out.printf("forward edge (%d,%d)\n",x.key,y.key);
		else if (c == CROSS) System.out.printf("cross edge (%d,%d)\n",x.key,y.key);
		else System.out.printf("edge (%d,%d)\n not in valid class=%d",x.key,y.key,c);	
	}
	
	static void initialize_search(TopSorter g)	{
		for(Vertex v : g.vertices)		{
			v.processed = v.discovered = false;
			v.parent = null;
		}
	}
	
	static final int TREE = 1, BACK = 2, FORWARD = 3, CROSS = 4;
	static int timer = 0;
	
	static int edge_classification(Vertex x, Vertex y)	{
		if (y.parent == x) return(TREE);
		if (y.discovered && !y.processed) return(BACK);
		if (y.processed && (y.entry_time > x.entry_time)) return(FORWARD);
		if (y.processed && (y.entry_time < x.entry_time)) return(CROSS);
		System.out.printf("Warning: self loop (%d,%d)\n",x,y);
		return -1;
	}
   
	void insert_edge(int x, int y, boolean directed) 	{
		Vertex one = vertices.get(x-1);
      Vertex two = vertices.get(y-1);
      one.edges.add(two);      
		vertices.get(x-1).degree++;
		if(!directed)
			insert_edge(y,x,true);
		else
			nedges++;
         two.inDegree++;
	}
   void remove_edge(Vertex x, Vertex y)  {
		if(x.degree<0)
			System.out.println("Warning: no edge --" + x + ", " + y);
		x.edges.remove(y);
		x.degree--;
	} 

	void print_graph()	{
		for(Vertex v : vertices)	{
			System.out.println("vertex: "  + v.key);
			for(Vertex w : v.edges)
				System.out.print("  adjacency list: " + w.key);
			System.out.println();
		}
	} 

   class Vertex implements Comparable<Vertex> {
      int key;
      int inDegree;
      int degree;
      int component;
      int color = -1;    // use mod numColors, -1 means not colored
      boolean discovered = false;
      boolean processed = false;
      int entry_time = 0;
      int exit_time = 0;
      Vertex parent = null;
      ArrayList<Vertex> edges = new ArrayList<Vertex>();
   
      public Vertex(int tkey){ 
         key = tkey;
      }
      public int compareTo(Vertex other){
         if (this.key > other.key){
            return 1;
         }         else if (this.key < other.key) {
            return -1;
         }
         else 
            return 0;
         }
      }

	Vertex unProcessedV(){	   
      for(Vertex v:  vertices)  {
         if (! v.processed ) return v;
      }
   return null;	
   }
}