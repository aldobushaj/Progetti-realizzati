package aima.core.logic.fol.domain;

import aima.core.logic.fol.domain.FOLDomainEvent;
import aima.core.logic.fol.domain.FOLDomainListener;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLDomainAnswerLiteralAddedEvent extends FOLDomainEvent {

	private static final long serialVersionUID = 1L;

	private String answerLiteralName;

	public FOLDomainAnswerLiteralAddedEvent(Object source,
			String answerLiteralName) {
		super(source);

		this.answerLiteralName = answerLiteralName;
	}

	public String getAnswerLiteralNameName() {
		return answerLiteralName;
	}

	@Override
	public void notifyListener(FOLDomainListener listener) {
		listener.answerLiteralNameAdded(this);
	}
}
