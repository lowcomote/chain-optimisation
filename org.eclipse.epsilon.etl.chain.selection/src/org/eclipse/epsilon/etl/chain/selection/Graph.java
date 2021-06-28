package org.eclipse.epsilon.etl.chain.selection;

import java.util.ArrayList;

public class Graph {
	
	ArrayList<ArrayList<String>> graph;
	int v;
	Graph(int nodes)
	{
		v=nodes;
		graph = new ArrayList<ArrayList<String>>();
		for(int i=0; i<v; i++) {
			graph.add(new ArrayList<String>());
		}
	}
	
	void addEdge(int u, String v) 
	{
		graph.get(u).add(v);
		//graph.getClass();
		
	}
		
	void printGraph()
	{
		for(int i=0;i<v;i++)
		{
			System.out.print("Metamodel "+(i+1));
			for(String x:graph.get(i))
				System.out.print(" -> "+x);
			System.out.println("\n");
		}
	}

}
