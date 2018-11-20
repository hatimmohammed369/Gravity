package gravity;

public class GravityLaws{
	public static final double G = 6.67 * Math.pow(10, -11);
	public static double PLANET_MASS = 6 * Math.pow(10, 24);//initial value = earth mass
	public static double PLANET_RADIUS = 6400000;//initial value = earth radius

	private static void checkR(double r){
		if(r <= 0)
			throw new IllegalArgumentException("(R) can't be "+(r < 0 ? "negative" : "zero"));
	}

	public static double gravityLaw(double mass1, double mass2, double r){
		checkR(r);
		return (G * mass1 * mass2)/(r*r);
	}

	public static double gravityBetweenPlanetAndObject(double objectMass, double r){
		checkR(r);
		return (G * PLANET_MASS * objectMass)/(r*r);
	}

	public static double fieldStrength(double r){
		checkR(r);
		return (G * PLANET_MASS)/(r*r);
	}

	public static double potentialEnergy(double mass1, double mass2, double r){
		checkR(r);
		return (G * mass1 * mass2)/r;
	}

	public static double work(double r){
		checkR(r);
		return (G * PLANET_MASS)/r;
	}

	public static double rOfAcceleration(double acceleration){
		if(acceleration < 0)
			throw new IllegalArgumentException("acceleration can't be negative!");
		if(acceleration == 0)
			return 0;
		return Math.sqrt( (G * PLANET_MASS)/acceleration );
	}

	public static double planetMass(double acceleration, double planetRadius){
		return (acceleration * Math.pow(planetRadius, 2))/G;
	}

	public static double planetRadius(double planetMass, double acceleration){
		return Math.sqrt( (G * planetMass)/acceleration );
	}
}
