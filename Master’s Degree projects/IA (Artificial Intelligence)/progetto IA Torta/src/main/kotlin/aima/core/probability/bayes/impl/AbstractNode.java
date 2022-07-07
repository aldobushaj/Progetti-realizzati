package aima.core.probability.bayes.impl;



import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.ConditionalProbabilityDistribution;
import aima.core.probability.bayes.Node;

/**
 * Abstract base implementation of the Node interface.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public abstract class AbstractNode implements Node, Serializable {
	private RandomVariable variable = null;

	public void setParents(Set<Node> parents) {
		this.parents = parents;
	}

	public void addParent(Node parent) {
		this.parents.add(parent);
	}
	public void removeParent(Node parent) {
		this.parents.remove(parent);
	}
	public void addChildren(Node child) {
		this.children.add(child);
	}
	public void removeChildren(Node child) {
		this.children.remove(child);
	}


	private Set<Node> parents = null;

	public void setChildren(Set<Node> children) {
		this.children = children;
	}

	private Set<Node> children = null;

	public AbstractNode(RandomVariable var) {
		this(var, (Node[]) null);
	}

	public AbstractNode(RandomVariable var, Node... parents) {
		if (null == var) {
			throw new IllegalArgumentException(
					"Random Variable for Node must be specified.");
		}
		this.variable = var;
		this.parents = new LinkedHashSet<Node>();
		if (null != parents) {
			for (Node p : parents) {
				((aima.core.probability.bayes.impl.AbstractNode) p).addChild(this);
				this.parents.add(p);
			}
		}
		this.children = new LinkedHashSet<>();
	}

	//
	// START-Node
	@Override
	public RandomVariable getRandomVariable() {
		return variable;
	}

	@Override
	public boolean isRoot() {
		return 0 == getParents().size();
	}

	@Override
	public Set<Node> getParents() {
		return parents;
	}

	@Override
	public Set<Node> getChildren() {
		return children;
	}

	@Override
	public Set<Node> getMarkovBlanket() {
		LinkedHashSet<Node> mb = new LinkedHashSet<Node>();
		// Given its parents,
		mb.addAll(getParents());
		// children,
		mb.addAll(getChildren());
		// and children's parents
		for (Node cn : getChildren()) {
			mb.addAll(cn.getParents());
		}

		return mb;
	}

	public abstract ConditionalProbabilityDistribution getCPD();

	// END-Node
	//

	@Override
	public String toString() {
		return getRandomVariable().getName();
	}

	@Override
	public boolean equals(Object o) {
		if (null == o) {
			return false;
		}
		if (o == this) {
			return true;
		}

		if (o instanceof Node) {
			Node n = (Node) o;

			return getRandomVariable().equals(n.getRandomVariable());
		}

		return false;
	}

	@Override
	public int hashCode() {
			if(variable!=null)
				return variable.hashCode();
			else
				return 0;

	}

	//
	// PROTECTED METHODS
	//
	protected void addChild(Node childNode) {
		children = new LinkedHashSet<Node>(children);

		children.add(childNode);

	}
}
