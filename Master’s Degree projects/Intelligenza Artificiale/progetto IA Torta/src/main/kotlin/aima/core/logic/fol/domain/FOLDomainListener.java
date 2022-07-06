package aima.core.logic.fol.domain;

import aima.core.logic.fol.domain.FOLDomainAnswerLiteralAddedEvent;
import aima.core.logic.fol.domain.FOLDomainSkolemConstantAddedEvent;
import aima.core.logic.fol.domain.FOLDomainSkolemFunctionAddedEvent;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface FOLDomainListener {
	void skolemConstantAdded(FOLDomainSkolemConstantAddedEvent event);

	void skolemFunctionAdded(FOLDomainSkolemFunctionAddedEvent event);

	void answerLiteralNameAdded(FOLDomainAnswerLiteralAddedEvent event);
}
