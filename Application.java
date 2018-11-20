package gravity;

import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Application extends javafx.application.Application{
	static final Rectangle FIELD_RECT = new Rectangle(200, 40);

	static final String DECIMAL_LITERAL_PATTERN = "\\d+((\\.)?(\\d+)[eE]?[+-]?)?\\d+";

	private static boolean isDecimalStringDigit(char c){
		return ((c >= '0' && c <= '9') || (c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-'));
	}
	private static boolean isDecimalLiteral1(String s){
		for(char c : s.toCharArray())
			if(!isDecimalStringDigit(c))
				return false;
		return true;
	}

	static boolean isDecimalLiteral(String s){
		if(!isDecimalLiteral1(s))
			return false;
		return Pattern.compile(DECIMAL_LITERAL_PATTERN).matcher(s).find();
	}

	@Override
	public void start(Stage primaryStage){
		primaryStage.setResizable(false);
		Pane root = new Pane();
		primaryStage.setOnCloseRequest((event) -> System.exit(0));

		Button gravityApplicationButton = new Button("Gravity");
		Button circularMotionApplicationButton = new Button("Circular Motion");
		Button planetsApplicationButton = new Button("Planets");

		//-----------------------------------------------------------------------------------------------------------
		gravityApplicationButton.setOnAction((topLevelActionEvent) -> {
			gravityApplicationButton.setDisable(true);
			Stage gravityAppStage = new Stage();
			gravityAppStage.setResizable(false);
			gravityAppStage.setOnCloseRequest((event) -> {
				gravityApplicationButton.setDisable(false);
			});

			Pane gravityAppRoot = new Pane();

			final Property<Double> planetMass = new Property<Double>(GravityLaws.PLANET_MASS){
				@Override
				public void set(Double value){
					this.value = value;
					GravityLaws.PLANET_MASS = value;
				}
			};

			final Property<Double> planetRadius = new Property<Double>(GravityLaws.PLANET_RADIUS){
				@Override
				public void set(Double value){
					this.value = value;
					GravityLaws.PLANET_RADIUS = value;
				}
			};

			Label planetMassLabel = new Label("Planet mass (kilograms):");
			TextField planetMassField = new TextField( planetMass.toString() );
			Label planetMassErrorLabel = new Label("Invalid value");
			planetMassErrorLabel.setStyle("-fx-text-fill: red;");
			planetMassErrorLabel.setFont(Font.font(13));
			planetMassErrorLabel.setVisible(false);

			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					if(!isDecimalLiteral(currentText)){
						planetMassErrorLabel.setVisible(true);
						return;
					}else{
						planetMassErrorLabel.setVisible(false);
						double last = Double.parseDouble(lastText);
						double current = Double.parseDouble(currentText);
						if(last != current)
							planetMass.set(current);
					}
				};
				planetMassField.textProperty().addListener(textChangedEvent);
			}

			Label planetRadiusLabel = new Label("Planet radius (meters):");
			TextField planetRadiusField = new TextField( planetRadius.toString() );
			Label planetRadiusErrorLabel = new Label("Invalid value");
			planetRadiusErrorLabel.setStyle("-fx-text-fill: red;");
			planetRadiusErrorLabel.setFont(Font.font(13));
			planetRadiusErrorLabel.setVisible(false);
			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					if(!isDecimalLiteral(currentText)){
						planetRadiusErrorLabel.setVisible(true);
						return;
					}else{
						planetRadiusErrorLabel.setVisible(false);
						double last = Double.parseDouble(lastText);
						double current = Double.parseDouble(currentText);
						if(last != current)
							planetRadius.set(current);
					}
				};
				planetRadiusField.textProperty().addListener(textChangedEvent);
			}

			Button planetGravitySubAppButton = new Button("Planet gravity");
			Button objectsGravitySubAppButton = new Button("Objects gravity");

			//-------------------------------------------------------------------------------------------------------
			planetGravitySubAppButton.setOnAction((planetGravitySubAppButtonActionEvent) -> {
				planetGravitySubAppButton.setDisable(true);
				Stage pgsaStage = new Stage();
				pgsaStage.setResizable(false);
				pgsaStage.setOnCloseRequest((closeRequestEvent) -> {
					planetGravitySubAppButton.setDisable(false);
				});

				final TreeMap<String, Property<Double>> values1 = new TreeMap<>();
				values1.put("objectMass", new Property<Double>(1.00));
				values1.put("objectCenterDistanceFromPlanetCenter", new Property<Double>(GravityLaws.PLANET_RADIUS));
				values1.put("distanceFromPlanetCenter", new Property<Double>(GravityLaws.PLANET_RADIUS));

				Pane pgsaRoot = new Pane();

				Circle planet = new Circle(90, Color.rgb(180, 211, 214));
				Text planetText = new Text("Planet");
				planetText.setFont(Font.font(35));
				Text radiusText = new Text("radius:"+String.valueOf(GravityLaws.PLANET_RADIUS)+" meters");
				radiusText.relocate(30, 90);
				radiusText.setFill(Color.RED);
				radiusText.setFont(Font.font("System", FontWeight.BOLD, 13));
				Text rText1 = new Text(String.valueOf(GravityLaws.PLANET_RADIUS));
				rText1.relocate(100, 105);
				rText1.setFill(Color.RED);
				rText1.setFont(Font.font("System", FontWeight.THIN, 15));
				Circle object = new Circle(50, Color.rgb(180, 211, 214));
				Text objectText = new Text("Object");
				objectText.setFill(Color.BLACK);
				objectText.setFont(Font.font(20));

				Label objectMassLabel = new Label("An object mass (kilograms):");
				TextField objectMassField = new TextField("1");

				pgsaRoot.getChildren().addAll(planet, planetText, new Line(100, 90, 550, 90), radiusText, rText1, object, objectText);
				planet.relocate(20, 10);
				planetText.relocate(50, 40);
				rText1.relocate(310, 70);
				object.relocate(555, 40);
				objectText.relocate(575, 75);

				Label objectCenterDistanceFromPlanetCenterLabel = new Label("Object center distance from planet center (meters):");
				TextField objectCenterDistanceFromPlanetCenterField = new TextField(String.valueOf(GravityLaws.PLANET_RADIUS));

				Label gravityBetweenPlanetAndObjectLabel = new Label("Gravity force between planet and object (newtons):");
				TextField gravityBetweenPlanetAndObjectField = new TextField(String.valueOf(
					GravityLaws.gravityBetweenPlanetAndObject( values1.get("objectMass").get().doubleValue(), 
						values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue() ) ));
				gravityBetweenPlanetAndObjectField.setEditable(false);

				Label objectPotentialEnergyLabel = new Label("Object potential energy (joules):");
				TextField objectPotentialEnergyField = new TextField(String.valueOf(
					GravityLaws.potentialEnergy(values1.get("objectMass").get().doubleValue(), GravityLaws.PLANET_MASS, 
						values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue()) ));
				objectPotentialEnergyField.setEditable(false);

				Label objectGravitionalWorkLabel = 
					new Label("Object gravitional work (joule/kilogram | meter\u00b2/second\u00b2)");
				TextField objectGravitionalWorkField = new TextField(String.valueOf(
					GravityLaws.work(values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue()) ));
				objectGravitionalWorkField.setEditable(false);

				Label distanceFromPlanetCenterLabel = new Label("Distance from planet center (meters):");
				TextField distanceFromPlanetCenterField = new TextField(String.valueOf( GravityLaws.PLANET_RADIUS ));

				Label planetFieldStrengthLabel = new Label("Planet field strength at {distance from planet center} "
					+"(newton/kilogram | meter/second\u00b2):");
				TextField planetFieldStrengthField;
				{
					double value = values1.get("distanceFromPlanetCenter").get().doubleValue();
					planetFieldStrengthField = new TextField(String.valueOf(
						GravityLaws.fieldStrength( value ) ));
				}
				planetFieldStrengthField.setEditable(false);

				values1.put("objectMass", new Property<Double>(1.00){
					@Override
					public void set(Double value){
						this.value = value;
						{
							double v = values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue();
							gravityBetweenPlanetAndObjectField.setText(String.valueOf(
								GravityLaws.gravityBetweenPlanetAndObject(values1.get("objectMass").get().doubleValue(), v) ));
						}
						{
							double v = values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue();
							objectPotentialEnergyField.setText(String.valueOf(
								GravityLaws.potentialEnergy(values1.get("objectMass").get().doubleValue(),
								 GravityLaws.PLANET_MASS, v) ));
						}
					}
				});

				values1.put("objectCenterDistanceFromPlanetCenter", new Property<Double>(GravityLaws.PLANET_RADIUS){
					@Override
					public void set(Double value){
						this.value = value;
						rText1.setText(String.valueOf(value));
						{
							double v = values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue();
							gravityBetweenPlanetAndObjectField.setText(String.valueOf(
								GravityLaws.gravityBetweenPlanetAndObject(values1.get("objectMass").get().doubleValue(),v) ));
						}
						{
							double v = values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue();
							objectPotentialEnergyField.setText(String.valueOf(
								GravityLaws.potentialEnergy(values1.get("objectMass").get().doubleValue(),
								 GravityLaws.PLANET_MASS, v) ));
						}
						{
							double v = values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue();
							objectGravitionalWorkField.setText(String.valueOf( GravityLaws.work(v) ));
						}
					}
				});
				values1.put("distanceFromPlanetCenter", new Property<Double>(GravityLaws.PLANET_RADIUS){
					@Override
					public void set(Double value){
						this.value = value;
						{
							double v = values1.get("distanceFromPlanetCenter").get().doubleValue();
							planetFieldStrengthField.setText(String.valueOf(
								GravityLaws.fieldStrength(v) ));
						}
					}
				});

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values1.get("objectMass").get().doubleValue();
						if(	isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current)
								values1.get("objectMass").set(current);
						}
					};
					objectMassField.textProperty().addListener(textChangedEvent);
				}

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values1.get("objectCenterDistanceFromPlanetCenter").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current)
								values1.get("objectCenterDistanceFromPlanetCenter").set(current);
						}
					};
					objectCenterDistanceFromPlanetCenterField.textProperty().addListener(textChangedEvent);
				}

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values1.get("distanceFromPlanetCenter").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current)
								values1.get("distanceFromPlanetCenter").set(current);
						}
					};
					distanceFromPlanetCenterField.textProperty().addListener(textChangedEvent);
				}

				pgsaRoot.getChildren().addAll(objectMassLabel, objectMassField, objectCenterDistanceFromPlanetCenterLabel,
					objectCenterDistanceFromPlanetCenterField, gravityBetweenPlanetAndObjectLabel, 
					gravityBetweenPlanetAndObjectField, objectPotentialEnergyLabel, objectPotentialEnergyField,
					objectGravitionalWorkLabel, objectGravitionalWorkField, distanceFromPlanetCenterLabel,
					distanceFromPlanetCenterField, planetFieldStrengthLabel, planetFieldStrengthField);

				for(Object e : pgsaRoot.getChildren()){
					if(e instanceof Label){
						((Label)e).setFont(Font.font(15));
						((Label)e).setStyle("-fx-text-fill: white;");
					}else if(e instanceof TextField){
						((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
							"-fx-text-fill: white;");
						((TextField)e).setShape(FIELD_RECT);
						((TextField)e).setPrefSize(200, 40);
						((TextField)e).setFont(Font.font(15));
					}
				}

				pgsaRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

				objectMassLabel.relocate(20, 200);
				objectMassField.relocate(20, 230);
				objectCenterDistanceFromPlanetCenterLabel.relocate(370, 200);
				objectCenterDistanceFromPlanetCenterField.relocate(370, 230);
				gravityBetweenPlanetAndObjectLabel.relocate(20, 300);
				gravityBetweenPlanetAndObjectField.relocate(20, 330);
				objectPotentialEnergyLabel.relocate(370, 300);
				objectPotentialEnergyField.relocate(370, 330);
				objectGravitionalWorkLabel.relocate(20, 400);
				objectGravitionalWorkField.relocate(20, 430);
				distanceFromPlanetCenterLabel.relocate(20, 500);
				distanceFromPlanetCenterField.relocate(20, 530);
				planetFieldStrengthLabel.relocate(20, 600);
				planetFieldStrengthField.relocate(20, 630);
				
				pgsaStage.setScene(new Scene(pgsaRoot, 720, 680));
				pgsaStage.show();
			});
			//-------------------------------------------------------------------------------------------------------

			//-------------------------------------------------------------------------------------------------------
			objectsGravitySubAppButton.setOnAction((objectsGravitySubAppActionEvent) -> {
				objectsGravitySubAppButton.setDisable(true);
				Stage ogsaStage = new Stage();
				ogsaStage.setResizable(false);
				ogsaStage.setOnCloseRequest((closeRequestEvent) -> {
					objectsGravitySubAppButton.setDisable(false);
				});

				Pane ogsaRoot = new Pane();

				Circle objectA = new Circle(50, Color.rgb(180, 211, 214));
				Text AText = new Text("A");
				AText.setFill(Color.WHITE);
				AText.setFont(Font.font(85));
				Circle objectB = new Circle(50, Color.rgb(180, 211, 214));
				Text BText = new Text("B");
				BText.setFill(Color.WHITE);
				BText.setFont(Font.font(85));
				Text rText = new Text("1");
				rText.setFill(Color.WHITE);
				rText.setFont(Font.font("System", FontWeight.THIN, 30));
				Text force1Text = new Text(String.valueOf(GravityLaws.gravityLaw(1, 1, 1)));
				force1Text.setFill(Color.WHITE);
				force1Text.setFont(Font.font("System", FontWeight.THIN, 15));
				Text force2Text = new Text(String.valueOf(GravityLaws.gravityLaw(1, 1, 1)));
				force2Text.setFill(Color.WHITE);
				force2Text.setFont(Font.font("System", FontWeight.THIN, 15));

				final TreeMap<String, Property<Double>> values = new TreeMap<>();
				values.put("R", new Property<Double>(1.00));
				values.put("objectAMass", new Property<Double>(1.00));
				values.put("objectBMass", new Property<Double>(1.00));
				
				Label distanceBetweenAAndBCentersLabel = new Label("Distance between A & B centers (meters):");
				TextField distanceBetweenAAndBCentersField = new TextField("1");

				Label objectAMassLabel = new Label("Object A mass (kilograms):");
				TextField objectAMassField = new TextField("1");

				Label objectBMassLabel = new Label("Object B mass (kilograms):");
				TextField objectBMassField = new TextField("1");

				Label gravityForceLabel = new Label("Gravity force between A & B (newtons):");
				TextField gravityForceField = new TextField(String.valueOf(
					GravityLaws.gravityLaw(1, 1, 1) ));
				gravityForceField.setEditable(false);

				Button objAPropertiesButton = new Button("Object A properties");
				//-------------------------------------------------------------------------------------------------
				{
					Circle planetCircleA = new Circle(90, Color.rgb(180, 211, 214));
					Text planetTextA = new Text("Planet");
					planetTextA.setFont(Font.font(35));
					Text radiusTextA = new Text("radius:"+String.valueOf(GravityLaws.PLANET_RADIUS)+" meters");
					radiusTextA.relocate(30, 90);
					radiusTextA.setFill(Color.RED);
					radiusTextA.setFont(Font.font("System", FontWeight.BOLD, 13));
					Text rTextA = new Text(String.valueOf(GravityLaws.PLANET_RADIUS));
					rTextA.relocate(100, 105);
					rTextA.setFill(Color.RED);
					rTextA.setFont(Font.font("System", FontWeight.THIN, 15));
					Circle objectACircle = new Circle(50, Color.rgb(180, 211, 214));
					Text objectAText = new Text("Object");
					objectAText.setFill(Color.BLACK);
					objectAText.setFont(Font.font(20));
					Stage objAPropertiesStage = new Stage();
					objAPropertiesStage.setResizable(false);
					objAPropertiesStage.setOnCloseRequest((closeRequestEvent) -> objAPropertiesButton.setDisable(false));
					Pane oapRoot = new Pane();

					final TreeMap<String, Property<Double>> oapValues = new TreeMap<>();
					oapValues.put("AR", new Property<Double>(GravityLaws.PLANET_RADIUS));
					oapValues.put("AFromR", new Property<Double>(1.00));

					Label distanceBetweenAAndPlanetCentersLabel = new Label("Distance between A & Planet centers (meters):");
					TextField distanceBetweenAAndPlanetCentersField = new TextField(String.valueOf(GravityLaws.PLANET_RADIUS));

					Label gravityBetweenPlanetAndObjALabel = new Label("Gravity force between planet & Object A (newtons):");
					TextField gravityBetweenPlanetAndObjAField = 
						new TextField(String.valueOf( GravityLaws.gravityBetweenPlanetAndObject(
							values.get("objectAMass").get().doubleValue(), values.get("R").get().doubleValue()) ));
					gravityBetweenPlanetAndObjAField.setEditable(false);

					Label objAPotentialEnergyLabel = new Label("Object A potential energy (joules):");
					TextField objAPotentialEnergyField = new TextField(String.valueOf(
						GravityLaws.potentialEnergy(values.get("objectAMass").get().doubleValue(), GravityLaws.PLANET_RADIUS,
						 oapValues.get("AR").get().doubleValue()) ));
					objAPotentialEnergyField.setEditable(false);

					Label objAGravitionalWorkLabel = new Label("Object A gravitional work (joule/kilogram):");
					TextField objAGravitionalWorkField = new TextField(String.valueOf(
						GravityLaws.work(oapValues.get("AR").get().doubleValue()) ));
					objAGravitionalWorkField.setEditable(false);			
				
					Label distanceFromObjACenterLabel = new Label("Distance from object A center (meters):");
					TextField distanceFromObjACenterField = new TextField("1");

					Label objAFieldStrengthLabel = new Label("Object A field strength (newton/kilogram | meter/second\u00b2):");
					TextField objAFieldStrengthField = new TextField(String.valueOf(
						GravityLaws.gravityLaw(values.get("objectAMass").get().doubleValue(), 1,
						 oapValues.get("AFromR").get().doubleValue()) ));
					objAFieldStrengthField.setDisable(false);

					oapValues.put("AR", new Property<Double>(GravityLaws.PLANET_RADIUS){
						@Override
						public void set(Double value){
							this.value = value;
							{
								double v = values.get("R").get().doubleValue();
								gravityBetweenPlanetAndObjAField.setText(String.valueOf( 
									GravityLaws.gravityBetweenPlanetAndObject(values.get("objectAMass").get().doubleValue(), v) ));
							}
							{
								double v = oapValues.get("AR").get().doubleValue();
								objAPotentialEnergyField.setText(String.valueOf(
									GravityLaws.potentialEnergy(
										values.get("objectAMass").get().doubleValue(), GravityLaws.PLANET_MASS, v) ));
							}
							{
								double v = oapValues.get("AR").get().doubleValue();
								objAGravitionalWorkField.setText(String.valueOf(
									GravityLaws.work(v) ));
							}
							rTextA.setText(value.toString());
						}
					});

					oapValues.put("AFromR", new Property<Double>(1.00){
						@Override
						public void set(Double value){
							this.value = value;
							objAFieldStrengthField.setText(String.valueOf(
								GravityLaws.gravityLaw(values.get("objectAMass").get().doubleValue(), 1,
									oapValues.get("AFromR").get().doubleValue()) ));
						}
					});

					oapRoot.getChildren().addAll(planetCircleA, planetTextA, radiusTextA, new Line(100, 90, 550, 90),
					 	rTextA, objectACircle, objectAText,
						distanceBetweenAAndPlanetCentersLabel, distanceBetweenAAndPlanetCentersField,
						gravityBetweenPlanetAndObjALabel, gravityBetweenPlanetAndObjAField, objAPotentialEnergyLabel,
						objAPotentialEnergyField, objAGravitionalWorkLabel, objAGravitionalWorkField, distanceFromObjACenterLabel,
						distanceFromObjACenterField, objAFieldStrengthLabel, objAFieldStrengthField);

					planetCircleA.relocate(20, 10);
					planetTextA.relocate(50, 40);
					rTextA.relocate(310, 70);
					objectACircle.relocate(555, 40);
					objectAText.relocate(575, 75);
					distanceBetweenAAndPlanetCentersLabel.relocate(20, 200);
					distanceBetweenAAndPlanetCentersField.relocate(20, 250);
					gravityBetweenPlanetAndObjALabel.relocate(360, 200);
					gravityBetweenPlanetAndObjAField.relocate(360, 250);
					objAPotentialEnergyLabel.relocate(20, 310);
					objAPotentialEnergyField.relocate(20, 340);
					objAGravitionalWorkLabel.relocate(360, 310);
					objAGravitionalWorkField.relocate(360, 340);
					distanceFromObjACenterLabel.relocate(20, 400);
					distanceFromObjACenterField.relocate(20, 430);
					objAFieldStrengthLabel.relocate(360, 400);
					objAFieldStrengthField.relocate(360, 430);
				
					for(Object e : oapRoot.getChildren()){
						if(e instanceof Label){
							((Label)e).setFont(Font.font(15));
							((Label)e).setStyle("-fx-text-fill: white;");
						}else if(e instanceof TextField){
							((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
								"-fx-text-fill: white;");
							((TextField)e).setShape(FIELD_RECT);
							((TextField)e).setPrefSize(200, 40);
							((TextField)e).setFont(Font.font(15));
						}
					}

					distanceBetweenAAndBCentersField.relocate(20, 20);
					distanceBetweenAAndBCentersLabel.relocate(20, 50);
				
					oapRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

					objAPropertiesStage.setScene(new Scene(oapRoot, 770,490));
					objAPropertiesButton.setOnAction((event) -> objAPropertiesStage.show());

					values.put("objectAMass", new Property<Double>(1.00){
						@Override
						public void set(Double value){
							this.value = value;
							gravityForceField.setText(String.valueOf(
								GravityLaws.gravityLaw(values.get("objectAMass").get(), values.get("objectBMass").get(), this.value) ));
							force1Text.setText(gravityForceField.getText());
							force2Text.setText(gravityForceField.getText());
							gravityBetweenPlanetAndObjAField.setText(String.valueOf(
								GravityLaws.gravityBetweenPlanetAndObject(values.get("objectAMass").get().doubleValue()
									, oapValues.get("AR").get().doubleValue()) ));
							objAPotentialEnergyField.setText(String.valueOf(
								GravityLaws.potentialEnergy(GravityLaws.PLANET_MASS, values.get("objectAMass").get().doubleValue(), 
									oapValues.get("AR").get().doubleValue()) ));
							objAGravitionalWorkField.setText(String.valueOf(
								GravityLaws.work(oapValues.get("AR").get().doubleValue()) ));
							objAFieldStrengthField.setText(String.valueOf(
								GravityLaws.gravityLaw(1, values.get("objectAMass").get().doubleValue(), 
									oapValues.get("AFromR").get().doubleValue()) ));
						}
					});
				}
				//-------------------------------------------------------------------------------------------------
				
				Button objBPropertiesButton = new Button("Object B properties");
				{
					Circle planetCircleB = new Circle(90, Color.rgb(180, 211, 214));
					Text planetTextB = new Text("Planet");
					planetTextB.setFont(Font.font(35));
					Text radiusTextB = new Text("radius:"+String.valueOf(GravityLaws.PLANET_RADIUS)+" meters");
					radiusTextB.relocate(30, 90);
					radiusTextB.setFill(Color.RED);
					radiusTextB.setFont(Font.font("System", FontWeight.BOLD, 13));
					Text rTextB = new Text(String.valueOf(GravityLaws.PLANET_RADIUS));
					rTextB.relocate(100, 105);
					rTextB.setFill(Color.RED);
					rTextB.setFont(Font.font("System", FontWeight.THIN, 15));
					Circle objectBCircle = new Circle(50, Color.rgb(180, 211, 214));
					Text objectBText = new Text("Object");
					objectBText.setFill(Color.BLACK);
					objectBText.setFont(Font.font(20));
					Stage objBPropertiesStage = new Stage();
					objBPropertiesStage.setResizable(false);
					objBPropertiesStage.setOnCloseRequest((closeRequestEvent) -> objBPropertiesButton.setDisable(false));
					Pane obpRoot = new Pane();

					final TreeMap<String, Property<Double>> obpValues = new TreeMap<>();
					obpValues.put("BR", new Property<Double>(GravityLaws.PLANET_RADIUS));
					obpValues.put("BFromR", new Property<Double>(1.00));

					Label distanceBetweenBAndPlanetCentersLabel = new Label("Distance between B & Planet centers (meters):");
					TextField distanceBetweenBAndPlanetCentersField = new TextField(String.valueOf(GravityLaws.PLANET_RADIUS));

					Label gravityBetweenPlanetAndObjBLabel = new Label("Gravity force between planet & Object B (newtons):");
					TextField gravityBetweenPlanetAndObjBField = 
						new TextField(String.valueOf( GravityLaws.gravityBetweenPlanetAndObject(
							values.get("objectBMass").get().doubleValue(), values.get("R").get().doubleValue()) ));
					gravityBetweenPlanetAndObjBField.setEditable(false);

					Label objBPotentialEnergyLabel = new Label("Object B potential energy (joules):");
					TextField objBPotentialEnergyField = new TextField(String.valueOf(
						GravityLaws.potentialEnergy(values.get("objectBMass").get().doubleValue(), GravityLaws.PLANET_RADIUS,
						 obpValues.get("BR").get().doubleValue()) ));
					objBPotentialEnergyField.setEditable(false);

					Label objBGravitionalWorkLabel = new Label("Object B gravitional work (joule/kilogram):");
					TextField objBGravitionalWorkField = new TextField(String.valueOf(
						GravityLaws.work(obpValues.get("BR").get().doubleValue()) ));
					objBGravitionalWorkField.setEditable(false);			
				
					Label distanceFromObjBCenterLabel = new Label("Distance from object B center (meters):");
					TextField distanceFromObjBCenterField = new TextField("1");

					Label objBFieldStrengthLabel = new Label("Object B field strength (newton/kilogram | meter/second\u00b2):");
					TextField objBFieldStrengthField = new TextField(String.valueOf(
						GravityLaws.gravityLaw(values.get("objectBMass").get().doubleValue(), 1,
						 obpValues.get("BFromR").get().doubleValue()) ));
					objBFieldStrengthField.setDisable(false);

					obpValues.put("BR", new Property<Double>(GravityLaws.PLANET_RADIUS){
						@Override
						public void set(Double value){
							this.value = value;
							{
								double v = values.get("R").get().doubleValue();
								gravityBetweenPlanetAndObjBField.setText(String.valueOf( GravityLaws.gravityBetweenPlanetAndObject(
									values.get("objectBMass").get().doubleValue(),v) ));
							}
							{
								double v = obpValues.get("BR").get().doubleValue();
								objBPotentialEnergyField.setText(String.valueOf(
									GravityLaws.potentialEnergy(
										values.get("objectBMass").get().doubleValue(), GravityLaws.PLANET_MASS, v) ));
							}
							{
								double v = obpValues.get("BR").get().doubleValue();
								objBGravitionalWorkField.setText(String.valueOf(
									GravityLaws.work(v) ));
							}
							rTextB.setText(value.toString());
						}
					});

					obpValues.put("BFromR", new Property<Double>(1.00){
						@Override
						public void set(Double value){
							this.value = value;
							objBFieldStrengthField.setText(String.valueOf(
								GravityLaws.gravityLaw(values.get("objectBMass").get().doubleValue(), 1,
									obpValues.get("BFromR").get().doubleValue()) ));
						}
					});

					obpRoot.getChildren().addAll(planetCircleB, planetTextB, radiusTextB, new Line(100, 90, 550, 90),
					 	rTextB, objectBCircle, objectBText,
						distanceBetweenBAndPlanetCentersLabel, distanceBetweenBAndPlanetCentersField,
						gravityBetweenPlanetAndObjBLabel, gravityBetweenPlanetAndObjBField, objBPotentialEnergyLabel,
						objBPotentialEnergyField, objBGravitionalWorkLabel, objBGravitionalWorkField, distanceFromObjBCenterLabel,
						distanceFromObjBCenterField, objBFieldStrengthLabel, objBFieldStrengthField);

					planetCircleB.relocate(20, 10);
					planetTextB.relocate(50, 40);
					rTextB.relocate(310, 70);
					objectBCircle.relocate(555, 40);
					objectBText.relocate(575, 75);
					distanceBetweenBAndPlanetCentersLabel.relocate(20, 200);
					distanceBetweenBAndPlanetCentersField.relocate(20, 250);
					gravityBetweenPlanetAndObjBLabel.relocate(360, 200);
					gravityBetweenPlanetAndObjBField.relocate(360, 250);
					objBPotentialEnergyLabel.relocate(20, 310);
					objBPotentialEnergyField.relocate(20, 340);
					objBGravitionalWorkLabel.relocate(360, 310);
					objBGravitionalWorkField.relocate(360, 340);
					distanceFromObjBCenterLabel.relocate(20, 400);
					distanceFromObjBCenterField.relocate(20, 430);
					objBFieldStrengthLabel.relocate(360, 400);
					objBFieldStrengthField.relocate(360, 430);
				
					for(Object e : obpRoot.getChildren()){
						if(e instanceof Label){
							((Label)e).setFont(Font.font(15));
							((Label)e).setStyle("-fx-text-fill: white;");
						}else if(e instanceof TextField){
							((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
								"-fx-text-fill: white;");
							((TextField)e).setShape(FIELD_RECT);
							((TextField)e).setPrefSize(200, 40);
							((TextField)e).setFont(Font.font(15));
						}
					}

					distanceBetweenAAndBCentersField.relocate(20, 20);
					distanceBetweenAAndBCentersLabel.relocate(20, 50);
				
					obpRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

					objBPropertiesStage.setScene(new Scene(obpRoot, 770,490));
					objBPropertiesButton.setOnAction((event) -> objBPropertiesStage.show());
					values.put("objectBMass", new Property<Double>(1.00){
						@Override
						public void set(Double value){
							this.value = value;
							gravityForceField.setText(String.valueOf(
								GravityLaws.gravityLaw(values.get("objectAMass").get(), values.get("objectBMass").get(), this.value) ));
							force1Text.setText(gravityForceField.getText());
							force2Text.setText(gravityForceField.getText());
							gravityBetweenPlanetAndObjBField.setText(String.valueOf(
								GravityLaws.gravityBetweenPlanetAndObject(values.get("objectBMass").get().doubleValue()
									, obpValues.get("BR").get().doubleValue()) ));
							objBPotentialEnergyField.setText(String.valueOf(
								GravityLaws.potentialEnergy(GravityLaws.PLANET_MASS, values.get("objectBMass").get().doubleValue(), 
									obpValues.get("BR").get().doubleValue()) ));
							objBGravitionalWorkField.setText(String.valueOf(
								GravityLaws.work(obpValues.get("BR").get().doubleValue()) ));
							objBFieldStrengthField.setText(String.valueOf(
								GravityLaws.gravityLaw(1, values.get("objectBMass").get().doubleValue(), 
									obpValues.get("BFromR").get().doubleValue()) ));
						}
					});
				}

				values.put("R", new Property<Double>(1.00){
					@Override
					public void set(Double value){
						this.value = value;
						gravityForceField.setText(String.valueOf(
							GravityLaws.gravityLaw(values.get("objectAMass").get(), values.get("objectBMass").get(), this.value) ));
						rText.setText(value.toString());
						force1Text.setText(gravityForceField.getText());
						force2Text.setText(gravityForceField.getText());
					}
				});
				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("R").get();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current)
								values.get("R").set(current);
						}
					};
					distanceBetweenAAndBCentersField.textProperty().addListener(textChangedEvent);
				}

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						final String key = "objectAMass";
						double last = values.get(key).get();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current)
								values.get(key).set(current);
						}
					};
					objectAMassField.textProperty().addListener(textChangedEvent);
				}

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						final String key = "objectBMass";
						double last = values.get(key).get();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current)
								values.get(key).set(current);
						}
					};
					objectBMassField.textProperty().addListener(textChangedEvent);
				}
				
				ogsaRoot.getChildren().addAll(objectA, AText, new Line(70, 130, 570, 130)/*centers line*/, new Line(70, 70, 70, 130),
					objectB, BText, new Line(570, 70, 570, 130), rText, new Line(130, 70, 300, 70), new Line(340, 70, 510, 70),
					force1Text, force2Text, distanceBetweenAAndBCentersLabel, distanceBetweenAAndBCentersField,
					objectAMassLabel, objectAMassField, objectBMassLabel, objectBMassField, gravityForceLabel, gravityForceField,
					objAPropertiesButton, objBPropertiesButton);
				objectA.relocate(20, 20);
				AText.relocate(42.5, 5);
				objectB.relocate(520, 20);
				BText.relocate(542.5, 5);
				rText.relocate(250, 90);
				force1Text.relocate(130, 50);
				force2Text.relocate(340, 50);
				distanceBetweenAAndBCentersLabel.relocate(180, 150);
				distanceBetweenAAndBCentersField.relocate(220, 180);
				objectAMassLabel.relocate(110, 230);
				objectAMassField.relocate(110, 260);
				objectBMassLabel.relocate(330, 230);
				objectBMassField.relocate(330, 260);
				gravityForceLabel.relocate(180, 310);
				gravityForceField.relocate(220, 340);
				objAPropertiesButton.relocate(110, 400);
				objBPropertiesButton.relocate(330, 400);
				
				for(Object e : ogsaRoot.getChildren()){
					if(e instanceof Label){
						((Label)e).setFont(Font.font(15));
						((Label)e).setStyle("-fx-text-fill: white;");
					}else if(e instanceof TextField){
						((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
							"-fx-text-fill: white;");
						((TextField)e).setShape(FIELD_RECT);
						((TextField)e).setPrefSize(200, 40);
						((TextField)e).setFont(Font.font(15));
					}else if(e instanceof Button){
						((Button)e).setStyle("-fx-text-fill: white;"+
							"-fx-background-color: rgb(100, 100, 100)");
						((Button)e).setOnMousePressed((event) -> {
							((Button)e).setStyle("-fx-text-fill: black;"+
							"-fx-background-color: rgb(200, 200, 200)");
						});
						((Button)e).setOnMouseReleased((event) -> {
							((Button)e).setStyle("-fx-text-fill: white;"+
							"-fx-background-color: rgb(100, 100, 100)");
						});
						((Button)e).setFont(Font.font(15));
						((Button)e).setPrefSize(200, 40);
						((Button)e).setShape(FIELD_RECT);
					}
				}

				ogsaRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

				ogsaStage.setScene(new Scene(ogsaRoot));
				ogsaStage.show();
			});
			//-------------------------------------------------------------------------------------------------------

			//-------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------

			gravityAppRoot.getChildren().addAll(planetMassLabel, planetMassField, planetMassErrorLabel,
				planetRadiusLabel, planetRadiusField, planetRadiusErrorLabel, planetGravitySubAppButton, 
				objectsGravitySubAppButton);

			for(Object e : gravityAppRoot.getChildren()){
				if(e instanceof Label){
					if(!((Label)e).getText().equals("Invalid value")){
						((Label)e).setFont(Font.font(15));
						((Label)e).setStyle("-fx-text-fill: white;");
					}
				}else if(e instanceof TextField){
					((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
						"-fx-text-fill: white;");
					((TextField)e).setShape(FIELD_RECT);
					((TextField)e).setPrefSize(200, 40);
					((TextField)e).setFont(Font.font(15));
				}else if(e instanceof Button){
					((Button)e).setStyle("-fx-text-fill: white;"+
						"-fx-background-color: rgb(100, 100, 100)");
					((Button)e).setOnMousePressed((event) -> {
						((Button)e).setStyle("-fx-text-fill: black;"+
						"-fx-background-color: rgb(200, 200, 200)");
					});
					((Button)e).setOnMouseReleased((event) -> {
						((Button)e).setStyle("-fx-text-fill: white;"+
						"-fx-background-color: rgb(100, 100, 100)");
					});
					((Button)e).setFont(Font.font(15));
					((Button)e).setPrefSize(200, 40);
					((Button)e).setShape(FIELD_RECT);
				}
			}

			planetMassLabel.relocate(20, 20);
			planetMassField.relocate(20, 50);
			planetMassErrorLabel.relocate(20, 95);
			planetRadiusLabel.relocate(240, 20);
			planetRadiusField.relocate(240, 50);
			planetRadiusErrorLabel.relocate(240, 95);
			planetGravitySubAppButton.relocate(20, 110);
			objectsGravitySubAppButton.relocate(240, 110);
			
			gravityAppRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

			gravityAppStage.setScene(new Scene(gravityAppRoot, 450, 160));
			gravityAppStage.show();
		});
		//-----------------------------------------------------------------------------------------------------------
		
		//-----------------------------------------------------------------------------------------------------------
		circularMotionApplicationButton.setOnAction((circularMotionApplicationActionEvent) -> {
			circularMotionApplicationButton.setDisable(true);
			Stage cmaStage = new Stage();
			cmaStage.setResizable(false);
			cmaStage.setOnCloseRequest((closeRequestEvent) -> circularMotionApplicationButton.setDisable(false));

			Pane cmaRoot = new Pane();

			final TreeMap<String, Property<Double>> values = new TreeMap<>();

			values.put("mass", new Property<Double>(1.00));
			values.put("circularPathRadius", new Property<Double>(1.00));
			values.put("velocity", new Property<Double>(1.00));
			values.put("totalTime", new Property<Double>(1.00));
			values.put("cycles", new Property<Double>(1.00));

			Label circularPathRadiusLabel = new Label("Circular path radius (meters):");
			TextField circularPathRadiusField = new TextField("1.00");

			Label massLabel = new Label("Object mass (kilograms):");
			TextField massField = new TextField("1.00");

			Label velocityLabel = new Label("Rotation velocity (meter/second):");
			TextField velocityField = new TextField("1.00");

			Label totalTimeLabel = new Label("Total time (seconds):");
			TextField totalTimeField = new TextField("1.00");

			Label cyclesLabel = new Label("Cycless:");
			TextField cyclesField = new TextField("1");

			Label singleCyclesTimeLabel = new Label("Single cycle time (seconds):");
			TextField singleCyclesTimeField = new TextField("1.00");
			singleCyclesTimeField.setEditable(false);

			Label frequencyLabel = new Label("Frequency (hertz):");
			TextField frequencyField = new TextField("1.00");
			frequencyField.setEditable(false);

			Label angularVelocityLabel = new Label("Angular velocity (radian/second):");
			TextField angularVelocityField = new TextField(String.valueOf(CircularMotionLaws.angularVelocity(1)));
			angularVelocityField.setEditable(false);

			Label angularAccelerationLabel = new Label("Angular acceleration (meter/second\u00b2):");
			TextField angularAccelerationField = new TextField(String.valueOf(CircularMotionLaws.angularAcceleration(1, 1)));
			angularAccelerationField.setEditable(false);

			Label centripetalForceLabel = new Label("Centripetal force (newtons):");
			TextField centripetalForceField = new TextField(String.valueOf( 
				CircularMotionLaws.centripetalForce(values.get("mass").get().doubleValue(), 1.00, 1.00) ));
			centripetalForceField.setEditable(false);

			values.put("mass", new Property<Double>(1.00){
				@Override
				public void set(Double value){
					this.value = value;
					//massField.setText(value.toString());
					centripetalForceField.setText( 
						String.valueOf( CircularMotionLaws.centripetalForce(value.doubleValue(), 
							values.get("velocity").get().doubleValue(), values.get("circularPathRadius").get().doubleValue()) ) );
				}
			});
			values.put("circularPathRadius", new Property<Double>(1.00){
				@Override
				public void set(Double value){
					this.value = value;
					//circularPathRadiusField.setText(value.toString());
					values.get("velocity").set( CircularMotionLaws.velocity( values.get("circularPathRadius").get().doubleValue(), 
						Double.parseDouble( singleCyclesTimeField.getText() ) ) );
				}
			});

			values.put("velocity", new Property<Double>(1.00){
				@Override
				public void set(Double value){
					this.value = value;
					//velocityField.setText(value.toString());
					angularAccelerationField.setText(String.valueOf(
						CircularMotionLaws.angularAcceleration(value.doubleValue(), 
							values.get("circularPathRadius").get().doubleValue()) ));
					centripetalForceField.setText(String.valueOf(
						CircularMotionLaws.centripetalForce(values.get("mass").get().doubleValue(), value.doubleValue(), 
							values.get("circularPathRadius").get().doubleValue()) ));
				}
			});

			values.put("totalTime", new Property<Double>(1.00){
				@Override
				public void set(Double value){
					this.value = value;
					//totalTimeField.setText(value.toString());
					singleCyclesTimeField.setText(
						String.valueOf( values.get("totalTime").get().doubleValue()/values.get("cycles").get().intValue() ));
					frequencyField.setText( String.valueOf( 1/Double.parseDouble(singleCyclesTimeField.getText()) ) );
					values.get("velocity").set( CircularMotionLaws.velocity( 
						values.get("circularPathRadius").get().doubleValue(), Double.parseDouble(singleCyclesTimeField.getText()) ) );
				}
			});
			values.put("cycles", new Property<Double>(1.00){
				@Override
				public void set(Double value){
					this.value = (double) value.intValue();
					//cyclesField.setText(String.valueOf(value.intValue()));
					values.get("totalTime").set( 
						values.get("totalTime").get().doubleValue()/Double.parseDouble(singleCyclesTimeField.getText()) );
				}
			});

			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					double last = values.get("circularPathRadius").get();
					if(isDecimalLiteral(currentText)){
						double current = Double.parseDouble(currentText);
						if(last != current)
							values.get("circularPathRadius").set(current);
					}
				};
				circularPathRadiusField.textProperty().addListener(textChangedEvent);
			}

			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					double last = values.get("mass").get();
					if(isDecimalLiteral(currentText)){
						double current = Double.parseDouble(currentText);
						if(last != current)
							values.get("mass").set(current);
					}
				};
				massField.textProperty().addListener(textChangedEvent);
			}

			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					double last = values.get("totalTime").get();
					if(isDecimalLiteral(currentText)){
						double current = Double.parseDouble(currentText);
						if(last != current)
							values.get("totalTime").set(current);
					}
				};
				totalTimeField.textProperty().addListener(textChangedEvent);
			}
			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					double last = values.get("cycles").get();
					if(isDecimalLiteral(currentText)){
						double current = Double.parseDouble(currentText);
						if(last != current)
							values.get("cycles").set(current);
					}
				};
				cyclesField.textProperty().addListener(textChangedEvent);
			}
			{
				ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
					double last = values.get("velocity").get();
					if(isDecimalLiteral(currentText)){
						double current = Double.parseDouble(currentText);
						if(last != current)
							values.get("velocity").set(current);
					}
				};
				velocityField.textProperty().addListener(textChangedEvent);
			}

			{
				ImageView image = new ImageView(new Image("circularMotion.png"));
				cmaRoot.getChildren().addAll(image);
				image.relocate(150, 20);
			}

			cmaRoot.getChildren().addAll(circularPathRadiusLabel, circularPathRadiusField, massLabel, massField,
				velocityLabel, velocityField, totalTimeLabel, totalTimeField, cyclesLabel, cyclesField, 
				singleCyclesTimeLabel, singleCyclesTimeField, frequencyLabel, frequencyField, 
				angularVelocityLabel, angularVelocityField, angularAccelerationLabel, angularAccelerationField,
				centripetalForceLabel, centripetalForceField);

			circularPathRadiusLabel.relocate(20, 160);
			circularPathRadiusField.relocate(20, 190);
			massLabel.relocate(290, 160);
			massField.relocate(290, 190);
			velocityLabel.relocate(20, 260);
			velocityField.relocate(20, 290);
			totalTimeLabel.relocate(290, 260);
			totalTimeField.relocate(290, 290);
			cyclesLabel.relocate(20, 360);
			cyclesField.relocate(20, 390);
			singleCyclesTimeLabel.relocate(290, 360);
			singleCyclesTimeField.relocate(290, 390);
			frequencyLabel.relocate(20, 460);
			frequencyField.relocate(20, 490);
			angularVelocityLabel.relocate(290, 460);
			angularVelocityField.relocate(290, 490);
			angularAccelerationLabel.relocate(20, 560);
			angularAccelerationField.relocate(20, 590);
			centripetalForceLabel.relocate(290, 560);
			centripetalForceField.relocate(290, 590);
			
			for(Object e : cmaRoot.getChildren()){
				if(e instanceof Label){
					if(!((Label)e).getText().equals("Invalid value")){
						((Label)e).setFont(Font.font(15));
						((Label)e).setStyle("-fx-text-fill: white;");
					}
				}else if(e instanceof TextField){
					((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
						"-fx-text-fill: white;");
					((TextField)e).setShape(FIELD_RECT);
					((TextField)e).setPrefSize(200, 40);
					((TextField)e).setFont(Font.font(15));
				}
			}

			cmaRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

			cmaStage.setScene(new Scene(cmaRoot, 510, 650));
			cmaStage.show();
		});
		//-----------------------------------------------------------------------------------------------------------

		//-----------------------------------------------------------------------------------------------------------
		planetsApplicationButton.setOnAction((planetsApplicationButtonActionEvent) -> {
			planetsApplicationButton.setDisable(true);
			Stage paStage = new Stage();
			paStage.setResizable(false);
			paStage.setOnCloseRequest((closeRequestEvent) -> planetsApplicationButton.setDisable(false));

			Pane paRoot = new Pane();
			
			Button planetMassCalculatorButton = new Button("Planet mass calculator");
			planetMassCalculatorButton.setOnAction((actionEvent) -> {
				planetMassCalculatorButton.setDisable(true);
				Stage pmcStage = new Stage();
				pmcStage.setResizable(false);
				pmcStage.setOnCloseRequest((closeRequestEvent) -> planetMassCalculatorButton.setDisable(false));

				final TreeMap<String, Property<Double>> values = new TreeMap<>();
				values.put("planetRadius", new Property<Double>( 64 * Math.pow(10, 5) ));
				values.put("planetAcceleration", new Property<Double>( 
					GravityLaws.gravityLaw(1.00, 6 * Math.pow(10, 24), values.get("planetRadius").get().doubleValue()) ));

				Pane pmcRoot = new Pane();

				Label planetRadiusLabel = new Label("Planet radius (meters):");
				TextField planetRadiusField = new TextField(values.get("planetRadius").toString());

				Label planetAccelerationLabel = 
					new Label("Planet acceleration on surface (meter/second\u00b2 | newton/kilogram):");
				TextField planetAccelerationField = new TextField(values.get("planetAcceleration").toString());

				Label planetMassLabel = new Label("Planet mass (kilograms):");
				TextField planetMassField = new TextField( 
					String.valueOf( GravityLaws.planetMass( values.get("planetAcceleration").get().doubleValue(), 
						values.get("planetRadius").get().doubleValue() ) ) );
				planetMassField.setEditable(false);

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("planetRadius").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current){
								values.get("planetRadius").set(current);
								planetMassField.setText( String.valueOf(
									GravityLaws.planetMass(values.get("planetAcceleration").get().doubleValue(),
									current ) ) );
							}
						}
					};
					planetRadiusField.textProperty().addListener(textChangedEvent);
				}
				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("planetAcceleration").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current){
								values.get("planetAcceleration").set(current);
								planetMassField.setText( String.valueOf(
									GravityLaws.planetMass(current,
									values.get("planetRadius").get().doubleValue() ) ) );
							}
						}
					};
					planetAccelerationField.textProperty().addListener(textChangedEvent);
				}

				pmcRoot.getChildren().addAll(planetRadiusLabel, planetRadiusField, planetAccelerationLabel, planetAccelerationField,
					planetMassLabel, planetMassField);

				planetRadiusLabel.relocate(165, 20);
				planetRadiusField.relocate(140, 50);
				planetAccelerationLabel.relocate(20, 110);
				planetAccelerationField.relocate(140, 140);
				planetMassLabel.relocate(160, 200);
				planetMassField.relocate(140, 230);

				for(Object e : pmcRoot.getChildren()){
					if(e instanceof Label){
						if(!((Label)e).getText().equals("Invalid value")){
							((Label)e).setFont(Font.font(15));
							((Label)e).setStyle("-fx-text-fill: white;");
						}
					}else if(e instanceof TextField){
						((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
							"-fx-text-fill: white;");
						((TextField)e).setShape(FIELD_RECT);
						((TextField)e).setPrefSize(200, 40);
						((TextField)e).setFont(Font.font(15));
					}
				}

				pmcRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

				pmcStage.setScene(new Scene(pmcRoot, 460, 290));
				pmcStage.show();
			});

			Button planetRadiusCalculatorButton = new Button("Planet radius calculator");
			planetRadiusCalculatorButton.setOnAction((actionEvent) -> {
				planetRadiusCalculatorButton.setDisable(true);
				Stage prcStage = new Stage();
				prcStage.setResizable(false);
				prcStage.setOnCloseRequest((closeRequestEvent) -> planetRadiusCalculatorButton.setDisable(false));

				final TreeMap<String, Property<Double>> values = new TreeMap<>();
				values.put("planetMass", new Property<Double>( 6 * Math.pow(10, 24) ));
				values.put("planetAcceleration", new Property<Double>( 
					GravityLaws.gravityLaw(1.00, 6 * Math.pow(10, 24), 64 * Math.pow(10, 5)) ));

				Pane prcRoot = new Pane();

				Label planetMassLabel = new Label("Planet mass (kilograms):");
				TextField planetMassField = new TextField( (6 * Math.pow(10, 24))+"");

				Label planetAccelerationLabel = 
					new Label("Planet acceleration on surface (meter/second\u00b2 | newton/kilogram):");
				TextField planetAccelerationField = new TextField(values.get("planetAcceleration").toString());

				Label planetRadiusLabel = new Label("Planet radius (meters):");
				TextField planetRadiusField = new TextField( 
					GravityLaws.planetRadius( values.get("planetMass").get().doubleValue(), 
						values.get("planetAcceleration").get().doubleValue() )+"" );
				planetRadiusField.setEditable(false);

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("planetMass").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current){
								values.get("planetMass").set(current);
								planetRadiusField.setText( String.valueOf( GravityLaws.planetRadius( 
									current, values.get("planetAcceleration").get().doubleValue() ) ) );
							}
						}
					};
					planetMassField.textProperty().addListener(textChangedEvent);
				}
				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("planetAcceleration").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current){
								values.get("planetAcceleration").set(current);
								planetRadiusField.setText( String.valueOf( GravityLaws.planetRadius( 
									values.get("planetMass").get().doubleValue(), current ) ) );
							}
						}
					};
					planetAccelerationField.textProperty().addListener(textChangedEvent);
				}

				prcRoot.getChildren().addAll(planetMassLabel, planetMassField, planetAccelerationLabel, planetAccelerationField,
					planetRadiusLabel, planetRadiusField);

				planetMassLabel.relocate(165, 20);
				planetMassField.relocate(140, 50);
				planetAccelerationLabel.relocate(20, 110);
				planetAccelerationField.relocate(140, 140);
				planetRadiusLabel.relocate(160, 200);
				planetRadiusField.relocate(140, 230);

				for(Object e : prcRoot.getChildren()){
					if(e instanceof Label){
						if(!((Label)e).getText().equals("Invalid value")){
							((Label)e).setFont(Font.font(15));
							((Label)e).setStyle("-fx-text-fill: white;");
						}
					}else if(e instanceof TextField){
						((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
							"-fx-text-fill: white;");
						((TextField)e).setShape(FIELD_RECT);
						((TextField)e).setPrefSize(200, 40);
						((TextField)e).setFont(Font.font(15));
					}
				}

				prcRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

				prcStage.setScene(new Scene(prcRoot, 460, 290));
				prcStage.show();
			});

			Button planetAccelerationCalculatorButton = new Button("Planet acceleration calculator");
			planetAccelerationCalculatorButton.setOnAction((actionEvent) -> {
				planetAccelerationCalculatorButton.setDisable(true);
				Stage pacStage = new Stage();
				pacStage.setResizable(false);
				pacStage.setOnCloseRequest((closeRequestEvent) -> planetAccelerationCalculatorButton.setDisable(false));

				final TreeMap<String, Property<Double>> values = new TreeMap<>();
				values.put("planetMass", new Property<Double>( 6 * Math.pow(10, 24) ));
				values.put("planetRadius", new Property<Double>( 64 * Math.pow(10, 5) ));

				Pane pacRoot = new Pane();

				Label planetMassLabel = new Label("Planet mass (kilograms):");
				TextField planetMassField = new TextField( (6 * Math.pow(10, 24))+"");

				Label planetRadiusLabel = new Label("Planet radius (meters):");
				TextField planetRadiusField = new TextField(values.get("planetRadius").toString());

				Label planetAccelerationLabel = 
				new Label("Planet acceleration on surface (meter/second\u00b2 | newton/kilogram):");
				TextField planetAccelerationField = new TextField(
					String.valueOf( GravityLaws.gravityLaw(1.00, values.get("planetMass").get().doubleValue(), 
						values.get("planetRadius").get().doubleValue()) ));
				planetAccelerationField.setEditable(false);

				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("planetMass").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current){
								values.get("planetMass").set(current);
								planetAccelerationField.setText(
									String.valueOf( GravityLaws.gravityLaw(1.00, current, 
										values.get("planetRadius").get().doubleValue()) ));
							}
						}
					};
					planetMassField.textProperty().addListener(textChangedEvent);
				}
				{
					ChangeListener<String> textChangedEvent = (observable, lastText, currentText) -> {
						double last = values.get("planetRadius").get().doubleValue();
						if(isDecimalLiteral(currentText)){
							double current = Double.parseDouble(currentText);
							if(last != current){
								values.get("planetRadius").set(current);
								planetAccelerationField.setText(
									String.valueOf( GravityLaws.gravityLaw(1.00, values.get("planetMass").get().doubleValue(), 
										current) ));
							}
						}
					};
					planetRadiusField.textProperty().addListener(textChangedEvent);
				}

				pacRoot.getChildren().addAll(planetMassLabel, planetMassField, planetRadiusLabel, planetRadiusField,
					planetAccelerationLabel, planetAccelerationField);

				planetMassLabel.relocate(165, 20);
				planetMassField.relocate(140, 50);
				planetRadiusLabel.relocate(160, 110);
				planetRadiusField.relocate(140, 140);
				planetAccelerationLabel.relocate(20, 200);
				planetAccelerationField.relocate(140, 230);

				for(Object e : pacRoot.getChildren()){
					if(e instanceof Label){
						if(!((Label)e).getText().equals("Invalid value")){
							((Label)e).setFont(Font.font(15));
							((Label)e).setStyle("-fx-text-fill: white;");
						}
					}else if(e instanceof TextField){
						((TextField)e).setStyle("-fx-background-color: rgb(75, 75, 75);"+
							"-fx-text-fill: white;");
						((TextField)e).setShape(FIELD_RECT);
						((TextField)e).setPrefSize(200, 40);
						((TextField)e).setFont(Font.font(15));
					}
				}

				pacRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

				pacStage.setScene(new Scene(pacRoot, 460, 290));
				pacStage.show();
			});

			paRoot.getChildren().addAll(planetMassCalculatorButton, planetRadiusCalculatorButton, planetAccelerationCalculatorButton);
			planetMassCalculatorButton.relocate(20, 20);
			planetRadiusCalculatorButton.relocate(20, 80);
			planetAccelerationCalculatorButton.relocate(20, 140);

			for(Object e : paRoot.getChildren()){
				((Button)e).setStyle("-fx-text-fill: white;"+
				"-fx-background-color: rgb(100, 100, 100)");
				((Button)e).setOnMousePressed((event) -> {
					((Button)e).setStyle("-fx-text-fill: black;"+
					"-fx-background-color: rgb(200, 200, 200)");
				});
				((Button)e).setOnMouseReleased((event) -> {
					((Button)e).setStyle("-fx-text-fill: white;"+
					"-fx-background-color: rgb(100, 100, 100)");
				});
				((Button)e).setFont(Font.font(15));
				((Button)e).setPrefSize(250, 40);
				((Button)e).setShape(FIELD_RECT);
			}

			paRoot.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));
			
			paStage.setScene(new Scene(paRoot, 280, 190));
			paStage.show();
		});
		//-----------------------------------------------------------------------------------------------------------
		root.getChildren().addAll(gravityApplicationButton, circularMotionApplicationButton, planetsApplicationButton);

		gravityApplicationButton.relocate(25, 25);
		circularMotionApplicationButton.relocate(255, 25);
		planetsApplicationButton.relocate(485, 25);

		for(Object e : root.getChildren()){
			((Button)e).setStyle("-fx-text-fill: white;"+
			"-fx-background-color: rgb(100, 100, 100)");
			((Button)e).setOnMousePressed((event) -> {
				((Button)e).setStyle("-fx-text-fill: black;"+
				"-fx-background-color: rgb(200, 200, 200)");
			});
			((Button)e).setOnMouseReleased((event) -> {
				((Button)e).setStyle("-fx-text-fill: white;"+
				"-fx-background-color: rgb(100, 100, 100)");
			});
			((Button)e).setFont(Font.font(15));
			((Button)e).setPrefSize(200, 40);
			((Button)e).setShape(FIELD_RECT);
		}

		root.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), null, null)));

		primaryStage.setScene(new Scene(root, 715, 80));
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
