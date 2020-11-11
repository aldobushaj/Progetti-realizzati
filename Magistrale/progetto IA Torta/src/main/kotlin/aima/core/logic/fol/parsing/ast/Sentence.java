package aima.core.logic.fol.parsing.ast;

import aima.core.logic.fol.parsing.ast.FOLNode;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface Sentence extends FOLNode {
	aima.core.logic.fol.parsing.ast.Sentence copy();
}
