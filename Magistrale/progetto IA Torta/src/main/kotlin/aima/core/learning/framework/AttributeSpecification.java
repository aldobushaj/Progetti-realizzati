package aima.core.learning.framework;

import aima.core.learning.framework.Attribute;

/**
 * @author Ravi Mohan
 * 
 */
public interface AttributeSpecification {

	boolean isValid(String string);

	String getAttributeName();

	Attribute createAttribute(String rawValue);
}
