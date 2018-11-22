/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gravityapplication;

import java.util.regex.Pattern;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author Hassan Mohammed
 */
public class GravityApplication extends Application {
    static Rectangle RECT = new Rectangle(200, 40);
    
    static final Color BACKGROUNDS_COLOR = Color.rgb(55, 55, 55);
    
    static boolean isDecimalChar(char c) {
        return (Character.isDigit(c) || c == 'E' || c == 'e' || c == '.' || c == '+' || c == '-');
    }
    
    static boolean isDecimalLiteral(String s){
        for(char c : s.toCharArray())
            if(!isDecimalChar(c))
                return false;
        if((!s.contains("E") || !s.contains("e")) && !s.contains("+") && !s.contains("-"))
            return (Pattern.compile("\\d+(\\.\\d*)?")).matcher(s).find();
        return (Pattern.compile("\\d+\\.\\d+[eE][+-]?\\d+")).matcher(s).find();
    }
    
    @Override
    public void start(Stage primaryStage){
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((event) -> System.exit(0));
        
        Pane root = new Pane();
        Button gravityApplicationButton = new Button("Gravity");
        gravityApplicationButton.setOnAction((gravityApplicationActionEvent) -> {
            Stage gaStage = new Stage();
            
            Pane gaStageRoot = new Pane();
            Label planetMassLabel = new Label("Planet mass (Kilograms):");
            TextField planetMassField = new TextField(GravityLaws.PLANET_MASS+"");
            {
                ChangeListener<String> listener = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_MASS);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_MASS);
                    if(oldValue != newValue)
                        GravityLaws.PLANET_MASS = newValue;
                };
                planetMassField.textProperty().addListener(listener);
            }
            
            Label planetRadiusLabel = new Label("Planet radius (Meters):");
            TextField planetRadiusField = new TextField(GravityLaws.PLANET_RADIUS+"");
            {
                ChangeListener<String> listener = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                    if(oldValue != newValue)
                        GravityLaws.PLANET_RADIUS = newValue;
                };
                planetRadiusField.textProperty().addListener(listener);
            }
            
            Label objectAMassLabel = new Label("Object A mass (Kilograms):");
            TextField objectAMassField = new TextField("1.00");
            Label objectBMassLabel = new Label("Object B mass (Kilograms):");
            TextField objectBMassField = new TextField("1.00");
            
            Label distanceLabel = new Label("Distance between A & B centers (Meters):");
            TextField distanceField = new TextField("1.00");
            
            Label gravityForceLabel = new Label("Gravity force between A & B (Newtons):");
            TextField gravityForceField = new TextField(GravityLaws.gravityLaw(1, 1, 1)+"");
            gravityForceField.setEditable(false);
            
            Property<Boolean> objectA = new Property<>(false);
            Button objectAPropertiesButton = new Button("Object A Properties");
            objectAPropertiesButton.setOnAction((objectAPropertiesActionEvent) -> {
                Stage oApStage = new Stage();
                
                Pane oApStageRoot = new Pane();
                
                Label rDistanceLabel = new Label("Distance between {planet} & {object A} centers (Meters):");
                TextField rDistanceField = new TextField(GravityLaws.PLANET_RADIUS+"");
                
                Label planetGravityForceLabel = new Label("Gravity force between planet & object A (Newtons):");
                TextField planetGravityForceField = new TextField(GravityLaws.planetGravity(1, GravityLaws.PLANET_RADIUS)+"");
                planetGravityForceField.setEditable(false);
                
                Label potentialEnergyLabel = new Label("Object A potential energy (Joules):");
                TextField potentialEnergyField = new TextField(GravityLaws.potentialEnergy(1, GravityLaws.PLANET_RADIUS)+"");
                potentialEnergyField.setEditable(false);
                
                Label workLabel = new Label("Object A gravitional work (Joule/kilogram):");
                TextField workField = new TextField(GravityLaws.work(GravityLaws.PLANET_RADIUS)+"");
                workField.setEditable(false);
                
                Label fromRLabel = new Label("Distance from object A center (Meters):");
                TextField fromRField = new TextField("1.00");
                
                Label objectAFieldStrengthLabel = new Label("Object A field strength at"
                        + " {distance from object A center} (Newton/kilogram):");
                TextField objectAFieldStrengthField = new TextField(GravityLaws.fieldStrength(1, 1)+"");
                objectAFieldStrengthField.setEditable(false);
                
                {//listener rDistance
                    //should affect (planetGravityForce, potentialEnergy, work)
                    ChangeListener<String> listener = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue){
                            double mass = (isDecimalLiteral(objectAMassField.getText()) ? 
                                    Double.parseDouble(objectAMassField.getText()) : 1.00);
                            planetGravityForceField.setText(GravityLaws.planetGravity(mass, newValue)+"");
                            potentialEnergyField.setText(GravityLaws.planetGravity(mass, newValue)+"");
                            workField.setText(GravityLaws.work(newValue)+"");
                        }
                    };
                    rDistanceField.textProperty().addListener(listener);
                }
                
                {//listener fromR
                    //should affect (objectFieldStrength)
                    ChangeListener<String> listener = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue){
                            double mass = (isDecimalLiteral(objectAMassField.getText()) ? 
                                    Double.parseDouble(objectAMassField.getText()) : 1.00);
                            objectAFieldStrengthField.setText(GravityLaws.fieldStrength(mass, newValue)+"");
                        }
                    };
                    fromRField.textProperty().addListener(listener);
                }
                
                {//listener objectAMass
                    //should affect (ABgravity, AAndPlanetGravity, ObjectAPotentialEnergy, ObjectAFieldStrength)
                    ChangeListener<String> listener = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue){
                            double massB = (isDecimalLiteral(objectBMassField.getText()) ? 
                                    Double.parseDouble(objectBMassField.getText()) : 1.00);
                            double r = (isDecimalLiteral(distanceField.getText()) ? 
                                    Double.parseDouble(distanceField.getText()) : 1.00);
                            gravityForceField.setText(GravityLaws.gravityLaw(newValue, massB, r)+"");
                            
                            double rFromPlanetCenter = (isDecimalLiteral(rDistanceField.getText()) ? 
                                    Double.parseDouble(rDistanceField.getText()) : GravityLaws.PLANET_RADIUS);
                            planetGravityForceField.setText(GravityLaws.planetGravity(newValue, rFromPlanetCenter)+"");
                            potentialEnergyField.setText(GravityLaws.potentialEnergy(newValue, rFromPlanetCenter)+"");
                            
                            double fromRValue = (isDecimalLiteral(fromRField.getText()) ? 
                                    Double.parseDouble(fromRField.getText()) : 1.00);
                            objectAFieldStrengthField.setText(GravityLaws.fieldStrength(newValue, fromRValue)+"");
                        }
                    };
                    objectAMassField.textProperty().addListener(listener);
                }
                
                if(!objectA.get()){
                    objectA.set(true);
                    return;
                }
                
                oApStageRoot.getChildren().addAll(rDistanceLabel, rDistanceField, planetGravityForceLabel, planetGravityForceField,
                        potentialEnergyLabel, potentialEnergyField, workLabel, workField, fromRLabel, fromRField,
                        objectAFieldStrengthLabel, objectAFieldStrengthField);
                rDistanceLabel.relocate(20, 20);
                rDistanceField.relocate(20, 50);
                planetGravityForceLabel.relocate(20, 110);
                planetGravityForceField.relocate(20, 140);
                potentialEnergyLabel.relocate(20, 210);
                potentialEnergyField.relocate(20, 240);
                workLabel.relocate(20, 310);
                workField.relocate(20, 340);
                fromRLabel.relocate(20, 410);
                fromRField.relocate(20, 440);
                objectAFieldStrengthLabel.relocate(20, 510);
                objectAFieldStrengthField.relocate(20, 540);
                
                for(Object e : oApStageRoot.getChildren()){
                    if(e instanceof Label){
                        ((Label)e).setFont(Font.font(15));
                        ((Label)e).setStyle("-fx-text-fill: white;");
                    }else if(e instanceof TextField){
                        ((TextField)e).setShape(RECT);
                        ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                            + "-fx-text-fill: white");
                        ((TextField)e).setPrefSize(200, 40);
                        ((TextField)e).setFont(Font.font(15));
                    }
                }
                
                oApStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
                oApStage.setScene(new Scene(oApStageRoot, 540, 600));
                oApStage.show();
            });
            objectAPropertiesButton.fire();
            
            Button objectBPropertiesButton = new Button("Object B Properties");
            
            Property<Boolean> objectB = new Property<>(false);
            objectBPropertiesButton.setOnAction((objectBPropertiesActionEvent) -> {
                Stage oBpStage = new Stage();
                
                Pane oBpStageRoot = new Pane();
                
                Label rDistanceLabel = new Label("Distance between {planet} & {object B} centers (Meters):");
                TextField rDistanceField = new TextField(GravityLaws.PLANET_RADIUS+"");
                
                Label planetGravityForceLabel = new Label("Gravity force between planet & object B (Newtons):");
                TextField planetGravityForceField = new TextField(GravityLaws.planetGravity(1, GravityLaws.PLANET_RADIUS)+"");
                planetGravityForceField.setEditable(false);
                
                Label potentialEnergyLabel = new Label("Object B potential energy (Joules):");
                TextField potentialEnergyField = new TextField(GravityLaws.potentialEnergy(1, GravityLaws.PLANET_RADIUS)+"");
                potentialEnergyField.setEditable(false);
                
                Label workLabel = new Label("Object B gravitional work (Joule/kilogram):");
                TextField workField = new TextField(GravityLaws.work(GravityLaws.PLANET_RADIUS)+"");
                workField.setEditable(false);
                
                Label fromRLabel = new Label("Distance from object B center (Meters):");
                TextField fromRField = new TextField("1.00");
                
                Label objectBFieldStrengthLabel = new Label("Object B field strength at"
                        + " {distance from object B center} (Newton/kilogram):");
                TextField objectBFieldStrengthField = new TextField(GravityLaws.fieldStrength(1, 1)+"");
                objectBFieldStrengthField.setEditable(false);
                
                {//listener rDistance
                    //should affect (planetGravityForce, potentialEnergy, work)
                    ChangeListener<String> listener = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue){
                            double mass = (isDecimalLiteral(objectBMassField.getText()) ? 
                                    Double.parseDouble(objectBMassField.getText()) : 1.00);
                            planetGravityForceField.setText(GravityLaws.planetGravity(mass, newValue)+"");
                            potentialEnergyField.setText(GravityLaws.planetGravity(mass, newValue)+"");
                            workField.setText(GravityLaws.work(newValue)+"");
                        }
                    };
                    rDistanceField.textProperty().addListener(listener);
                }
                
                {//listener fromR
                    //should affect (objectFieldStrength)
                    ChangeListener<String> listener = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue){
                            double mass = (isDecimalLiteral(objectBMassField.getText()) ? 
                                    Double.parseDouble(objectBMassField.getText()) : 1.00);
                            objectBFieldStrengthField.setText(GravityLaws.fieldStrength(mass, newValue)+"");
                        }
                    };
                    fromRField.textProperty().addListener(listener);
                }
                
                {//listener objectAMass
                    //should affect (ABgravity, AAndPlanetGravity, ObjectAPotentialEnergy, ObjectAFieldStrength)
                    ChangeListener<String> listener = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue){
                            double massA = (isDecimalLiteral(objectAMassField.getText()) ? 
                                    Double.parseDouble(objectAMassField.getText()) : 1.00);
                            double r = (isDecimalLiteral(distanceField.getText()) ? 
                                    Double.parseDouble(distanceField.getText()) : 1.00);
                            gravityForceField.setText(GravityLaws.gravityLaw(newValue, massA, r)+"");
                            
                            double rFromPlanetCenter = (isDecimalLiteral(rDistanceField.getText()) ? 
                                    Double.parseDouble(rDistanceField.getText()) : GravityLaws.PLANET_RADIUS);
                            planetGravityForceField.setText(GravityLaws.planetGravity(newValue, rFromPlanetCenter)+"");
                            potentialEnergyField.setText(GravityLaws.potentialEnergy(newValue, rFromPlanetCenter)+"");
                            
                            double fromRValue = (isDecimalLiteral(fromRField.getText()) ? 
                                    Double.parseDouble(fromRField.getText()) : 1.00);
                            objectBFieldStrengthField.setText(GravityLaws.fieldStrength(newValue, fromRValue)+"");
                        }
                    };
                    objectBMassField.textProperty().addListener(listener);
                }
                
                if(!objectB.get()){
                    objectB.set(true);
                    return;
                }
                
                oBpStageRoot.getChildren().addAll(rDistanceLabel, rDistanceField, planetGravityForceLabel, planetGravityForceField,
                        potentialEnergyLabel, potentialEnergyField, workLabel, workField, fromRLabel, fromRField,
                        objectBFieldStrengthLabel, objectBFieldStrengthField);
                rDistanceLabel.relocate(20, 20);
                rDistanceField.relocate(20, 50);
                planetGravityForceLabel.relocate(20, 110);
                planetGravityForceField.relocate(20, 140);
                potentialEnergyLabel.relocate(20, 210);
                potentialEnergyField.relocate(20, 240);
                workLabel.relocate(20, 310);
                workField.relocate(20, 340);
                fromRLabel.relocate(20, 410);
                fromRField.relocate(20, 440);
                objectBFieldStrengthLabel.relocate(20, 510);
                objectBFieldStrengthField.relocate(20, 540);
                
                for(Object e : oBpStageRoot.getChildren()){
                    if(e instanceof Label){
                        ((Label)e).setFont(Font.font(15));
                        ((Label)e).setStyle("-fx-text-fill: white;");
                    }else if(e instanceof TextField){
                        ((TextField)e).setShape(RECT);
                        ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                            + "-fx-text-fill: white");
                        ((TextField)e).setPrefSize(200, 40);
                        ((TextField)e).setFont(Font.font(15));
                    }
                }
                
                oBpStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
                oBpStage.setScene(new Scene(oBpStageRoot, 540, 600));
                oBpStage.show();
            });
            objectBPropertiesButton.fire();
            
            {//listener distance
                //should affect (gravityForce)
                ChangeListener<String> listener = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : 1.00);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : 1.00);
                    if(oldValue != newValue){
                        double massA = (isDecimalLiteral(objectAMassField.getText()) ?
                                Double.parseDouble(objectAMassField.getText()) : 1.00);
                        double massB = (isDecimalLiteral(objectBMassField.getText()) ?
                                Double.parseDouble(objectBMassField.getText()) : 1.00);
                        gravityForceField.setText(GravityLaws.gravityLaw(massA, massB, newValue)+"");
                    }
                };
                distanceField.textProperty().addListener(listener);
            }
            gaStageRoot.getChildren().addAll(planetMassLabel, planetMassField, planetRadiusLabel, planetRadiusField,
                    objectAMassLabel, objectAMassField, objectBMassLabel, objectBMassField, gravityForceLabel, gravityForceField,
                    distanceLabel, distanceField, objectAPropertiesButton, objectBPropertiesButton);
            planetMassLabel.relocate(20, 20);
            planetMassField.relocate(20, 50);
            planetRadiusLabel.relocate(240, 20);
            planetRadiusField.relocate(240, 50);
            objectAMassLabel.relocate(20, 110);
            objectAMassField.relocate(20, 140);
            objectBMassLabel.relocate(240, 110);
            objectBMassField.relocate(240, 140);
            distanceLabel.relocate(80, 210);
            distanceField.relocate(115, 240);
            gravityForceLabel.relocate(80, 310);
            gravityForceField.relocate(115, 340);
            objectAPropertiesButton.relocate(20, 410);
            objectBPropertiesButton.relocate(240, 410);
            
            for(Object e : gaStageRoot.getChildren()){
                if(e instanceof Label){
                    ((Label)e).setFont(Font.font(15));
                    ((Label)e).setStyle("-fx-text-fill: white;");
                }else if(e instanceof TextField){
                    ((TextField)e).setShape(RECT);
                    ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                            + "-fx-text-fill: white");
                    ((TextField)e).setPrefSize(200, 40);
                    ((TextField)e).setFont(Font.font(15));
                }else if(e instanceof Button){
                    ((Button)e).setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: rgb(100, 100, 100);");
                    ((Button)e).setOnMousePressed((event) -> {
                        ((Button)e).setStyle("-fx-text-fill: black;"
                        + "-fx-background-color: rgb(200, 200, 200)");
                    });
                    ((Button)e).setOnMouseReleased((event) -> {
                        ((Button)e).setStyle("-fx-text-fill: white;"
                        + "-fx-background-color: rgb(100, 100, 100)");
                    });
                    ((Button)e).setShape(RECT);
                    ((Button)e).setPrefSize(200, 40);
                    ((Button)e).setFont(Font.font(15));
                }
            }
            
            gaStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
            gaStage.setScene(new Scene(gaStageRoot, 460, 470));
            gaStage.show();
        });
        Button circularMotionApplicationButton = new Button("Circular Motion");
        circularMotionApplicationButton.setOnAction((circularMotionApplicationActionEvent) -> {
            Stage cmaStage = new Stage();
            
            Pane cmaStageRoot = new Pane();
            Label circularPathRadiusLabel = new Label("Circular path radius (Meters):");
            TextField circularPathRadiusField = new TextField("1.00");
            
            Label massLabel = new Label("Object Mass (Kilograms):");
            TextField massField = new TextField("1.00");
            
            Label velocityLabel = new Label("Velocity (Meter/second):");
            TextField velocityField = new TextField("1.00");
            
            Label singleCycleTimeLabel = new Label("Single cycle time (seconds):");
            TextField singleCycleTimeField = new TextField("1.00");
            
            Label frequencyLabel = new Label("Frequency (hertz):");
            TextField frequencyField = new TextField("1.00");
            frequencyField.setEditable(false);
            
            Label angularVelocityLabel = new Label("Angular velocity (radian/second):");
            TextField angularVelocityField = new TextField(CircularMotionLaws.angularVelocity(1)+"");
            angularVelocityField.setEditable(false);
            
            Label accelerationLabel = new Label("Angular acceleration (Meter/second\u00b2):");
            TextField accelerationField = new TextField(CircularMotionLaws.acceleration(1, 1)+"");
            accelerationField.setEditable(false);
            
            Label centripetalForceLabel = new Label("Centripetal force (newtons):");
            TextField centripetalForceField = new TextField(CircularMotionLaws.centripetalForce(1, 1, 1)+"");
            centripetalForceField.setEditable(false);
            
            {//text changed event for circular path radius
                //should affect velocity, acceleration, centripetalForce
                ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : 1.00);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : 1.00);
                    if(oldValue != newValue){
                        double singleCycleTime = (isDecimalLiteral(singleCycleTimeField.getText()) ?
                            Double.parseDouble(singleCycleTimeField.getText()) : 1.00);
                        velocityField.setText(CircularMotionLaws.velocity(newValue, singleCycleTime)+"");
                        
                        double velocity = (isDecimalLiteral(velocityField.getText()) ? 
                                Double.parseDouble(velocityField.getText()) : 1.00);
                        accelerationField.setText(CircularMotionLaws.acceleration(velocity, newValue)+"");
                        
                        double mass = (isDecimalLiteral(massField.getText()) ? Double.parseDouble(massField.getText()) : 1.00);
                        centripetalForceField.setText(CircularMotionLaws.centripetalForce(mass, velocity, newValue)+"");
                    }
                };
                circularPathRadiusField.textProperty().addListener(textChangedEvent);
            }
            
            {//text changed event for mass 
                //should affect centripetal force
                ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : 1.00);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : 1.00);
                    if(oldValue != newValue){
                        double velocity = (isDecimalLiteral(velocityField.getText()) ?
                                Double.parseDouble(velocityField.getText()) : 1.00);
                        double circularPathRadius = (isDecimalLiteral(circularPathRadiusField.getText()) ?
                                Double.parseDouble(circularPathRadiusField.getText()) : 1.00);
                        centripetalForceField.setText(CircularMotionLaws.centripetalForce(newValue, velocity, circularPathRadius)+"");
                    }
                };
                massField.textProperty().addListener(textChangedEvent);
            }
            
            {//text changed event for velocity
                //should affect (acceleration, centripetalForce)
                ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : 1.00);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : 1.00);
                    if(oldValue != newValue){
                        double circularPathRadius = (isDecimalLiteral(circularPathRadiusField.getText()) ?
                                Double.parseDouble(circularPathRadiusField.getText()) : 1.00);
                        accelerationField.setText(CircularMotionLaws.acceleration(newValue, circularPathRadius)+"");
                        
                        double mass = (isDecimalLiteral(massField.getText()) ?
                                Double.parseDouble(massField.getText()) : 1.00);
                        centripetalForceField.setText(CircularMotionLaws.centripetalForce(mass, newValue, circularPathRadius)+"");
                    }
                };
                velocityField.textProperty().addListener(textChangedEvent);
            }
            
            {//text changed event for (singleCycleTime)
                //should affect (velocities, frequency)
                ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                    double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : 1.00);
                    double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : 1.00);
                    if(oldValue != newValue){
                        double circularPathRadius = (isDecimalLiteral(circularPathRadiusField.getText()) ?
                                Double.parseDouble(circularPathRadiusField.getText()) : 1.00);
                        velocityField.setText(CircularMotionLaws.velocity(circularPathRadius, newValue)+"");
                        
                        angularVelocityField.setText(CircularMotionLaws.angularVelocity(newValue)+"");
                        
                        frequencyField.setText((1/newValue)+"");
                    }
                };
                singleCycleTimeField.textProperty().addListener(textChangedEvent);
            }
            
            cmaStageRoot.getChildren().addAll(circularPathRadiusLabel, circularPathRadiusField, massLabel, massField,
                    velocityLabel, velocityField, singleCycleTimeLabel, singleCycleTimeField, frequencyLabel, frequencyField,
                    angularVelocityLabel, angularVelocityField, accelerationLabel, accelerationField,
                    centripetalForceLabel, centripetalForceField);
            
            circularPathRadiusLabel.relocate(20, 20);
            circularPathRadiusField.relocate(20, 50);
            massLabel.relocate(300, 20);
            massField.relocate(300, 50);
            velocityLabel.relocate(20, 110);
            velocityField.relocate(20, 140);
            singleCycleTimeLabel.relocate(300, 110);
            singleCycleTimeField.relocate(300, 140);
            frequencyLabel.relocate(20, 210);
            frequencyField.relocate(20, 240);
            angularVelocityLabel.relocate(300, 210);
            angularVelocityField.relocate(300, 240);
            accelerationLabel.relocate(20, 310);
            accelerationField.relocate(20, 340);
            centripetalForceLabel.relocate(300, 310);
            centripetalForceField.relocate(300, 340);
            
            for(Object e : cmaStageRoot.getChildren()){
                if(e instanceof Label){
                    ((Label)e).setFont(Font.font(15));
                    ((Label)e).setStyle("-fx-text-fill: white;");
                }else if(e instanceof TextField){
                    ((TextField)e).setShape(RECT);
                    ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                            + "-fx-text-fill: white");
                    ((TextField)e).setPrefSize(200, 40);
                    ((TextField)e).setFont(Font.font(15));
                }
            }
            
            cmaStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
            cmaStage.setScene(new Scene(cmaStageRoot, 520, 400));
            cmaStage.show();
        });
        Button planetsApplicationButton = new Button("Planets");
        planetsApplicationButton.setOnAction((planetApplicationActionEvent) -> {
            Stage paStage = new Stage();
            
            Pane paRoot = new Pane();
            Button planetMassCalculatorButton = new Button("Planet Mass Calculator");
            planetMassCalculatorButton.setOnAction((planetMassCalculatorActionEvent) -> {
                Stage pmcStage = new Stage();
                Pane pmcStageRoot = new Pane();
                
                Label planetRadiusLabel = new Label("Planet radius (Meters):");
                TextField planetRadiusField = new TextField(GravityLaws.PLANET_RADIUS+"");
                Label planetAccelerationLabel = new Label("Planet acceleration on surface (Meter/second\u00b2):");
                TextField planetAccelerationField = new TextField(GravityLaws.A+"");
                Label planetMassLabel = new Label("Planet mass (Kilograms):");
                TextField planetMassField = new TextField(GravityLaws.PLANET_MASS+"");
                planetMassField.setEditable(false);
                
                {//change listener planet radius
                    ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_RADIUS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_RADIUS);
                        double acceleration = (isDecimalLiteral(planetAccelerationField.getText()) ? 
                                Double.parseDouble(planetAccelerationField.getText()) : GravityLaws.A);
                        if(oldValue != newValue)
                            planetMassField.setText(GravityLaws.planetMass(acceleration, newValue)+"");
                    };
                    planetRadiusField.textProperty().addListener(textChangedEvent);
                }
                
                {//change listener planet acceleration
                    ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.A);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.A);
                        double radius = (isDecimalLiteral(planetRadiusField.getText()) ? 
                                Double.parseDouble(planetRadiusField.getText()) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue)
                            planetMassField.setText(GravityLaws.planetMass(newValue, radius)+"");
                    };
                    planetAccelerationField.textProperty().addListener(textChangedEvent);
                }
                
                pmcStageRoot.getChildren().addAll(planetRadiusLabel, planetRadiusField, planetAccelerationLabel, 
                        planetAccelerationField, planetMassLabel, planetMassField);
                planetRadiusLabel.relocate(75, 20);
                planetRadiusField.relocate(75, 50);
                planetAccelerationLabel.relocate(20, 110);
                planetAccelerationField.relocate(75, 140);
                planetMassLabel.relocate(75, 210);
                planetMassField.relocate(75, 240);
                
                for(Object e : pmcStageRoot.getChildren()){
                    if(e instanceof Label){
                        ((Label)e).setFont(Font.font(15));
                        ((Label)e).setStyle("-fx-text-fill: white;");
                    }else if(e instanceof TextField){
                        ((TextField)e).setShape(RECT);
                        ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                                + "-fx-text-fill: white");
                        ((TextField)e).setPrefSize(200, 40);
                        ((TextField)e).setFont(Font.font(15));
                    }
                }
                
                pmcStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
                pmcStage.setScene(new Scene(pmcStageRoot, 350, 300));
                pmcStage.show();
            });
            
            Button planetRadiusCalculatorButton = new Button("Planet Radius Calculator");
            planetRadiusCalculatorButton.setOnAction((planetMassCalculatorActionEvent) -> {
                Stage prcStage = new Stage();
                Pane prcStageRoot = new Pane();
                
                Label planetMassLabel = new Label("Planet mass (Kilograms):");
                TextField planetMassField = new TextField(GravityLaws.PLANET_MASS+"");
                Label planetAccelerationLabel = new Label("Planet acceleration on surface (Meter/second\u00b2):");
                TextField planetAccelerationField = new TextField(GravityLaws.A+"");
                Label planetRadiusLabel = new Label("Planet radius (Meters):");
                TextField planetRadiusField = new TextField(GravityLaws.PLANET_RADIUS+"");
                planetRadiusField.setEditable(false);
                
                {//change listener planet mass
                    ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_MASS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_MASS);
                        double acceleration = (isDecimalLiteral(planetAccelerationField.getText()) ? 
                                Double.parseDouble(planetAccelerationField.getText()) : GravityLaws.A);
                        if(oldValue != newValue)
                            planetRadiusField.setText(GravityLaws.planetRadius(acceleration, newValue)+"");
                    };
                    planetMassField.textProperty().addListener(textChangedEvent);
                }
                
                {//change listener planet acceleration
                    ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.A);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.A);
                        double mass = (isDecimalLiteral(planetMassField.getText()) ? 
                                Double.parseDouble(planetMassField.getText()) : GravityLaws.PLANET_MASS);
                        if(oldValue != newValue)
                            planetRadiusField.setText(GravityLaws.planetRadius(newValue, mass)+"");
                    };
                    planetAccelerationField.textProperty().addListener(textChangedEvent);
                }
                
                prcStageRoot.getChildren().addAll(planetMassLabel, planetMassField, planetAccelerationLabel, 
                        planetAccelerationField, planetRadiusLabel, planetRadiusField);
                
                planetMassLabel.relocate(75, 20);
                planetMassField.relocate(75, 50);
                planetAccelerationLabel.relocate(20, 110);
                planetAccelerationField.relocate(75, 140);
                planetRadiusLabel.relocate(75, 210);
                planetRadiusField.relocate(75, 240);
                
                for(Object e : prcStageRoot.getChildren()){
                    if(e instanceof Label){
                        ((Label)e).setFont(Font.font(15));
                        ((Label)e).setStyle("-fx-text-fill: white;");
                    }else if(e instanceof TextField){
                        ((TextField)e).setShape(RECT);
                        ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                                + "-fx-text-fill: white");
                        ((TextField)e).setPrefSize(200, 40);
                        ((TextField)e).setFont(Font.font(15));
                    }
                }
                
                prcStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
                prcStage.setScene(new Scene(prcStageRoot, 350, 300));
                prcStage.show();
            });
            
            Button planetAccelerationCalculatorButton = new Button("Planet Acceleration Calculator");
            planetAccelerationCalculatorButton.setOnAction((planetMassCalculatorActionEvent) -> {
                Stage pacStage = new Stage();
                Pane pacStageRoot = new Pane();
                
                Label planetMassLabel = new Label("Planet mass (Kilograms):");
                TextField planetMassField = new TextField(GravityLaws.PLANET_MASS+"");
                Label planetRadiusLabel = new Label("Height (Meters):");
                TextField planetRadiusField = new TextField(GravityLaws.PLANET_RADIUS+"");
                Label planetAccelerationLabel = new Label("Planet acceleration on surface (Meter/second\u00b2):");
                TextField planetAccelerationField = new TextField(GravityLaws.A+"");
                planetAccelerationField.setEditable(false);
                
                {//change listener planet mass
                    ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.PLANET_MASS);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.PLANET_MASS);
                        double radius = (isDecimalLiteral(planetRadiusField.getText()) ? 
                                Double.parseDouble(planetRadiusField.getText()) : GravityLaws.PLANET_RADIUS);
                        if(oldValue != newValue)
                            planetAccelerationField.setText(GravityLaws.fieldStrength(newValue, radius)+"");
                    };
                    planetMassField.textProperty().addListener(textChangedEvent);
                }
                
                {//change listener planet radius
                    ChangeListener<String> textChangedEvent = (observable, oldText, newText) -> {
                        double oldValue = (isDecimalLiteral(oldText) ? Double.parseDouble(oldText) : GravityLaws.A);
                        double newValue = (isDecimalLiteral(newText) ? Double.parseDouble(newText) : GravityLaws.A);
                        double mass = (isDecimalLiteral(planetMassField.getText()) ? 
                                Double.parseDouble(planetMassField.getText()) : GravityLaws.PLANET_MASS);
                        if(oldValue != newValue)
                            planetAccelerationField.setText(GravityLaws.fieldStrength(mass, newValue)+"");
                    };
                    planetRadiusField.textProperty().addListener(textChangedEvent);
                }
                
                pacStageRoot.getChildren().addAll(planetMassLabel, planetMassField, planetRadiusLabel, 
                        planetRadiusField, planetAccelerationLabel, planetAccelerationField);
                
                planetMassLabel.relocate(75, 20);
                planetMassField.relocate(75, 50);
                planetRadiusLabel.relocate(75, 110);
                planetRadiusField.relocate(75, 140);
                planetAccelerationLabel.relocate(20, 210);
                planetAccelerationField.relocate(75, 240);
                
                for(Object e : pacStageRoot.getChildren()){
                    if(e instanceof Label){
                        ((Label)e).setFont(Font.font(15));
                        ((Label)e).setStyle("-fx-text-fill: white;");
                    }else if(e instanceof TextField){
                        ((TextField)e).setShape(RECT);
                        ((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"
                                + "-fx-text-fill: white");
                        ((TextField)e).setPrefSize(200, 40);
                        ((TextField)e).setFont(Font.font(15));
                    }
                }
                
                pacStageRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
                pacStage.setScene(new Scene(pacStageRoot, 350, 300));
                pacStage.show();
            });
            
            paRoot.getChildren().addAll(planetMassCalculatorButton, planetRadiusCalculatorButton, planetAccelerationCalculatorButton);
            planetMassCalculatorButton.relocate(50, 20);
            planetRadiusCalculatorButton.relocate(50, 80);
            planetAccelerationCalculatorButton.relocate(50, 140);
            
            for(Object e : paRoot.getChildren()){
                ((Button)e).setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: rgb(100, 100, 100);");
                ((Button)e).setOnMousePressed((event) -> {
                    ((Button)e).setStyle("-fx-text-fill: black;"
                        + "-fx-background-color: rgb(200, 200, 200)");
                });
                ((Button)e).setOnMouseReleased((event) -> {
                    ((Button)e).setStyle("-fx-text-fill: white;"
                        + "-fx-background-color: rgb(100, 100, 100)");
                });
                ((Button)e).setShape(new Rectangle(250, 40));
                ((Button)e).setPrefSize(250, 40);
                ((Button)e).setFont(Font.font(15));
            }
            
            paRoot.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
            
            paStage.setScene(new Scene(paRoot, 350, 200));
            paStage.show();
        });
        
        root.getChildren().addAll(gravityApplicationButton, circularMotionApplicationButton, planetsApplicationButton);
        gravityApplicationButton.relocate(20, 20);
        circularMotionApplicationButton.relocate(240, 20);
        planetsApplicationButton.relocate(460, 20);
        
        for(Object e : root.getChildren()){
            ((Button)e).setStyle("-fx-text-fill: white;"
                    + "-fx-background-color: rgb(100, 100, 100);");
            ((Button)e).setOnMousePressed((event) -> {
                ((Button)e).setStyle("-fx-text-fill: black;"
                        + "-fx-background-color: rgb(200, 200, 200)");
            });
            ((Button)e).setOnMouseReleased((event) -> {
                ((Button)e).setStyle("-fx-text-fill: white;"
                        + "-fx-background-color: rgb(100, 100, 100)");
            });
            ((Button)e).setShape(RECT);
            ((Button)e).setPrefSize(200, 40);
            ((Button)e).setFont(Font.font(15));
        }
        
        root.setBackground(new Background(new BackgroundFill(BACKGROUNDS_COLOR, null, null)));
        primaryStage.setScene(new Scene(root, 670, 70));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
