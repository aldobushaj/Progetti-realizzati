package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.Term;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Function implements Term {
	private String functionName;
	private List<Term> terms = new ArrayList<Term>();
	private String stringRep = null;
	private int hashCode = 0;

	public Function(String functionName, List<Term> terms) {
		this.functionName = functionName;
		this.terms.addAll(terms);
	}

	public String getFunctionName() {
		return functionName;
	}

	public List<Term> getTerms() {
		return Collections.unmodifiableList(terms);
	}

	//
	// START-Term
	public String getSymbolicName() {
		return getFunctionName();
	}

	public boolean isCompound() {
		return true;
	}

	public List<Term> getArgs() {
		return getTerms();
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitFunction(this, arg);
	}

	public aima.core.logic.fol.parsing.ast.Function copy() {
		List<Term> copyTerms = new ArrayList<Term>();
		for (Term t : terms) {
			copyTerms.add(t.copy());
		}
		return new aima.core.logic.fol.parsing.ast.Function(functionName, copyTerms);
	}

	// END-Term
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof aima.core.logic.fol.parsing.ast.Function)) {
			return false;
		}

		aima.core.logic.fol.parsing.ast.Function f = (aima.core.logic.fol.parsing.ast.Function) o;

		return f.getFunctionName().equals(getFunctionName())
				&& f.getTerms().equals(getTerms());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + functionName.hashCode();
			for (Term t : terms) {
				hashCode = 37 * hashCode + t.hashCode();
			}
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(functionName);
			sb.append("(");

			boolean first = true;
			for (Term t : terms) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append(t.toString());
			}

			sb.append(")");

			stringRep = sb.toString();
		}
		return stringRep;
	}
}