package aima.core.probability.domain;

import java.io.Serializable;

public abstract class AbstractDiscreteDomain implements DiscreteDomain, Serializable {

	//
	// START-Domain
	@Override
	public boolean isFinite() {
		return false;
	}

	@Override
	public boolean isInfinite() {
		return true;
	}

	@Override
	public int size() {
		throw new IllegalStateException(
				"You cannot determine the size of an infinite domain");
	}

	@Override
	public abstract boolean isOrdered();
	// END-Domain
	//
}