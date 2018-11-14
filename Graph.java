import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Graph {	
	Vertex[][] graph;
	Vertex[] vertexList;
	Vertex[][] adjList;
	int dimension;
	int total_Verts;
	
	//Constructor
	public Graph(int dimension) {
		this.dimension=dimension;
		total_Verts=dimension*dimension;
		adjList=new Vertex[total_Verts][4];
		
		
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
		graph[0][0].TOP=false;
		graph[dimension-1][dimension-1].BOTTOM=false;
		
		//graph[0][dimension-1].TOP=false;
	}
	
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
	 * Sets the adjacency list for each vertex
	 */
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
	
	public void createMaze() {
		Stack<Vertex> stack=new Stack<>();
		int visited=1;	//Number of vertexes visited
		Vertex current=graph[0][0];
		
		while(visited<total_Verts) {
			if(!current.adjList.isEmpty()&&current.whiteNeighbors()) {	//if vertex has neighbors
				System.out.print("a");
				Vertex neighbor=current.getNeighbor();
				breakWall(current, neighbor);
				stack.push(current);	//visit other neighbors if any
				current=neighbor;	//Go to next node
				visited++;
			}
			else {
				System.out.print("b");
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
		Vertex current=graph[0][0];	//starting point
		Stack<Vertex> stack=new Stack<Vertex>();
		stack.push(current);
		int step=0;	//The number of steps taken to get to vertex
		
		while(!stack.isEmpty() && current.label!=total_Verts) {	//Stack is not empty and has not reached exit
			current=stack.pop();	
			current.color2=2;	//Make vertex black do its not visited again
			current.setStep(step);	//set step for this vertex
			step+=1;	//increment step
			
			for(int i=0;i<current.adjList2.size()-1;i++) {	//Look at each neighbor
				Vertex next=getVertex(current.adjList2.get(i).label);
				if(next.color2==0) {	//If neighbor is white add it to stack
					stack.push(next);
				}
			}
		}
	}
	
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
		LinkedList<Vertex> adjList;	//List of neighbors
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
			this.adjList = new LinkedList<Vertex>();
			this.adjList2 = new LinkedList<Vertex>();
			this.label=label;
			step="1";
			color=0;
			color2=0;
		}
		
		public void setStep(int n) {
			step=Integer.toString(n);
		}
		
		public String getStep() {
			return step;
		}
		
		public boolean whiteNeighbors() {
			for(int i=0;i<this.adjList.size();i++) {
				if(adjList.get(i).color==0)
					return true;
			}
			return false;
		}
		
		public Vertex getNeighbor() {
			Vertex[] white=new Vertex[4];	//create array list for white neighbors
			int index=0;
			for(int i=0;i<this.adjList.size();i++) {
				if(!this.adjList.isEmpty()&&this.adjList.get(i).color==0) {
					white[index]=this.adjList.get(i);
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
	
	public static void main(String[] args) {
		Graph g = new Graph(3);
		System.out.println(g.getVertex(1).step);
		g.getVertex(1).setStep(3);
		System.out.println(g.getVertex(1).step);
		g.setNeighbors();
		System.out.print(g.getAdjList());
		System.out.println(g.printMaze());
		
		g.createMaze();
		g.solveDFS();
		System.out.print(g.getAdjList());
		System.out.print(g.printMaze());
	}
}
