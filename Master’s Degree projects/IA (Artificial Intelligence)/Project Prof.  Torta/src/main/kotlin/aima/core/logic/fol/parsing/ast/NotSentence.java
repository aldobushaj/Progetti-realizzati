package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class NotSentence implements Sentence {
	private Sentence negated;
	private List<Sentence> args = new ArrayList<Sentence>();
	private String stringRep = null;
	private int hashCode = 0;

	public NotSentence(Sentence negated) {
		this.negated = negated;
		args.add(negated);
	}

	public Sentence getNegated() {
		return negated;
	}

	//
	// START-Sentence
	public String getSymbolicName() {
		return Connectors.NOT;
	}

	public boolean isCompound() {
		return true;
	}

	public List<Sentence> getArgs() {
		return Collections.unmodifiableList(args);
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitNotSentence(this, arg);
	}

	public aima.core.logic.fol.parsing.ast.NotSentence copy() {
		return new aima.core.logic.fol.parsing.ast.NotSentence(negated.copy());
	}

	// END-Sentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		aima.core.logic.fol.parsing.ast.NotSentence ns = (aima.core.logic.fol.parsing.ast.NotSentence) o;
		return (ns.negated.equals(negated));
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + negated.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("NOT(");
			sb.append(negated.toString());
			sb.append(")");
			stringRep = sb.toString();
		}
		return stringRep;
	}
}
