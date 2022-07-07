package aima.core.learning.neural;

import aima.core.learning.neural.FeedForwardNeuralNetwork;
import aima.core.learning.neural.FunctionApproximator;
import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * 
 */
public interface NNTrainingScheme {
	Vector processInput(FeedForwardNeuralNetwork network, Vector input);

	void processError(FeedForwardNeuralNetwork network, Vector error);

	void setNeuralNetwork(FunctionApproximator ffnn);
}
