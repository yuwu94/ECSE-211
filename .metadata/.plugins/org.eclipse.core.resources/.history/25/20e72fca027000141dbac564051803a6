import lejos.nxt.LightSensor;

public class CSTest {
		ColorSensor rightCS = new ColorSensor(SensorPort.S2);
		ColorSensor leftCS = new ColorSensor(SensorPort.S3);

		leftCS.setFloodlight(Color.GREEN);
		rightCS.setFloodlight(Color.GREEN);

		int leftValue = leftCS.readNormalizedValue();
		int rightValue = rightCS.readNormalizedValue();
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString(leftValue, 0, 0);
			LCD.drawString(rightValue, 0, 1);

}