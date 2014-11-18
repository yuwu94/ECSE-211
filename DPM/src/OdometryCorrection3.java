package dpm.odometer;
import dpm.device.TwoWheeledRobot;
import dpm.device.sensor.LightSensorPoller;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;

/* 
 * OdometryCorrection.java
 */

/**
 * Class used to correct the values of the odometer based in actual readings from the field.
 * @version 1.0
 */
public class OdometryCorrection extends Thread {
	private static final long CORRECTION_PERIOD = 10;
	
	private Odometer odometer;
	private int blackLine_threshold = -3;
	private TwoWheeledRobot robot;
	private LightSensorPoller LSPoller;

	private static double Court_width =30.48 ;
	private static double distance = 13;
	
	private static boolean correctionNeeded = true;
	/**
	 * Standard constructor.
	 * @param odometer The odometer.
	 * @param robot The instance of the robot class.
	 */
	public OdometryCorrection(Odometer odometer,TwoWheeledRobot robot,LightSensorPoller LSPoller) {
		this.odometer = odometer;
		this.robot = robot;
		this.LSPoller = LSPoller;
		}

	/**
	 * "Main" method for a Thread.
	 */
	public static void TurnOffCorrection(){
		correctionNeeded = false;
	}
	public static void TurnOnCorrection(){
		correctionNeeded = true;
	}
	public void run(){
		long correctionStart, correctionEnd;
		/*
		 * we start the blackLine_count from 0, and increase the counter whenever we detect a blackline.
		 * for each time we increment the counter, we correct the value of its position
		 * */
		
		while (true) {
			correctionStart = System.currentTimeMillis();
			int secondOrderlightSensorDerivative = LSPoller.getSecondOrderDerivative();

			if(secondOrderlightSensorDerivative < blackLine_threshold){
				RConsole.println("correction");
				/*add the correction code here*/

				double[] old_CenterPosition = new double[3];
				odometer.getPosition(old_CenterPosition, new boolean[] { true, true, true });
				double[] LSPosition = new double[3];
				getLSPositionFromCenterPosition(old_CenterPosition, LSPosition);
				
				boolean Position_Corrected = false;

//	firstly find the minimum value of x
				double min_x_difference = Court_width;
				double min_y_difference = Court_width;
				
				double corrected_x=0,corrected_y=0 ;
				
				for(int i =0 ; i< 12; i++){
					double x_difference = Math.abs(i*Court_width - LSPosition[0]);
					if (x_difference < min_x_difference){
						min_x_difference = x_difference;
						corrected_x = i*Court_width;
					}
				}
//then find the minimum value of y
				for(int i = 0; i < 12; i++){
				
					double y_difference = Math.abs(i*Court_width - LSPosition[1]);
					if (y_difference < min_y_difference){
						min_y_difference = y_difference;
						corrected_y = i*Court_width;
					}
				}
//compared if we should correct to a x line or an y line				
				boolean correctXNeeded = false;
				boolean correctYNeeded = false;
			
				/*by looking at which correction is smaller, we determine if we should correct the x or y*/
				if(min_x_difference < min_y_difference){
					correctXNeeded = true;}
				if(min_y_difference < min_x_difference){
					correctYNeeded = true;
				}
				if(min_x_difference<2 && min_y_difference <2) {
					correctXNeeded = true;
					correctYNeeded = true;
				}
				if(min_x_difference >10){correctXNeeded = false;}
				if(min_y_difference >10){correctYNeeded = false;}
				
				if(correctXNeeded){ Sound.beep(); LSPosition[0] = corrected_x;}
				if(correctYNeeded){ Sound.beep(); LSPosition[1] = corrected_y;}
				
				double[] correctedCenterPosition = new double[3];
				getCenterPositionFromLSPosition(LSPosition, correctedCenterPosition);
				
				double x_adjustment = correctedCenterPosition[0]-old_CenterPosition[0];
				double y_adjustment = correctedCenterPosition[1]-old_CenterPosition[1];
				
				double correctedCenterX = odometer.getX() + x_adjustment;
				double correctedCenterY = odometer.getY() + y_adjustment;
				odometer.setPosition(new double[]{correctedCenterX,correctedCenterY,odometer.getTheta()},new boolean[]{true,true,true});
					RConsole.println("The xCorrection needed : " +correctXNeeded);
					RConsole.println("The x adjustment is "+ x_adjustment);
					RConsole.println("The yCorrection needed : " +correctYNeeded);
					RConsole.println("The y adjustment is "+ y_adjustment);


			}
			
			correctionEnd = System.currentTimeMillis();
			
			
			if (correctionEnd - correctionStart < CORRECTION_PERIOD) {
				try {
					Thread.sleep(CORRECTION_PERIOD
							- (correctionEnd - correctionStart));
				} catch (InterruptedException e) {
				}
			}
			
		}
	}
	public static void getLSPositionFromCenterPosition(double[] CenterPosition,double[] LSPosition){
		LSPosition[0] = CenterPosition[0] - distance * Math.cos(Math.toRadians(CenterPosition[2]));
		LSPosition[1] = CenterPosition[1] - distance * Math.sin(Math.toRadians(CenterPosition[2]));
		LSPosition[2] = CenterPosition[2];
	}
	public static void getCenterPositionFromLSPosition(double[] LSPosition,double[] CenterPosition){
		CenterPosition[0] = LSPosition[0] + distance * Math.cos(Math.toRadians(LSPosition[2]));
		CenterPosition[1] = LSPosition[1] + distance * Math.sin(Math.toRadians(LSPosition[2]));
		CenterPosition[2] = LSPosition[2];
	}
}