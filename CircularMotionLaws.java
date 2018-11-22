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
public class CircularMotionLaws {
    public static double velocity(double circularPathRadius, double singleCycleTime){
        return (2 * Math.PI * circularPathRadius) / singleCycleTime;
    }
    
    public static double angularVelocity(double singleCycleTime){
        return (2 * Math.PI)/singleCycleTime;
    }
    
    public static double acceleration(double velocity, double circularPathRadius){
        return Math.pow(velocity, 2)/circularPathRadius;
    }
    
    public static double centripetalForce(double mass, double acceleration){
        return (mass * acceleration);
    }
    
    public static double centripetalForce(double mass, double velocity, double circularPathRadius){
        return (mass * acceleration(velocity, circularPathRadius));
    }
}
