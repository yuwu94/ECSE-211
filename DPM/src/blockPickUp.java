import lejos.nxt.*;
public class blockPickUp<UltrasonicController> {
        public static final int ROTATE_SPEED = 150;
        public static final int FORWARD_SPEED = 200;
        public static final double forwardDistance = 5.0;
        public final double leftRadius = 2.1;
        public final double rightRadius = 2.15;
        public final double width = 10.0;
        private NXTRegulatedMotor claw;
        private UltrasonicSensor sensor;
        private NXTRegulatedMotor leftMotor,rightMotor;
        
        private static UltrasonicPoller poller;
        private  UltrasonicController usController;
        
        //initiating objects
        WheelDriver wheels = new WheelDriver( leftMotor, rightMotor) ;
        Odometer odo = new Odometer();
        Navigation nav = new Navigation(leftRadius, rightRadius, odo, wheels);
        ClawDriver theClaw = new ClawDriver(claw);
        
       
       
        public void findRange (NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor, double leftRadius, double rightRadius, double width) {
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
                
                boolean stopSearch = false;
                
                while (stopSearch){
                                
                                //make this loop forever while this function runs
                                poller.getDist();                        
                                
                                //rotate between range of -45 and 45 degrees
                                rotateCCW(45);
                                rotateCCW(-90);
                                if (poller.getDist() > 10) {
                                        rotateCCW(45);
                                        travelForward(forwardDistance);
                                }
                                else {
                                        travelForward(forwardDistance);
                                        //then turn in the direction of the object
                                        //turn around
                                        theClaw.open();
                                        theClaw.close();
                                        stopSearch = true;
                                }                               
                }                    
        }
        
        public void travelForward(double forwardDistance){
                        leftMotor.setSpeed(FORWARD_SPEED);
                        rightMotor.setSpeed(FORWARD_SPEED);
                        leftMotor.rotate(convertDistance(leftRadius, forwardDistance), true);
                        rightMotor.rotate(convertDistance(rightRadius, forwardDistance), false);
        }
        
        //rotate 45 degrees counterclockwise
        public void rotateCCW(double angle) {
            leftMotor.setSpeed(ROTATE_SPEED);
            rightMotor.setSpeed(ROTATE_SPEED);
            leftMotor.rotate(-convertAngle(leftRadius, width, angle), true);
            rightMotor.rotate(convertAngle(rightRadius, width, angle), false);
            
        }
       
       
        private static int convertDistance(double radius, double distance) {
                return (int) ((180.0 * distance) / (Math.PI * radius));
        }
 
        private static int convertAngle(double radius, double width, double angle) {
                return convertDistance(radius, Math.PI * width * angle / 360.0);
        }
       
       
       
       
       
       
       
       
       
       
       
       
}