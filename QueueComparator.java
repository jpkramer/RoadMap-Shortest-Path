import java.util.Comparator;
import java.util.HashMap;
import net.datastructures.Vertex;

/**
 * 
 * QueueComparator.java
 * 
 * used to determine the position of Vertices in a queue
 * specifically used for min priority queue of based on Vertex weight 
 * 
 * @author JonathanKramer
 *
 */

public class QueueComparator implements Comparator<Vertex<City>> {
	HashMap<Vertex<City>, Double> weightMap;
	
	/**
	 * Constructor
	 * 
	 * necessary to include and initialize weight map to compare vertex weight values
	 * @param wMap
	 */
	public QueueComparator(HashMap<Vertex<City>, Double> wMap ) {
		weightMap = wMap;
	}
	
	/**
	 * required compare method that determines position in queue
	 */
	public int compare(Vertex<City> o1, Vertex<City> o2) {
		double w1 = weightMap.get(o1);
		double w2 = weightMap.get(o2);
		
			if (w1 < w2)
				return -1;
			else if (w1 > w2)
				return 1;
			else
				return 0;
	}

}
