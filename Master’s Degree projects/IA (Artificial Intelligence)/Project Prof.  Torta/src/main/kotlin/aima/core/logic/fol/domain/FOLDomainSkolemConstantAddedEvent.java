package aima.core.logic.fol.domain;

import aima.core.logic.fol.domain.FOLDomainEvent;
import aima.core.logic.fol.domain.FOLDomainListener;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLDomainSkolemConstantAddedEvent extends FOLDomainEvent {

	private static final long serialVersionUID = 1L;

	private String skolemConstantName;

	public FOLDomainSkolemConstantAddedEvent(Object source,
			String skolemConstantName) {
		super(source);

		this.skolemConstantName = skolemConstantName;
	}

	public String getSkolemConstantName() {
		return skolemConstantName;
	}

	@Override
	public void notifyListener(FOLDomainListener listener) {
		listener.skolemConstantAdded(this);
	}
}
