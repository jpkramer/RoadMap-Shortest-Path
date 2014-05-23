/**
 * 
 * Highway.java
 * 
 * acts as the Edge for the AdjacencyMapGraph
 * 
 * @author JonathanKramer
 *
 */

public class Highway {
	private double distance;
	private int travelTime;
	
	/**
	 * Constructor
	 * 
	 * @param d
	 * @param t
	 */
	public Highway(int d, int t){
		distance = d;
		travelTime = t;
	}
	
	/**
	 * getter method for distance edge weight
	 * 
	 * @return
	 */
	public double getDistance(){
		return distance;
	}
	
	/**
	 * getter method for travel time edge weight
	 * 
	 * @return
	 */
	public int getTravelTime(){
		return travelTime;
	}
	
	
}
