package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.impl.ObjectWithDynamicAttributes;
import aima.core.agent.impl.aprog.simplerule.Condition;

/**
 * Implementation of an AND condition.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class ANDCondition extends Condition {
	private Condition left;

	private Condition right;

	public ANDCondition(Condition leftCon, Condition rightCon) {
		assert (null != leftCon);
		assert (null != rightCon);

		left = leftCon;
		right = rightCon;
	}

	@Override
	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return (left.evaluate(p) && right.evaluate(p));
	}

	@Override
	public String toString() {
		return "[" + left + " && " + right + "]";
	}
}
