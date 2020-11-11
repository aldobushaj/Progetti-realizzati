package aima.core.logic.fol.parsing.ast;

import aima.core.logic.fol.parsing.ast.FOLNode;

import java.util.List;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface Term extends FOLNode {
	List<aima.core.logic.fol.parsing.ast.Term> getArgs();

	aima.core.logic.fol.parsing.ast.Term copy();
}
