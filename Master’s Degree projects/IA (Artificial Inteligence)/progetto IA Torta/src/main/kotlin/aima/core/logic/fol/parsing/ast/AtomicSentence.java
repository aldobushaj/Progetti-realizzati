package aima.core.logic.fol.parsing.ast;

import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface AtomicSentence extends Sentence {
	List<Term> getArgs();

	aima.core.logic.fol.parsing.ast.AtomicSentence copy();
}
