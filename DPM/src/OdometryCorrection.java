import lejos.nxt.LCD;
import lejos.nxt.Sound;


public class OdometryCorrection extends Thread{
	private Odometer odo;
	
	private LightSensorController right,left;
	
	private static final int WIDTH = 3;
	
	private static final int LINE = 490;
	
	private static final int PERIOD = 500;
	
	private double xLast,yLast;
	
	private double x,y;
	
	
	
	public OdometryCorrection(Odometer odo, LightSensorController right, LightSensorController left){
		this.odo = odo;
		this.right = right;
		this.left = left;
	}
	
	public double calculate(boolean right){
		double xDist = xLast - x;
		double yDist = yLast - y;
		double result;
		
		double position = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		
		if(right){
			result = Math.PI/2 - Math.atan2(position, WIDTH);
		}
		else{
			result = Math.PI/2 + Math.atan2(position, WIDTH);
		}
		
		 return result;
		 
	}
	
	public double getNewTheta(double theta){
		double oldTheta = Math.toDegrees(odo.getTheta());
		
		//If facing north (between 315 and 45 degrees)
		if(oldTheta >= 315 || oldTheta < 45){
			return theta;
		}
		//Facing east (between 135 and 45)
		else if(oldTheta < 135 && oldTheta >= 45){
			return Math.PI/2 + theta;
		}
		//Facing south (between 135 and 225)
		else if(oldTheta < 225 && oldTheta >= 135 ){
			return Math.PI + theta;
		}
		//facing west
		else{
			return Math.PI*(3/2) + theta;
		}
		
	}
	
	public void run(){
		while(true){
			//Right sensor passes first
			LCD.drawInt(right.getFilteredVal(), 0, 5);
			if(right.getFilteredVal() < LINE){
				Sound.beep();
				yLast = odo.getY();
				xLast = odo.getX();
				
				//Wait for left Sensor
				while(true){
					if(left.getFilteredVal() < LINE){
						y = odo.getY();
						x = odo.getX();
						
						double newTheta = calculate(true);
						
						odo.setTheta(getNewTheta(newTheta));
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
				}		
			}
			//Left Sensor passes first
			if(left.getFilteredVal() < LINE){
				yLast = odo.getY();
				xLast = odo.getX();
				
				//Wait for right Sensor
				while(true){
					if(right.getFilteredVal() < LINE){
						y = odo.getY();
						x = odo.getX();
						
						double newTheta = calculate(false);
						
						odo.setTheta(getNewTheta(newTheta));
						
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
				}		
			}
		}
	}
	
}