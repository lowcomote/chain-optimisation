# chain-optimisation
A prototype for optimisation of transformation chains built on-the-top of Epsilon Transformation Language.

1. Download the latest version of Eclipse and select the **Eclipse IDE for Eclipse Committers** option when prompted during the installation process.

2. Clone the Git repository: **git://git.eclipse.org/gitroot/epsilon/org.eclipse.epsilon.git** (**master** branch).

3. Import all the projects under the plugins, features, and tests folders in your workspace.

4. Clone the static analysis git repository: **https://github.com/epsilonlabs/static-analysis.git** (**master** branch)

5. Import all the projects in your workspace.

6. Open **releng/org.eclipse.epsilon.target/org.eclipse.epsilon.target.target** and click the **Set as Active Target Platform** link on the top right

7. Clone this repository master branch and import all plugins in your workspace.

8. Right-click on org.eclipse.epsilon.chain.selection/src/org.eclipse.epsilon.chain.selection/Chain_MT.java in the Project Explorer and select Run as → Java Application.


**For Extracting Dependency Graph:**

1. **EtlDependencyGraphGenerator** generates a dependency graph by adding information such as traceableBy, needTraceOf to the data field of each transformation rule.

2. For understanding which statement need to be removed, we need to keep hold of the line numbers which are responsible for each dependency, that information is present in a HashMap called as lineNumber.

For example,
1. rule Node2TraceLink
2.	transform n:Graph!Node
3.	to t:SimpleTrace!TraceLink {
4.	}
5. 
6. rule Edge2TraceLink
7. 	transform e:Graph!Edge
8.	  to s:SimpleTrace!TraceLink {
9.	  s.sources=e.source.equivalent();
10. }
 
 rule Edge2Tracelink's data field would be like this: needTraceOf = Node2TraceLink, traceableBy= {}
 linNumber would contain  
 Edge2TraceLink, Node2TraceLink.  9 <- indicates the lineNumber
 
 **For Chain Execution**
 Run \src\org\eclipse\epsilon\etl\chain\optimisation\EtlChainOptimiser.java and press 0
 
 **For Chain Identification**
 Run \src\org\eclipse\epsilon\etl\chain\optimisation\EtlChainOptimiser.java and press 11
 
 **For running chain optimization code**
 
1. Run \src\org\eclipse\epsilon\etl\chain\optimisation\EtlChainOptimiser.java and press 5 to run the findTransformationRuleIndex() function in order to get the  optimized version of ETL files.
 
2. We can then run the normal and optimized version of the file by re-running Run \src\org\eclipse\epsilon\etl\chain\optimisation\EtlChainOptimiser.java and choosing case no. 101 and 102 respectively.
  
