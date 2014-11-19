import java.util.ArrayList;

import lejos.nxt.Sound;


public class Localizer extends Thread{
	
	boolean running;
	
	private static final int WALL = 20;
	
	private Odometer odo;
	
	private Navigation nav;
	
	private Map map;
	
	private ArrayList<Ghost> ghosts;
	
	private UltrasonicController controller;
	
	public Localizer(Map map,Odometer odo,Navigation nav,UltrasonicController uc){
		this.map = map;
		running = true;
		this.nav = nav;
		this.controller = uc;
		
		for(int i = 0; i < map.getSize(); i++){
			for(int j = 0; j < map.getSize(); j++){
				if(!map.getSquare(i,j).isWall()){
					Ghost n = new Ghost(i,j,"N",map);
					Ghost s = new Ghost(i,j,"S",map);
					Ghost w = new Ghost(i,j,"W",map);
					Ghost e = new Ghost(i,j,"E",map);
					ghosts.add(n);
					ghosts.add(s);
					ghosts.add(w);
					ghosts.add(e);
				}
			}
		}
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public int numValid(){
		int num = 0;
		for(Ghost g : ghosts){
			if(g.isValid()){
				num++;
			}
		}
		return num;
	}
	
	public void noWall(){
		for(Ghost g: ghosts){
			if(g.wallinFront() != 0){
				g.invalid();
			}
		}
	}
	
	public void wall(){
		for(Ghost g : ghosts){
			if(g.wallinFront() == 0){
				g.invalid();
			}
		}
	}
	
	public void run(){
		while(numValid() > 1){
			if(controller.getFilteredDist() < WALL){
				wall();
				nav.turnCW();
				for(Ghost g : ghosts){
					if(g.isValid()){
						g.turn();
					}
				}
			}
			else{
				noWall();
				nav.oneTileForward();
				for(Ghost g : ghosts){
					if(g.isValid()){
						g.move();
					}
				}
			}
		}
		for(Ghost g : ghosts){
			if(g.isValid()){
				odo.setX(g.getX()*30 + 15);
				odo.setY(g.getY()*30 + 15);
				if(g.getOrientation().equals("N")) {odo.setTheta(0);}
				if(g.getOrientation().equals("S")) {odo.setTheta(Math.PI);}
				if(g.getOrientation().equals("E")) {odo.setTheta(Math.PI/2);}
				if(g.getOrientation().equals("W")) {odo.setTheta(3 * Math.PI/2);}
			}
		}
		
		Sound.beep();
		running = false;
	}
}
