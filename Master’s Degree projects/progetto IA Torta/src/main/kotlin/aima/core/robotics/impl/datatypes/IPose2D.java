package aima.core.robotics.impl.datatypes;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.impl.datatypes.Angle;

/**
 * This interface describes functionality for a pose in a two-dimensional Cartesian plot.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> the pose implementing {@code IPose2D}.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}. 
 */
public interface IPose2D<P extends aima.core.robotics.impl.datatypes.IPose2D<P,M>,M extends IMclMove<M>> extends IMclPose<P,Angle,M> {
	
	/**
	 * @return the X coordinate of the pose.
	 */
	double getX();
	/**
	 * @return the Y coordinate of the pose.
	 */
	double getY();
	/**
	 * @return the heading of the pose in radians.
	 */
	double getHeading();
}
