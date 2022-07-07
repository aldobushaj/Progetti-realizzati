package aima.core.probability.bayes.impl;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.ConditionalProbabilityTable;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.Node;
import aima.core.probability.bayes.impl.AbstractNode;
import aima.core.probability.bayes.impl.CPT;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Default implementation of the FiniteNode interface that uses a fully
 * specified Conditional Probability Table to represent the Node's conditional
 * distribution.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class FullCPTNode extends AbstractNode implements FiniteNode, Cloneable, Serializable {
	private ConditionalProbabilityTable cpt = null;
	public Boolean isClone=false;
	public Boolean getVisited() {
		return visited;
	}

	public void setVisited() {
		this.visited = true;
	}

	Boolean visited=false;

	public List<Node> getMarriedNodes() {
		return marriedNodes;
	}

	public void setMarriedNodes(List<Node> reachableNodes) {
		this.marriedNodes = reachableNodes;
	}
	public void addMarriedNode(List<Node> marriedNode) {
		this.marriedNodes.addAll(marriedNode);
	}
	public void addSingleMarriedNodeNode (Node marriedNode) {
		this.marriedNodes.add(marriedNode);
	}
	public Set<Node> getNeighbours(){
		Set <Node> neighbours = new HashSet<>();
		neighbours.addAll(marriedNodes);
		neighbours.addAll(getParents());
		neighbours.addAll(getChildren());
		return neighbours;
	}
	public void removeMarriedNode(Node divorceNode) {
		this.marriedNodes.remove(divorceNode);
	}

	List<Node> marriedNodes =new ArrayList<>();

	public FullCPTNode(RandomVariable var, double[] distribution) {
		this(var, distribution, (Node[]) null);
	}

	public FullCPTNode(RandomVariable var, double[] values, Node... parents) {
		super(var, parents);

		RandomVariable[] conditionedOn = new RandomVariable[getParents().size()];
		int i = 0;
		for (Node p : getParents()) {
			conditionedOn[i++] = p.getRandomVariable();
		}

		cpt = new CPT(var, values, conditionedOn);
	}

	//
	// START-Node
	@Override
	public ConditionalProbabilityDistribution getCPD() {
		return getCPT();
	}

	// END-Node
	//

	//
	// START-FiniteNode

	@Override
	public ConditionalProbabilityTable getCPT() {
		return cpt;
	}
	public boolean setCPT(RandomVariable var,List<Node> evidence, double[] values, Node... parents){
		RandomVariable[] conditionedOn = new RandomVariable[getParents().size()];
		int i = 0;
		// Ottengo un array con tutti i nodi parents
		for (Node p : this.getParents()) {
			conditionedOn[i++] = p.getRandomVariable();
		}
		// Aggiorno la CPT
		this.cpt = new CPT(var,values,conditionedOn);
		return true;
	}
	public Object clone() throws
			CloneNotSupportedException
	{
		aima.core.probability.bayes.impl.FullCPTNode cloned = (aima.core.probability.bayes.impl.FullCPTNode) super.clone(); //shallow copy
		cloned.setMarriedNodes(clone(cloned.marriedNodes)); // clono i married nodes
		cloned.setParents(clone(cloned.getParents()));
		cloned.setChildren(clone(cloned.getChildren()));


		return super.clone();
	}

	public static<T> Set<T> clone(Set<T> original) {
		Set<T> copy = original.stream()
				.collect(Collectors.toSet());
		return copy;
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

	public void selectDomain(List<Node> interestedParents, Object... domain) {
		CategoricalDistribution conditioningCase = cpt.getConditioningCase(domain);
		double[] values = conditioningCase.getValues();
		RandomVariable[] vars = this.getParents().stream().filter(p -> !interestedParents.contains(p)).map(Node::getRandomVariable).toArray(RandomVariable[]::new);
		this.cpt = new CPT(this.getRandomVariable(), values, vars);
	}
	// END-FiniteNode
	//
}
