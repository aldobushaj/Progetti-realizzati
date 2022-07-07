package aima.core.util.math;

import aima.core.util.math.Matrix;

import java.util.List;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class Vector extends Matrix {
	private static final long serialVersionUID = 1L;

	// Vector is modelled as a matrix with a single column;
	/**
	 * Constructs a vector with the specified size.
	 * 
	 * @param size
	 *            the capacity of the vector
	 */
	public Vector(int size) {
		super(size, 1);
	}

	/**
	 * Constructs a vector with the specified list of values.
	 * 
	 * @param lst
	 *            a list of values
	 */
	public Vector(List<Double> lst) {
		super(lst.size(), 1);
		for (int i = 0; i < lst.size(); i++) {
			setValue(i, lst.get(i));
		}
	}

	/**
	 * Returns the value at the specified index.
	 * 
	 * @param i
	 *            the index of the value to return.
	 * 
	 * @return the value at the specified index.
	 */
	public double getValue(int i) {
		return super.get(i, 0);
	}

	/**
	 * Sets the value at the specified index.
	 * 
	 * @param index
	 *            the index of the value to set.
	 * @param value
	 *            the value to be placed at the index.
	 */
	public void setValue(int index, double value) {
		super.set(index, 0, value);
	}

	/**
	 * Returns a copy of this vector.
	 * 
	 * @return a copy of this vector.
	 */
	public aima.core.util.math.Vector copyVector() {
		aima.core.util.math.Vector result = new aima.core.util.math.Vector(getRowDimension());
		for (int i = 0; i < getRowDimension(); i++) {
			result.setValue(i, getValue(i));
		}
		return result;
	}

	/**
	 * Returns the number of values in this vector.
	 * 
	 * @return the number of values in this vector.
	 */
	public int size() {
		return getRowDimension();
	}

	/**
	 * Returns the result of vector subtraction.
	 * 
	 * @param v
	 *            the vector to subtract
	 * 
	 * @return the result of vector subtraction.
	 */
	public aima.core.util.math.Vector minus(aima.core.util.math.Vector v) {
		aima.core.util.math.Vector result = new aima.core.util.math.Vector(size());
		for (int i = 0; i < size(); i++) {
			result.setValue(i, getValue(i) - v.getValue(i));
		}
		return result;
	}

	/**
	 * Returns the result of vector addition.
	 * 
	 * @param v
	 *            the vector to add
	 * 
	 * @return the result of vector addition.
	 */
	public aima.core.util.math.Vector plus(aima.core.util.math.Vector v) {
		aima.core.util.math.Vector result = new aima.core.util.math.Vector(size());
		for (int i = 0; i < size(); i++) {
			result.setValue(i, getValue(i) + v.getValue(i));
		}
		return result;
	}

	/**
	 * Returns the index at which the maximum value in this vector is located.
	 * 
	 * @return the index at which the maximum value in this vector is located.
	 * 
	 * @throws RuntimeException
	 *             if the vector does not contain any values.
	 */
	public int indexHavingMaxValue() {
		if (size() <= 0) {
			throw new RuntimeException("can't perform this op on empty vector");
		}
		int res = 0;
		for (int i = 0; i < size(); i++) {
			if (getValue(i) > getValue(res)) {
				res = i;
			}
		}
		return res;
	}
}
