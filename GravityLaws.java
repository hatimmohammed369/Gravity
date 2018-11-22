/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityapplication;

/**
 *
 * @author Hassan Mohammed
 */
public class GravityLaws {
    public static final double G = 6.67 * Math.pow(10, -11);
    public static double PLANET_MASS = 6 * Math.pow(10, 24);
    public static double PLANET_RADIUS = GravityLaws.rOfAcceleration(9.8);
    public static double A = planetFieldStrength(PLANET_RADIUS);
    
    public static double gravityLaw(double mass1, double mass2, double r){
        return (mass1 * mass2 * G)/(r*r);
    }
    
    public static double planetGravity(double objectMass, double r){
        return gravityLaw(PLANET_MASS, objectMass, r);
    }
    
    public static double fieldStrength(double InitialObjectMass, double r){
        return (InitialObjectMass * G)/(r*r);
    }
    
    public static double planetFieldStrength(double r){
        return fieldStrength(PLANET_MASS, r);
    }
    
    public static double potentialEnergy(double objectMass, double r){
        return (PLANET_MASS * objectMass * G)/r;
    }
    
    public static double work(double r){
        return (PLANET_MASS * G)/r;
    }
    
    public static double rOfAcceleration(double accelerationOnSurface){
        if(accelerationOnSurface == 0)
            return 0;
        return Math.sqrt((PLANET_MASS * G)/accelerationOnSurface);
    }
    
    public static double planetMass(double accelerationOnSurface, double planetRadius){
        return ((accelerationOnSurface * Math.pow(planetRadius, 2))/G);
    }
    
    public static double planetRadius(double accelerationOnSurface, double planetMass){
        return Math.sqrt((planetMass * G)/accelerationOnSurface);
    }
}
