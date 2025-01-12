package aima.core.agent.impl;

import aima.core.agent.EnvironmentState;
import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class DynamicEnvironmentState extends ObjectWithDynamicAttributes
		implements EnvironmentState {
	public DynamicEnvironmentState() { }

	@Override
	public String describeType() {
		return EnvironmentState.class.getSimpleName();
	}
}