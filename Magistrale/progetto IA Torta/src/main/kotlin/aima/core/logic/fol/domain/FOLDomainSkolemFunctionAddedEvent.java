package aima.core.logic.fol.domain;

import aima.core.logic.fol.domain.FOLDomainEvent;
import aima.core.logic.fol.domain.FOLDomainListener;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLDomainSkolemFunctionAddedEvent extends FOLDomainEvent {

	private static final long serialVersionUID = 1L;

	private String skolemFunctionName;

	public FOLDomainSkolemFunctionAddedEvent(Object source,
			String skolemFunctionName) {
		super(source);

		this.skolemFunctionName = skolemFunctionName;
	}

	public String getSkolemConstantName() {
		return skolemFunctionName;
	}

	@Override
	public void notifyListener(FOLDomainListener listener) {
		listener.skolemFunctionAdded(this);
	}
}
