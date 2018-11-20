package gravity;

public class CircularMotionLaws{
	public static double velocity(double circularPathRadius, double singleTurnTime){
		return (2 * Math.PI * circularPathRadius)/singleTurnTime;
	}

	public static double angularVelocity(double singleTurnTime){
		return (2 * Math.PI)/singleTurnTime;
	}

	public static double angularAcceleration(double velocity, double circularPathRadius){
		return Math.pow(velocity, 2)/circularPathRadius;
	}

	public static double centripetalForce(double mass, double velocity, double circularPathRadius){
		return (mass * Math.pow(velocity, 2))/circularPathRadius;
	}
}
