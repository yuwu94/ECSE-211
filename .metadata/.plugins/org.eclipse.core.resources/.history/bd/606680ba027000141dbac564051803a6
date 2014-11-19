package dpm.device.sensor;

import lejos.nxt.LightSensor;
import lejos.nxt.comm.RConsole;
import dpm.device.TwoWheeledRobot;

/**
 * Thread used for the light sensor poller.
 * It request data from the light sensor at a given interval (30 ms)
 * @version 1.0
 */
public class LightSensorPoller extends Thread{
	int sensorValue;
	int PreviousValue;
	int firstOrderDerivative = 0;
	int Previous_firstOrderDerivative = 0;
	int secondOrderDerivative = 0;
	LightSensor sensor;
	int[] sensorValueArray = new int[10];
	public LightSensorPoller(LightSensor sensor){
		this.sensor = sensor;
	}
	
	/**
	 * "Main" method for a Thread.
	 */
	public void run(){
		PreviousValue = sensor.readValue();
		sensorValue = sensor.readValue();
		while(true){
			Previous_firstOrderDerivative = firstOrderDerivative;
			PreviousValue = sensorValue;
			sensorValue = sensor.readValue();
			firstOrderDerivative = sensorValue - PreviousValue;
			secondOrderDerivative = firstOrderDerivative - Previous_firstOrderDerivative;
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method that returns a filtered value of the readings of the sensor.
	 * @return A filtered value.
	 */
	public int getFilteredSensorValue(){
		return sensorValue;
	}
	
	/**
	 * Method that returns the derivative of the current values.
	 * @return The derivative of the current values.
	 */
	public int getFirstOrderDerivative(){
		return firstOrderDerivative;
	}
	public int getSecondOrderDerivative(){
		return secondOrderDerivative;
	}
}

