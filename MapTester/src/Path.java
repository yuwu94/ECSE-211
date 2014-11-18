import java.util.ArrayList;


public class Path {
	private ArrayList<GridSquare> squares;
	private ArrayList<Waypoint> points;
	
	public Path(){
		squares = new ArrayList<GridSquare>();
	}
	
	public void addSquare(GridSquare s){
		squares.add(s);
	}
	
	public ArrayList<Waypoint> getPoints(){
		points = new ArrayList<Waypoint>();
		for(int i = 0; i < squares.size(); i++){
			points.add(squareToPoint(squares.get(i)));
		}
		return points;
	}
	
	private Waypoint squareToPoint(GridSquare s){
		Waypoint p = new Waypoint(15 + s.getX()*30,15 + s.getY()*30);
		return p;
	}
	
}
