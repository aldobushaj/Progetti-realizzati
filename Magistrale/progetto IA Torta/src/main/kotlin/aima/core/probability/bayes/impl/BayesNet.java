package aima.core.probability.bayes.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.FullCPTNode;

/**
 * Default implementation of the BayesianNetwork interface.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class BayesNet implements BayesianNetwork,Cloneable, Serializable {
	// Metodi utili a clonare i nodi e le relative Liste/Map
	@Override
	public Object clone() throws CloneNotSupportedException {
		aima.core.probability.bayes.impl.BayesNet cloned = (aima.core.probability.bayes.impl.BayesNet) super.clone();
		cloned.setRootNodes((Set<Node>)clone(cloned.getRootNodes()));

		cloned.setVariables((List<RandomVariable>)clone(cloned.getVariables()));
		cloned.setVarToNodeMap((Map<RandomVariable, Node>)clone(cloned.getVarToNodeMap()));
		return cloned;
	}
	public static<T> Set<Node> clone(Set<T> original) {
		Set<T> copy = original.stream()
				.collect(Collectors.toSet());
		Set<Node> clones=new HashSet<>();
		for (T node: copy
			 ) {
			Node cloneNode;
			try {
				cloneNode=(Node) ((FullCPTNode)node).clone();
				clones.add(cloneNode);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return clones;
	}
	public static<T> List<T> clone(List<T> original) {
		List<T> copy = original.stream()
				.collect(Collectors.toList());
		return copy;
	}
	public static<K,V> Map<K,V> clone(Map<K,V> original) {
		Map<K,V> copy = new HashMap<>();
		copy.putAll(original);
		return copy;
	}

	public Set<Node> getRootNodes() {
		return rootNodes;
	}

	public Set<Node> rootNodes = new LinkedHashSet<Node>();

	public void setVariables(List<RandomVariable> variables) {
		this.variables = variables;
	}

	public Map<RandomVariable, Node> getVarToNodeMap() {
		return varToNodeMap;
	}

	public void setVarToNodeMap(Map<RandomVariable, Node> varToNodeMap) {
		this.varToNodeMap = varToNodeMap;
	}

	public List<RandomVariable> variables = new ArrayList<RandomVariable>(); // returned by getVariabledInTopologicalOrder
	public Map<RandomVariable, Node> varToNodeMap = new HashMap<RandomVariable, Node>();

	public BayesNet(Node... rootNodes) {
		if (null == rootNodes) {
			throw new IllegalArgumentException(
					"Root Nodes need to be specified.");
		}
		for (Node n : rootNodes) {
			this.rootNodes.add(n);
		}
		if (this.rootNodes.size() != rootNodes.length) {
			throw new IllegalArgumentException(
					"Duplicate Root Nodes Passed in.");
		}
		// Ensure is a DAG
		checkIsDAGAndCollectVariablesInTopologicalOrder();
		//variables = Collections.unmodifiableList(variables);
	}
	public void removeNode(List<Node> toRemove){
		for(  Node e : toRemove){

			rootNodes.remove(e);
			rootNodes.addAll(e.getChildren());
			System.out.println("Variables prima: "+variables.toString());
			variables.remove(getRandomVariable(e));
			System.out.println("Variables dopo: "+variables.toString());
			System.out.println("Map prima: "+varToNodeMap.toString());
			varToNodeMap.values().remove(e);
			System.out.println("Map dopo: "+varToNodeMap.toString());
		}
	}


	//
	// START-BayesianNetwork
	@Override
	public List<RandomVariable> getVariablesInTopologicalOrder() {
		return variables;
	}

	@Override
	public Node getNode(RandomVariable rv) {
		return varToNodeMap.get(rv);
	}

	//get Random Variable by Node
	public RandomVariable getRandomVariable(Node n){
		return getKeyByValue(varToNodeMap,n);
	}
	// Get key given value
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Map.Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	//Return all the Nodes of the Bayesan Newtwork
	public List<Node> getAllNodes()
	{ List<Node> nodeList = new ArrayList<>();
		for (RandomVariable n:variables
			 ) {
			nodeList.add(varToNodeMap.get(n));
		}
		return nodeList;
	}

	// END-BayesianNetwork
	//

	//
	// PRIVATE METHODS
	//
	private void checkIsDAGAndCollectVariablesInTopologicalOrder() {

		// Topological sort based on logic described at:
		// http://en.wikipedia.org/wiki/Topoligical_sorting
		Set<Node> seenAlready = new HashSet<Node>();
		Map<Node, List<Node>> incomingEdges = new HashMap<Node, List<Node>>();
		Set<Node> s = new LinkedHashSet<Node>();
		for (Node n : this.rootNodes) {
			walkNode(n, seenAlready, incomingEdges, s);
		}
		while (!s.isEmpty()) {
			Node n = s.iterator().next();
			s.remove(n);
			variables.add(n.getRandomVariable());
			varToNodeMap.put(n.getRandomVariable(), n);
			for (Node m : n.getChildren()) {
				List<Node> edges = incomingEdges.get(m);
				edges.remove(n);
				if (edges.isEmpty()) {
					s.add(m);
				}
			}
		}

		for (List<Node> edges : incomingEdges.values()) {
			if (!edges.isEmpty()) {
				throw new IllegalArgumentException(
						"Network contains at least one cycle in it, must be a DAG.");
			}
		}
	}

	private void walkNode(Node n, Set<Node> seenAlready,
			Map<Node, List<Node>> incomingEdges, Set<Node> rootNodes) {
		if (!seenAlready.contains(n)) {
			seenAlready.add(n);
			// Check if has no incoming edges
			if (n.isRoot()) {
				rootNodes.add(n);
			}
			incomingEdges.put(n, new ArrayList<Node>(n.getParents()));
			for (Node c : n.getChildren()) {
				walkNode(c, seenAlready, incomingEdges, rootNodes);
			}
		}
	}



	//--------------------- METODI AGGIUNTI -------------------------------------
	public void setRootNodes(Set<Node> rootNodes) {
		this.rootNodes = rootNodes;
	}
	public List<RandomVariable> getVariables() {
		return variables;
	}

}
