
/**The controller class that takes data from the poller, and filters the data.
 * 
 * @author Daniel
 *
 */
public class LightSensorController {
	private LightSensorPoller poller;
	private int value;
	
	public LightSensorController(LightSensorPoller p){
		this.poller = p;
	}
	
	/**
	 * 
	 * @return the filtered distance from the ultrasonic sensor
	 */
	public int getFilteredDist(){
		int unfilteredVal = poller.getLight();
		
		//Filter Code Here
		
		return value;
	}
}