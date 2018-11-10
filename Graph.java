import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Graph {	
	Vertex[][] graph;
	//Vertex currentVert;
	Vertex[] vertexList;
	int dimension;
	int total_Verts;
	
	//Constructor
	public Graph(int dimension) {
		this.dimension=dimension;
		total_Verts=dimension*dimension;
		//adjList=new Node[total_Verts];
		
		
		//Create and fill graph
		graph=new Vertex[dimension][dimension];
		vertexList=new Vertex[total_Verts];
		int value=1;
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				Vertex v=new Vertex(value);
				vertexList[value-1]=v;
				graph[i][j]=v;
				value++;
			}
		}
	}
	
	/**
	 * Sets the adjacency list for each vertex
	 */
	public void setNeighbors() {
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				//add top neighbor if not in first row
				if(j!=0)
					graph[i][j].adjList.add(graph[i][j-1]);
				//add bottom neighbor if not on last row
				if(j!=dimension-1)
					graph[i][j].adjList.add(graph[i][j+1]);
				//add left neighbor if not in first column of graph
				if(i!=0)
					graph[i][j].adjList.add(graph[i-1][j]);
				//add right neighbor if not in last column
				if(i!=dimension-1)
					graph[i][j].adjList.add(graph[i+1][j]);
			}
		}
	}
	
	public void breakWall(Vertex current, Vertex neighbor) {
		if(current.label+1==neighbor.label) {
			current.RIGHT=false;
			neighbor.LEFT=false;
		}
		else if(current.label-1==neighbor.label) {
			current.LEFT=false;
			neighbor.RIGHT=false;
		}
		else if(current.label+dimension==neighbor.label) {
			current.BOTTOM=false;
			neighbor.TOP=false;
		}
		else {
			current.TOP=false;
			neighbor.BOTTOM=false;
		}		
	}
	
	public void dfsMaze(Vertex current) {
		Stack<Vertex> stack=new Stack<Vertex>();
		int visited=0;	//Number of vertexes visited
		
		while(visited<total_Verts) {
			if(!current.adjList.isEmpty()) {	//if vertex has unvisited neighbors
				Vertex neighbor=current.getNeighbor();
				breakWall(current, neighbor);
				stack.push(current);	//visit other neighbors if any
				current=neighbor;	//Go to next node
				visited++;
			}
			else {
				current=stack.pop();
			}		
		}
	}
	
	public void createMaze(Vertex v){
		Queue<Vertex> q=new LinkedList<>();
		q.add(v);
		while(!q.isEmpty()) {
			Vertex next=q.remove();
			
			for(int i=0;i<next.adjList.size()-1;i++) {
				Vertex neighbor=next.adjList.get(i);
				
			}
		}
	}
	
	public String printMaze() {
		String maze="";
		for(int i=0;i<total_Verts;i++) {

		}
	}
	
	//Vertex of graph
	static class Vertex{
		LinkedList<Vertex> adjList;	//List of neighbors
		int color;	//WHITE=0, GREY=1, BLACK=2
		int label;	//Vertex number 1...n
		boolean TOP=true;	//Walls
		boolean RIGHT=true;
		boolean BOTTOM=true;
		boolean LEFT=true;
		
		public Vertex(int label) {
			this.label=label;
			color=0;
		}
		
		public Vertex getNeighbor() {
			//if white turn grey
			if(color==0)
				color=1;
			
			Random random=new Random();
			Vertex neighbor=adjList.get(random.nextInt(adjList.size()-1));	//select random neighbor
			adjList.remove(neighbor);	//remove from adjacency list
			
			return neighbor;
		}
	}
}
