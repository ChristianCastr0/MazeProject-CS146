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
				if(value==total_Verts) {
					graph[i][j].BOTTOM=false;
				}
				value++;
			}
		}
	}
	
	/**
	 * Sets the adjacency list for each vertex
	 */
	/*public void setNeighbors() {
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				//add top neighbor if not in first row
				if(j!=0)
					graph[i][j].adjList.add(graph[i][j-1]);
				//add bottom neighbor if not on last row
				if(j!=(dimension-1))
					graph[i][j].adjList.add(graph[i][j+1]);
				//add left neighbor if not in first column of graph
				if(i!=0)
					graph[i][j].adjList.add(graph[i-1][j]);
				//add right neighbor if not in last column
				if(i!=dimension-1)
					graph[i][j].adjList.add(graph[i+1][j]);
			}
		}
	}*/
	
	public void setNeighbors() {
		for(int i=0;i<dimension;i++) {
			for(int j=0;j<dimension;j++) {
				//add top neighbor if not in first row
				if(i!=0)
					graph[i][j].adjList.add(graph[i-1][j]);
				//add bottom neighbor if not on last row
				if(i!=(dimension-1))
					graph[i][j].adjList.add(graph[i+1][j]);
				//add left neighbor if not in first column of graph
				if(j!=0)
					graph[i][j].adjList.add(graph[i][j-1]);
				//add right neighbor if not in last column
				if(j!=dimension-1)
					graph[i][j].adjList.add(graph[i][j+1]);
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
	
	public void createMaze() {
		Stack<Vertex> stack=new Stack<Vertex>();
		int visited=1;	//Number of vertexes visited
		Vertex current=graph[0][0];
		
		while(visited<total_Verts) {
			if(!current.adjList.isEmpty()) {	//if vertex has neighbors
				Vertex neighbor=current.getNeighbor(current);
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
	
	//not done
	public void solveBFS(){
		Queue<Vertex> q=new LinkedList<>();
		q.add(vertexList[0]);
		while(!q.isEmpty()) {
			Vertex next=q.remove();
			
			for(int i=0;i<next.adjList.size()-1;i++) {
				Vertex neighbor=next.adjList.get(i);
			}
		}
	}
	
	public void solveDFS() {
		Vertex current=vertexList[0];	//starting point
		Stack<Vertex> stack=new Stack<>();
		stack.push(current);
		int step=0;	//The number of steps taken to get to vertex
		
		while(!stack.isEmpty() && current.label!=vertexList.length-1) {	//Stack is not empty and has not reached exit
			current=stack.pop();	//
			current.color=2;	//Make vertex black do its not visited again
			current.step=step;	//set step for this vertex
			step+=1;	//increment step
			
			for(int i=0;i<current.adjList.size()-1;i++) {	//Look at each neighbor
				Vertex next=current.adjList.get(i);
				if(next.color==0) {	//If neighbor is white add it to stack
					stack.push(next);
				}
			}
		}
	}
	
	public String printMaze() {
        String maze = "";
        int vertex=0;
		for(int row=1;row<(dimension*2)+2;row++) {
			for(int col=0;col<=dimension;col++) {
				if(row%2==0) {	//if "|" must be printed
					if(vertexList[vertex].LEFT)
						maze+="| ";
					else
						maze+=" ";
					if(col==dimension&&vertexList[vertex].RIGHT)
						maze+="|";
				}
				else if(row==dimension*2+1) {
					if(vertexList[vertex].BOTTOM)
						maze+="+-";
					else
						maze+="+ ";
				}
				else {	//if + and - must be printed
					if(vertexList[vertex].TOP)
						maze+="+-";
					else
						maze+="+ ";
				}
			}
			vertex++;
			if(row%2!=0)
				maze+="+";
			maze+="\n";
		}
		return maze;
	}  
	
	//Vertex of graph
	static class Vertex{
		LinkedList<Vertex> adjList;	//List of neighbors
		int color;	//WHITE=0, GREY=1, BLACK=2
		int label;	//Vertex number 1...n
		int step;	//Steps taken to get to  this vertex
		boolean TOP=true;	//Walls
		boolean RIGHT=true;
		boolean BOTTOM=true;
		boolean LEFT=true;
		
		public Vertex(int label) {
			adjList = new LinkedList<Vertex>();
			this.label=label;
			color=0;
		}
		
		public Vertex getNeighbor(Vertex v) {
			Vertex[] white=new Vertex[4];	//create array list for white neighbors
			int index=0;
			for(int i=0;i<v.adjList.size();i++) {
				if(v.adjList.get(i).color==0) {
					white[index]=v.adjList.get(i);
					index++;
				}
			}
			
			Random random=new Random();
			Vertex neighbor=white[random.nextInt(adjList.size())];	//select random neighbor
			adjList.remove(neighbor);	//remove from adjacency list
			return neighbor;
		}
	}
	
	public static void main(String[] args) {
		Graph g = new Graph(3);
		g.setNeighbors();
		g.createMaze();
		System.out.print(g.printMaze());
		/*for(int i=0;i<g.dimension;i++) {
			for(int j=0;j<g.dimension;j++)
				System.out.println(g.graph[i][j].label);
			System.out.println();
		}*/
		
	}
}
