//Daniel Burke 260507112
//Mariane Kim 260498054
//Group 12
//TAKEN FROM LAB 2
import lejos.nxt.Motor;

/*
 * Odometer.java
 */
/**Odometer Class gets the tachometer readings from the motors, and calculates the relative position of the robot.
 * 
 * @author Daniel
 *
 */
public class Odometer extends Thread {
	// robot position
	private double x, y, theta;

	// odometer update period, in ms
	private static final long ODOMETER_PERIOD = 25;

	// lock object for mutual exclusion
	private Object lock;
	
	private int lastTachoLeft,  lastTachoRight;

	// default constructor
	/**Initializes an Odometer with starting position at [0,0], heading 0
	 * 
	 */
	public Odometer() {
		lastTachoLeft = Motor.A.getTachoCount();
		lastTachoRight = Motor.B.getTachoCount();
		x = 0.0;
		y = 0.0;
		theta = 0.0;
		lock = new Object();
	}

	// run method (required for Thread)
	public void run() {
		long updateStart, updateEnd;

		while (true) {
			updateStart = System.currentTimeMillis();
			//Get tachometer readings
			int tachoLeft = Motor.A.getTachoCount();
			int tachoRight = Motor.B.getTachoCount();
			
			//calculate wheel distance as per lecture notes
			double distanceLeft = Math.PI * 2 * (tachoLeft - lastTachoLeft)/ 180;
			double distanceRight = Math.PI * 2 * (tachoRight - lastTachoRight)/ 180;
			
			lastTachoLeft = tachoLeft;
			lastTachoRight = tachoRight;
			
			//get delta Distance and Delta theta as per lecture notes
			double delD = 0.5 * (distanceLeft + distanceRight);
			double delT = (distanceLeft - distanceRight)/(15.24);
			
			synchronized (lock) {
				//update the stored x,y, and theta values
				x = x + delD*Math.sin(theta);
				y = y + delD*Math.cos(theta);
				
				theta += delT;
			}

			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < ODOMETER_PERIOD) {
				try {
					Thread.sleep(ODOMETER_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {
					// there is nothing to be done here because it is not
					// expected that the odometer will be interrupted by
					// another thread
				}
			}
		}
	}

	// accessors
	/**Given a double array, and boolean array, will update certain positions of the array
	 * 
	 * @param position	array of positions [x,y,theta]
	 * @param update	array of booleans [x,y,theta]
	 */
	public void getPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				position[0] = x;
			if (update[1])
				position[1] = y;
			if (update[2])
				position[2] = theta;
		}
	}
	
	/**Synchronized x return
	 * 
	 * @return Value of x
	 */
	public double getX() {
		double result;

		synchronized (lock) {
			result = x;
		}

		return result;
	}

	/**Synchronized y return
	 * 
	 * @return Value of y
	 */
	public double getY() {
		double result;

		synchronized (lock) {
			result = y;
		}

		return result;
	}
	
	/**Synchronized theta return
	 * 
	 * @return Value of theta
	 */
	public double getTheta() {
		double result;

		synchronized (lock) {
			result = theta;
		}

		return result;
	}

	// mutators
	/**Changes the position of the odometer, given a double array
	 * 
	 * @param position position to change [x,y,theta]
	 * @param update boolean of positions that should be changed [x,y,theta]
	 */
	public void setPosition(double[] position, boolean[] update) {
		// ensure that the values don't change while the odometer is running
		synchronized (lock) {
			if (update[0])
				x = position[0];
			if (update[1])
				y = position[1];
			if (update[2])
				theta = position[2];
		}
	}
	
	/**Synchronized x mutator
	 * 
	 * @param x 
	 */
	public void setX(double x) {
		synchronized (lock) {
			this.x = x;
		}
	}
	
	/**Synchronized y mutator
	 * 
	 * @param y
	 */
	public void setY(double y) {
		synchronized (lock) {
			this.y = y;
		}
	}
	
	/**Synchronized theta mutator
	 * 
	 * @param theta 
	 */
	public void setTheta(double theta) {
		synchronized (lock) {
			this.theta = theta;
		}
	}
}