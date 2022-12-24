package org.eclipse.epsilon.etl.chain.selection;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Node
{
	String nodeName;
	Node(String name)
	{
		this.nodeName = name;
	}
}


public class Graph
{
	HashMap<Node, ArrayList<Node>> adjList;
	
	Graph(List<Node> list)
	{
		this.adjList = new HashMap<Node, ArrayList<Node>>();
		for(Node n:list)
			adjList.put(n, new ArrayList<Node>());
	}
	
	void addEdge(Node node1, Node node2)
	{
		adjList.get(node1).add(node2);
		//adjList.get(node2).add(node1);
	}

	void removeEdge(Node node1, Node node2)
	{
		ArrayList<Node> node1List = adjList.get(node1);
		ArrayList<Node> node2List = adjList.get(node2);
			
		node1List.remove(node2);
		node2List.remove(node1);	
	}
	
	void printAdjList()
	{
		for (Map.Entry mapElement : adjList.entrySet()) {
            Node n = (Node)mapElement.getKey();
            ArrayList<Node> list = adjList.get(n);
            if(list.size()>0)
            {
            	 System.out.print(n.nodeName + "->");
                 for(Node a : list)
                 	System.out.print(a.nodeName + " ");
                 System.out.println();
            }
        
	}
	}
}






