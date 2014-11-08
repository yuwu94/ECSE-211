

import lejos.nxt.*;

public class Week3Demo {
	public static void main(String[] args) {
		int buttonChoice;

		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString("  Claw | Drive  ", 0, 2);
			LCD.drawString("       |        ", 0, 3);
			LCD.drawString("       |        ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT
				&& buttonChoice != Button.ID_RIGHT);

		if (buttonChoice == Button.ID_LEFT) {
			NXTRegulatedMotor ClawMotor = Motor.C;
			ClawMotor.flt();
			LCD.clear();
			while(true){
				LCD.drawString("Claw Tachometer", 0, 0);
				LCD.drawInt(ClawMotor.getTachoCount(), 0, 1);
				if(Button.waitForAnyPress() == Button.ID_ESCAPE){
					break;
				}
			}


		} else {

			// spawn a new Thread to avoid SquareDriver.drive() from blocking
			(new Thread() {
				public void run() {
					SquareDriver.drive(Motor.A, Motor.B, 2, 2, 15.24);
				}
			}).start();
		}
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}