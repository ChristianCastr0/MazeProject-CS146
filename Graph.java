import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * Program that represents a nXn graph that can be altered to represent a perfect maze
 * @author Christian Castro & Hibo Osman
 * @version 1.0	11/14/18
 */
public class Graph {	
	Vertex[][] graph;
	int dimension;
	int total_Verts;
	
	//Constructor
	public Graph(int dimension) {
		this.dimension=dimension;
		total_Verts=dimension*dimension;
		
		//Create and fill graph
		graph=new Vertex[dimension][dimension];
		int value=1;
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				Vertex v=new Vertex(value);
				graph[i][j]=v;
				value++;
			}
		}
		//Break walls for start and end of maze
		graph[0][0].TOP=false;
		graph[dimension-1][dimension-1].BOTTOM=false;
	}
	
	/**
	 * Gets vertex in graph based on the given label
	 * @param n	Label of the vertex
	 * @return	Vertex v that corresponds to the label given in the parameter
	 */
	public Vertex getVertex(int n) {
		Vertex v=null;
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				if(graph[i][j].label==n)
					v=graph[i][j];
			}
		}
		return v;
	}
	
	/**
	 * Sets the walls for each vertex
	 */
	public void setNeighbors() {
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				//add top wall if not in first row
				if(i!=0)
					graph[i][j].wallList.add(graph[i-1][j]);
				//add bottom wall if not on last row
				if(i!=(dimension-1))
					graph[i][j].wallList.add(graph[i+1][j]);
				//add left wall if not in first column of graph
				if(j!=0)
					graph[i][j].wallList.add(graph[i][j-1]);
				//add right wall if not in last column
				if(j!=dimension-1)
					graph[i][j].wallList.add(graph[i][j+1]);
			}
		}
	}
	
	/**
	 * Gets the adjacency list of each vertex in the graph
	 * @return	List of adjacency lists for each vertex
	 */
	public String getAdjList() {
		String list="";	
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				Vertex v=graph[i][j];
				for(int k=0;k<v.adjList2.size();k++)
					list+=v.adjList2.get(k).label+" ";
				list+="\n";
			}
		}
		return list;
	}
	
	/**
	 * Breaks wall between the 2 vertexes given in the parameter
	 * @param current Current vertex
	 * @param neighbor	Neighbor of the current vertex
	 */
	public void breakWall(Vertex current, Vertex neighbor) {
		//break right neighbors wall
		if(current.label+1==neighbor.label) {
			current.RIGHT=false;
			neighbor.LEFT=false;
		}
		//break left neighbors wall
		else if(current.label-1==neighbor.label) {
			current.LEFT=false;
			neighbor.RIGHT=false;
		}
		//break bottom neighbors wall
		else if(current.label+dimension==neighbor.label) {
			current.BOTTOM=false;
			neighbor.TOP=false;
		}
		//break top neighbors wall
		else {
			current.TOP=false;
			neighbor.BOTTOM=false;
		}	
	}
	
	/**
	 * Using the DFS method goes through each vertex and breaks walls to create a perfect maze
	 */
	public void createMaze() {
		Stack<Vertex> stack=new Stack<>();
		int visited=1;	//Number of vertexes visited
		Vertex current=graph[0][0];
		
		while(visited<total_Verts) {
			if(!current.wallList.isEmpty()&&current.whiteNeighbors()) {	//if vertex has neighbors
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
	
	/**
	 * Finds a path to the end of the maze using BFS method
	 */
	public void solveBFS(){
		Vertex currentV = graph[0][0];  // first position
		Queue<Vertex> q = new LinkedList<>();  
		q.add(currentV); // add to queue
		int step = 0;
		
		while (!q.isEmpty() && currentV.label!=total_Verts) {
			currentV = q.remove();  //remove then change color to black
			currentV.color2 = 2;     //fully explored
			currentV.setStep(step);
			step += 1;     
			
			for(int i=0;i<currentV.adjList2.size();i++) {	//check each neighbor
				Vertex next=getVertex(currentV.adjList2.get(i).label);
				if(next.color2==0) {	//if neighbor is white add it to queue
					q.add(next);
				}	
			}	
		}	
	}
	
	/**
	 * Finds path to the end of the maze using DFS method
	 */
	public void solveDFS() {
		Vertex current=graph[0][0];	//starting point
		Stack<Vertex> stack=new Stack<Vertex>();
		stack.push(current);
		int step=0;	//The number of steps taken to get to vertex
		
		while(!stack.isEmpty() && current.label!=total_Verts) {	//Stack is not empty and has not reached exit
			current=stack.pop();	
			current.color2=2;	//Make vertex black so it's not visited again
			current.setStep(step);	//set step for this vertex
			step+=1;	//increment step
			
			for(int i=0;i<current.adjList2.size();i++) {	//Look at each neighbor
				Vertex next=getVertex(current.adjList2.get(i).label);
				if(next.color2==0) {	//If neighbor is white add it to stack
					stack.push(next);
				}
			}
		}
	}
	
	/**
	 * Prints maze based on the status of each vertex's walls and step variable
	 * @return	String that contains maze pattern
	 */
	public String printMaze() {
        String maze = "";
        int r=0;
		for(int row=0;row<(dimension*2)+1;row++) {
			for(int col=0;col<dimension;col++) {
				if(row%2!=0) {	//if "|" must be printed
					if(graph[r][col].LEFT)
						maze+="|"+graph[r][col].step;
					else
						maze+=" "+graph[r][col].step;
					if(col==dimension-1) {
						maze+="|";
						r++;
					}
				}
				else if(row==dimension*2||r==dimension) {
					r=dimension-1;
					if(graph[r][col].BOTTOM)
						maze+="+-";
					else
						maze+="+ ";
				}
				else {	//if + and - must be printed
					if(graph[r][col].TOP)
						maze+="+-";
					else
						maze+="+ ";
				}
			}
			if(row%2==0)
				maze+="+";
			maze+="\n";
		}
		return maze;
	}  
	
	//Vertex of graph
	static class Vertex{
		LinkedList<Vertex> wallList;	//List of walls
		LinkedList<Vertex> adjList2;	//List of neighbors
		int color;	//WHITE=0, GREY=1, BLACK=2
		int color2;
		int label;	//Vertex number 1...n
		String step=" ";	//Steps taken to get to  this vertex
		boolean TOP=true;	//Walls
		boolean RIGHT=true;
		boolean BOTTOM=true;
		boolean LEFT=true;
		
		public Vertex(int label) {
			this.wallList = new LinkedList<Vertex>();
			this.adjList2 = new LinkedList<Vertex>();
			this.label=label;
			step=" ";
			color=0;
			color2=0;
		}
		
		/**
		 * Sets step of the vertex
		 * @param n Number that represent the step tajen to get to this vertex
		 */
		public void setStep(int n) {
			step=Integer.toString(n);
		}
		
		/**
		 * Gets the value of the step in the form of a string
		 * @return Step
		 */
		public String getStep() {
			return step;
		}
		
		/**
		 * Checks if the the current vertex has any white neighbors
		 * @return True or false depending on if there are any white neighbors
		 */
		public boolean whiteNeighbors() {
			for(int i=0;i<this.wallList.size();i++) {
				if(wallList.get(i).color==0)
					return true;
			}
			return false;
		}
		
		/**
		 * Creates an array of white neighbors and then randomly selects one of them
		 * @return A white neighbor
		 */
		public Vertex getNeighbor() {
			Vertex[] white=new Vertex[4];	//create array list for white neighbors
			int index=0;
			for(int i=0;i<this.wallList.size();i++) {
				if(!this.wallList.isEmpty()&&this.wallList.get(i).color==0) {
					white[index]=this.wallList.get(i);
					index++;
				}
			}
			
			Random random=new Random();
			Vertex neighbor=white[random.nextInt(index)];	//select random white neighbor
			//Update adjacency list and color of both vertices
			adjList2.add(neighbor);
			neighbor.adjList2.add(this);
			neighbor.color=1;
			this.color=1;
			
			return neighbor;
		}
	}
}
