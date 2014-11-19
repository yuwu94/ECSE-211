import lejos.nxt.*;
public class blockPickUp<UltrasonicController> {
        public static final int ROTATE_SPEED = 150;
        public final double leftRadius = 2.1;
        public final double rightRadius = 2.15;
        private UltrasonicSensor sensor;
        private NXTRegulatedMotor leftMotor,rightMotor;
        WheelDriver wheels = new WheelDriver( leftMotor, rightMotor) ;
        Odometer odo = new Odometer();
        Navigation nav = new Navigation(leftRadius, rightRadius, odo, wheels);
        private UltrasonicPoller poller;
        private UltrasonicController usController ;
       
       
        public static void findRange (NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, double leftRadius, double rightRadius, double width) {
                // reset the motors
                for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { leftMotor, rightMotor }) {
                        motor.stop();
                        motor.setAcceleration(10000);
                }              
                // wait 5 seconds
                try {
                        Thread.sleep(2000);
                }
                catch (InterruptedException e) {
                        // there is nothing to be done here because it is not expected that
                        // the odometer will be interrupted by another thread
                }
                        // turn 90 degrees clockwise
                        leftMotor.setSpeed(ROTATE_SPEED);
                        rightMotor.setSpeed(ROTATE_SPEED);
 
                        leftMotor.rotate(-convertAngle(leftRadius, width, 45.0), true);
                        rightMotor.rotate(convertAngle(rightRadius, width, 45.0), false);
                       
 
                        leftMotor.rotate(convertAngle(leftRadius, width, 90), true);
                        rightMotor.rotate(-convertAngle(rightRadius, width, 90), false);
        }
       
       
        private static int convertDistance(double radius, double distance) {
                return (int) ((180.0 * distance) / (Math.PI * radius));
        }
 
        private static int convertAngle(double radius, double width, double angle) {
                return convertDistance(radius, Math.PI * width * angle / 360.0);
        }
       
       
       
       
       
       
       
       
       
       
       
       
}