import lejos.nxt.*;
		import lejos.util.*;
		import lejos.nxt.comm.*;
		/**
		A simple data acquisition demo using the light sensor. The cart moves
		in a straight line until the requisite number of samples are acquired.
		The Java Rconsole class is used to datalog to a remote listener.
		*/
		public class DataAcquisition implements TimerListener {
		
			/* Class Constants */
			
			public static final int SINTERVAL=150; /* A 10Hz sampling rate */
			public static final int NSAMPLES=100; /* Number of samples to acquire */
			public static final int TIMEOUT=40000; /* Timeout = 40 seconds */
			public static final int FWDSPEED=10; /* Forward speed */
			public static final int SLEEPINT=500; /* Main sleep = 500 mS */
			
			/* Class Variables (persistent) */
			public static int numSamples;
			public static int currentSample;
			
		
			/* Objects instanced once by this class */
			static ColorSensor cs = new ColorSensor(SensorPort.S3);
			
			// static LightSensor myLight = new LightSensor(SensorPort.S2);
			
			
			/* Class entry point (main) starts here */
			public static void main(String[] args) throws InterruptedException {
				boolean rolling = true;
				int status;
				numSamples=0;
				/* Open channel to remote console */
				RConsole.openUSB(TIMEOUT);
				/* Set up display area */
				LCD.clear();
				LCD.drawString("Data Acquisition Demo",0,0,false);
				LCD.drawString("Channel opened...",0,2,false);
				LCD.drawString("# Samples ",0,4,false);
				LCD.drawString("Last Val. ",0,5,false);
				
				
				/* Set up timer interrupts */
				Timer myTimer = new Timer(SINTERVAL,new DataAcquisition());
				
				/* Start the cart rolling forward at nominal speed */
				
				Motor.A.forward(); /* Start motor threads */
				Motor.B.forward();
				Motor.A.setSpeed(FWDSPEED); /* Synchronize rotation */
				Motor.B.setSpeed(FWDSPEED);
				
				/* Enable the exception handler */
				cs.setFloodlight(true);
				cs.setFloodlight(cs.GREEN);	
				
				myTimer.start();
				
				/* Main continuously updates the display and checks for abort */
				while (rolling) {
					status=Button.readButtons();
					if ((status==8) || Button.ESCAPE.isDown()|| (numSamples>=NSAMPLES)){    
						RConsole.println("Closing remote console...\n");
						RConsole.close(); /* Close console */
						NXT.shutDown(); /* Return to monitor */
						Motor.A.stop(); /* Synchronize rotation */
						Motor.B.stop();
					}
					LCD.drawInt(numSamples,4,11,4); /* List current state */
					LCD.drawInt(currentSample,4,11,5);
					Thread.sleep(SLEEPINT); /* Have a short nap */
				}
			}
			
			/*
			* Data acquisition loop is implemented in the timer handler (listener)
			*/
			public void timedOut() {
				currentSample = cs.getNormalizedLightValue() ;
			//	currentSample= myLight.readNormalizedValue();
				numSamples++;
				
				RConsole.println(Integer.toString(currentSample));
			}
		}