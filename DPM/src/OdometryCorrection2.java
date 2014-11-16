 import lejos.nxt.Sound;
        import lejos.util.*;
         
         
        public class OdometryCorrection extends Thread {
                //set default period of timer
                private final int DEFAULT_PERIOD = 30;
                //set navigation
                private Navigation nav;
                //Class variables
                //set tileline threshold
                private final double tileline = 990;
                //set light value array
                private double[] light = new double [2];
                //set boolean array
                private boolean[] line = new boolean [2];
                //set initialize check
                private boolean check;
                //set x tile index and y tile index
                private int xNumber,yNumber;
         
               
            //set up position array
                private double [] pos = new double [3];
               
                //set up sensors
                private LightPollerCorrect lPoll;
               
                //set up odometer
                private Odometer odo;
               
                //constructor
                public OdometryCorrection (Odometer o, LightPollerCorrect l, Navigation n) {      
                       
                        //set up navigation
                        nav = n;
                               
                        //set odometer
                        odo = o;
                        //set lightPoller
                        lPoll = l;
                       
                        //initialize correction as off
                        check = false;
                       
                        //set initial tile lines at 0,0
                        xNumber = 0;
                        yNumber = 0;
                }
               
               
                //method runs constantly
                public void run() {
       
                        //if correction is enabled
                    if (check) {
                        odo.getPosition(pos);
                       
                        //check to see if line has been detected
                   
                                        line = lineDetected();
       
                        //before line[0]&&line[1]
                      if ((line[0])&&(line[1])) {
                         
                          //sets correction path based on heading
                          //four cases, spanning the positive and negative x and y axis
                         
                         
                              if(((pos[2] > 315)&&(pos[2] <= 360))||((pos[2] >= 0)&&(pos[2] <= 45))) {
                                  try {
                                                yCorrection(true);
                                        } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                                   }
                                else if ((pos[2] <= (270-45))&&(pos[2]> (90 + 45)))
                                        {try {
                                                yCorrection(false);
                                        } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                                        }
                                else if ((pos[2] > (45))&&(pos[2] <= (90 + 45)))
                                        {try {
                                                xCorrection(true);
                                        } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                                        }
                                else if ((pos[2] > (270-45))&&(pos[2]<= (360-45)))
                                        try {
                                                xCorrection(false);
                                        } catch (InterruptedException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                        }
                      }
                         
                    }  
                       
                }//end timed out
               
                //allows for a turn with correction temporarily turned off
                public void protectedTurn(double angle){
                        check = false;
                        nav.turnTo(angle);    
                        check = true;  
                }
               
                //allows for a travel with correction temporarily turned off
                public void protectedTravel(double x, double y){
                        check = false;
                        nav.travelTo(x,y);    
                        check = true;  
                }      
         
               
                //setter for enabling the odometer correction
                public void correctionOn(boolean condition){
                        check = condition;
                }
               
                //method for moving up or down one tile
            public void vertTile(boolean up){
                  double [] dest = new double [2];
                  dest[0] = (xNumber*30) + 15;
                 
                  if (up){
                  dest[1] = (yNumber + 1)*30 +15;
                  protectedTurn(0);
                  }
                 
                  else{
                      dest[1] = (yNumber - 1)*30 +15;
                      protectedTurn(180);
               }
              nav.travelTo(dest[0],dest[1]);
               
               
            }//end vert tile  
               
                //method for moving right or left one tile
            public void horiTile(boolean right){
                  double [] dest = new double [2];      
                  dest[1] = (yNumber*30) + 15;
                 
                  if (right){
                  dest[0] = (xNumber + 1)*30 +15;
                  protectedTurn(90);
                  }
                 
                  else{
              dest[0] = (xNumber - 1)*30 +15;
              protectedTurn(270);
               }    
                nav.travelTo(dest[0],dest[1]);
             
            }//end hori tile  
       
                //x correction method
                private void xCorrection(boolean positive) throws InterruptedException{
                  odo.getPosition(pos);
                  double newXval = pos[0];
 
                  if (positive){
       
                                Sound.beepSequence();
                                xNumber += 1;
                                newXval = (30*xNumber) + 6.5;  
                                setOdometer(newXval,0,true);  
                  }
                  else {
                                  Sound.beepSequence();
                                  newXval = (30*xNumber) - 6.5;          
                                   xNumber -= 1;      
                                   setOdometer(newXval,0,true);          
                        }
       
                }//end x correction
         
                //y correction method
                private void yCorrection(boolean positive) throws InterruptedException{
                        Sound.beepSequence();
                        double newYval;
                         
                          if (positive){
                                yNumber += 1;
                                newYval = (30*yNumber) + 6.5;
                          }
         
                          else{
                                 
                                   newYval = (30*yNumber) - 6.5;              
                                   yNumber -= 1;
                          }
                               
                        setOdometer(0,newYval,false);          
         
                }//end y correction
               
                //method checks whether one or both light sensors detect a tileline
                public boolean [] lineDetected()
                {
                       
                        //Thread.sleep(600);
                       
                        boolean [] conditions = new boolean[2];
                        light = lPoll.getFilteredData();
               
                        if (light[0] <= tileline){
                                conditions[0] = true;
                                //Sound.beepSequence();
                        }
                        else  
                                conditions[0] = false;
                       
                        if (light[1] <= tileline){
                                conditions[1] = true;
                                //Sound.beepSequence();
                        }
                       
                        else  
                                conditions[1] = false;
               
                        return conditions;
                       
                }
               
                //sets the current position in relation to tiles, aka the tileline multiplier (15x)
                public void setInitialTiles(int x, int y){      
                        xNumber = x;  
                        yNumber = y;  
                }
               
               
                //method sets odometers new position
            private void setOdometer(double x, double y,  boolean isX) throws InterruptedException {
                odo.getPosition(pos);
               if (isX)
                odo.setPosition(new double [] {x,pos[1],pos[2]}, new boolean[]{true,true,true});
               else
                odo.setPosition(new double [] {pos[0],y,pos[2]}, new boolean[]{true,true,true});
               
               Thread.sleep(800);
            }
           
            //method calibrates light sensors
            public void calibrateSensors(){
                lPoll.calibrate();
            }
}