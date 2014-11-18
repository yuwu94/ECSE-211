import lejos.nxt.LCD;
import lejos.nxt.Sound;
public class OdometryCorrection extends Thread{
	private Odometer odo;
	
	private LightSensorController right,left;
	
	private static final double WIDTH = 8.6;
	
	private static final int LINE = 490;
	
	private static final int PERIOD = 534;
	
	private double xLast,yLast;
	
	private double x,y;
	
	
	
	public OdometryCorrection(Odometer odo, LightSensorController right, LightSensorController left){
		this.odo = odo;
		this.right = right;
		this.left = left;
	}
	
	public double calculate(boolean right){
		double xDist = x - xLast;
		double yDist = y - yLast;
		double result;
		
		double position = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
		
		if(right){
			result = Math.PI/2 + Math.atan2(position, WIDTH);
		}
		else{
			result = Math.PI/2 - Math.atan2(position, WIDTH);
		}
		
		 return result;
		 
	}
	
	public double getNewTheta(double theta){
		double oldTheta = Math.toDegrees(odo.getTheta());
		double newTheta = 0;
		
		if (oldTheta >= 45 || oldTheta <= 135){
			newTheta = theta;
		} else if (oldTheta >= 315 && oldTheta <= 45){
			newTheta = Math.PI/2 + theta;
		} else if (oldTheta >= 225 || oldTheta <= 315){
			newTheta = Math.PI + theta;
		} else if (oldTheta >= 135 && oldTheta <= 225){
			newTheta = Math.PI*(3/2) + theta;
		}
		return newTheta;
	}
	
	public void run(){
		double newTheta = 0;
		int rightValue = right.getFilteredVal();
		int leftValue = left.getFilteredVal();
		
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
						
						newTheta = calculate(true);
												
						try {
							Thread.sleep(20);
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
						
						newTheta = calculate(false);
						
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
				}		
			}
			while(rightValue < LINE && leftValue <LINE){
				odo.setTheta(getNewTheta(newTheta));
			}
		}
	}
	
}