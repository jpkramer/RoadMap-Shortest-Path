import java.awt.Point;
/**
 * City.java
 * 
 * acts as the Vertex for the AdjacencyMapGraph
 * 
 * @author JonathanKramer
 *
 */

public class City {

	private String myName;
	private Point myLocation;
	private final static double TOLERANCE = 5;
	
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param location
	 */
	public City(String name, Point location) {
		myName = name;
		myLocation = location;
		
	}
	
	/**
	 * uses a Tolerance of 5 to determine if a point is close enough to a vertex 
	 * to be considered clicked
	 * 
	 * @param p
	 * @return
	 */
	public boolean isNear(Point p){
		
		if(myLocation.distance(p) <= TOLERANCE)
			return true;
		else
			return false;
		
	}
	
	/**
	 * getter method for the City Name
	 * 
	 * @return
	 */
	public String getName(){
		return myName;
	}
	
	/** 
	 * getter method for the City location
	 * 
	 * @return
	 */
	public Point getLocation(){
		return myLocation;
	}
	
}
