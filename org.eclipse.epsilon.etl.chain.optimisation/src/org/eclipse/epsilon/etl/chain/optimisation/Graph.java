package org.eclipse.epsilon.etl.chain.optimisation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
class Node
{
	String nodeName;
	Node(String name)
	{
		this.nodeName = name;
	}
}
*/

class Graph
{
	HashMap<String, ArrayList<String>> adjList;
	
	Graph(List<String> list)
	{
		this.adjList = new HashMap<String, ArrayList<String>>();
		for(String n:list)
			adjList.put(n, new ArrayList<String>());
	}
	
	void addEdge(String node1, String node2)
	{
		adjList.get(node1).add(node2);
		//adjList.get(node2).add(node1);
	}

	void removeEdge(String node1, String node2)
	{
		ArrayList<String> node1List = adjList.get(node1);
		ArrayList<String> node2List = adjList.get(node2);
			
		node1List.remove(node2);
		node2List.remove(node1);	
	}
	
	HashMap<String, ArrayList<String>> printAdjList()
	{
		for (Map.Entry mapElement : adjList.entrySet()) {
			String n = (String)mapElement.getKey();
            ArrayList<String> list = adjList.get(n);
            if(list.size()>0)
            {
            	 System.out.print(n + "->");
                 for(String a : list)
                 	System.out.print(a + " ");
                 System.out.println();
            }
        
		}
		return adjList;
	}
}






