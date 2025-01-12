package aima.core.agent;

import aima.core.agent.EnvironmentObject;

import java.util.Optional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.1, page 35.<br>
 * 
 * Figure 2.1 Agents interact with environments through sensors and actuators.
 *
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Agent<P, A> extends EnvironmentObject {
	/**
	 * Call the Agent's program, which maps any given percept sequences to an
	 * action.
	 * 
	 * @param percept
	 *      	The current percept of a sequence perceived by the Agent.
	 * @return 	The Action to be taken in response to the currently perceived
	 *         	percept. Empty replaces NoOp in earlier implementations.
	 */
	Optional<A> act(P percept);

	/**
	 * Life-cycle indicator as to the liveness of an Agent.
	 * 
	 * @return 	Value true if the Agent is to be considered alive, false otherwise.
	 */
	boolean isAlive();

	/**
	 * Set the current liveness of the Agent.
	 * 
	 * @param alive
	 *        	Set to true if the Agent is to be considered alive, false
	 *       	otherwise.
	 */
	void setAlive(boolean alive);
}
