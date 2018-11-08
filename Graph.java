import java.util.LinkedList;

public class Graph {
	//Vertex of graph
	class Vertex{
		LinkedList<Vertex> adjList;	//List of neighbors
		int color;	//WHITE=0, GREY=1, BLACK=2
		String value;	
		int row;	//row of vertex
		int col;	//column of vertex
		
		
		public Vertex(int row, int col) {
			this.row=row;
			this.col=col;
			this.value="";
			adjList=new LinkedList<>();
			color=0;
		}
	}
	
	private final int MAX_VERTS=20;
	private Vertex vertexList[];
	private int adjMatrix[][];
	private int n;
	
	public Graph(int dimension) {
		vertexList=new Vertex[dimension*dimension];
		adjMatrix= new int[dimension][dimension];
		n=adjMatrix.length;
		
		for(int i=0; i<MAX_VERTS; i++)
			for(int j=0; j<MAX_VERTS;j++)
				adjMatrix[i][j]=0;
	}
	
	public void addVertex(char lab) {
		vertexList[n++]=new Vertex(lab);
	}
	
	public void addEdge(int start, int end) {
		adjMatrix[start][end]=1;
		adjMatrix[end][start]=1;
	}
	
	public void displayVertex(int v) {
		System.out.print(vertexList[v].label);
	}
}
