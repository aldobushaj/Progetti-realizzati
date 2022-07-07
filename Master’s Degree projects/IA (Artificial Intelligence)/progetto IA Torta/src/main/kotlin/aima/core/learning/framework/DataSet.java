package aima.core.learning.framework;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.Example;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class DataSet {
	protected DataSet() {

	}

	public List<Example> examples;

	public DataSetSpecification specification;

	public DataSet(DataSetSpecification spec) {
		examples = new LinkedList<Example>();
		this.specification = spec;
	}

	public void add(Example e) {
		examples.add(e);
	}

	public int size() {
		return examples.size();
	}

	public Example getExample(int number) {
		return examples.get(number);
	}

	public aima.core.learning.framework.DataSet removeExample(Example e) {
		aima.core.learning.framework.DataSet ds = new aima.core.learning.framework.DataSet(specification);
		for (Example eg : examples) {
			if (!(e.equals(eg))) {
				ds.add(eg);
			}
		}
		return ds;
	}

	public double getInformationFor() {
		String attributeName = specification.getTarget();
		Hashtable<String, Integer> counts = new Hashtable<String, Integer>();
		for (Example e : examples) {

			String val = e.getAttributeValueAsString(attributeName);
			if (counts.containsKey(val)) {
				counts.put(val, counts.get(val) + 1);
			} else {
				counts.put(val, 1);
			}
		}

		double[] data = new double[counts.keySet().size()];
		Iterator<Integer> iter = counts.values().iterator();
		for (int i = 0; i < data.length; i++) {
			data[i] = iter.next();
		}
		data = Util.normalize(data);

		return Util.information(data);
	}

	public Hashtable<String, aima.core.learning.framework.DataSet> splitByAttribute(String attributeName) {
		Hashtable<String, aima.core.learning.framework.DataSet> results = new Hashtable<String, aima.core.learning.framework.DataSet>();
		for (Example e : examples) {
			String val = e.getAttributeValueAsString(attributeName);
			if (results.containsKey(val)) {
				results.get(val).add(e);
			} else {
				aima.core.learning.framework.DataSet ds = new aima.core.learning.framework.DataSet(specification);
				ds.add(e);
				results.put(val, ds);
			}
		}
		return results;
	}

	public double calculateGainFor(String parameterName) {
		Hashtable<String, aima.core.learning.framework.DataSet> hash = splitByAttribute(parameterName);
		double totalSize = examples.size();
		double remainder = 0.0;
		for (String parameterValue : hash.keySet()) {
			double reducedDataSetSize = hash.get(parameterValue).examples
					.size();
			remainder += (reducedDataSetSize / totalSize)
					* hash.get(parameterValue).getInformationFor();
		}
		return getInformationFor() - remainder;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		aima.core.learning.framework.DataSet other = (aima.core.learning.framework.DataSet) o;
		return examples.equals(other.examples);
	}

	@Override
	public int hashCode() {
		return 0;
	}

	public Iterator<Example> iterator() {
		return examples.iterator();
	}

	public aima.core.learning.framework.DataSet copy() {
		aima.core.learning.framework.DataSet ds = new aima.core.learning.framework.DataSet(specification);
		for (Example e : examples) {
			ds.add(e);
		}
		return ds;
	}

	public List<String> getAttributeNames() {
		return specification.getAttributeNames();
	}

	public String getTargetAttributeName() {
		return specification.getTarget();
	}

	public aima.core.learning.framework.DataSet emptyDataSet() {
		return new aima.core.learning.framework.DataSet(specification);
	}

	/**
	 * @param specification
	 *            The specification to set. USE SPARINGLY for testing etc ..
	 *            makes no semantic sense
	 */
	public void setSpecification(DataSetSpecification specification) {
		this.specification = specification;
	}

	public List<String> getPossibleAttributeValues(String attributeName) {
		return specification.getPossibleAttributeValues(attributeName);
	}

	public aima.core.learning.framework.DataSet matchingDataSet(String attributeName, String attributeValue) {
		aima.core.learning.framework.DataSet ds = new aima.core.learning.framework.DataSet(specification);
		for (Example e : examples) {
			if (e.getAttributeValueAsString(attributeName).equals(
					attributeValue)) {
				ds.add(e);
			}
		}
		return ds;
	}

	public List<String> getNonTargetAttributes() {
		return Util.removeFrom(getAttributeNames(), getTargetAttributeName());
	}
}
