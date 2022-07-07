package aima.core.agent.impl;

import aima.core.agent.State;
import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * @author Ciaran O'Reilly
 */
public class DynamicState extends ObjectWithDynamicAttributes implements State {
	public DynamicState() { }

	@Override
	public String describeType() {
		return State.class.getSimpleName();
	}
}