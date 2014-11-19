import lejos.nxt.*;
 
public class NavTest {
//      final static double width = 16.01;                                     
//      final static double radiusL =2.21, radiusR = 2.21;
       
        public static void main(String[] args) {
                int buttonChoice;
                // objects that need to be instantiated
                double travelX = 60.0;
                double travelY = 30.0;
                WheelDriver wheelDriver = new WheelDriver(Motor.A, Motor.B);
                Odometer odometer = new Odometer();
                Navigation navigator = new Navigation(travelX, travelY, odometer, wheelDriver);
 
                do {
                        // clear the display
                        LCD.clear();
 
                        //user decides which to execute
                        LCD.drawString("< Left | Right >", 0, 0);
                        LCD.drawString("       |        ", 0, 1);
                        LCD.drawString(" Float | Nav    ", 0, 2);
                        LCD.drawString("motors |        ", 0, 3);
 
 
                        buttonChoice = Button.waitForAnyPress();
                }
                while (buttonChoice != Button.ID_LEFT
                                && buttonChoice != Button.ID_RIGHT);
 
                if (buttonChoice == Button.ID_LEFT) {
                        for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B, Motor.C }) {
                                motor.forward();
                                motor.flt();
                        }
                        // start only the odometer and the odometry display
                        odometer.start();
                        //odometryDisplay.start();
 
                } else {
                        odometer.start();
                        //odometryDisplay.start();  
                        navigator.travelTo(new Waypoint(travelX, travelY));;
                }
               
                while (Button.waitForAnyPress() != Button.ID_ESCAPE);
                System.exit(0);
        }
}