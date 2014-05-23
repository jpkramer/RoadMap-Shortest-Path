import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import net.datastructures.AdjacencyMapGraph;
import net.datastructures.Edge;
import net.datastructures.Vertex;

/**
 * RoadMap.java
 * 
 * reads in file information to create a RoadMap and its associated Predecessor Map
 * performs Dijkstra's Algorithm to do so
 * 
 * @author JonathanKramer
 *
 */

public class RoadMap {
	private AdjacencyMapGraph<City, Highway> roadMap = new AdjacencyMapGraph<>(false); 
	private HashMap<Vertex<City>, Vertex<City>> shortPathPred = new HashMap<>();
	
	/**
	 * Constructor
	 * calls private helper methods that process file info
	 * @param cityFile
	 * @param linkFile
	 */
	public RoadMap(String cityFile, String linkFile)  {
		HashMap<String, Vertex<City>> cityMap;
		try {
			cityMap = readCityFile(cityFile);
			readLinkFile(linkFile, cityMap);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * reads the city file
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private HashMap<String, Vertex<City>> readCityFile(String file) throws IOException, FileNotFoundException{
		BufferedReader cityFile =  new BufferedReader(new FileReader(file));
		HashMap<String, Vertex<City>> cityMap = new HashMap<>();
		String newLine = cityFile.readLine(); 
		String comma = ",";
		
		while (newLine != null) {
			Point cityLocation = null;
			String [] cities = newLine.split(comma);
			String cityName = cities[0];
			try {
					cityLocation = new Point(Integer.parseInt(cities[1]), Integer.parseInt(cities[2]));
			} catch (IllegalArgumentException e) {}
		
			City newCity = new City(cityName, cityLocation);
			
			cityMap.put(cityName, roadMap.insertVertex(newCity));
			
			newLine = cityFile.readLine();
		}
		
		cityFile.close();
		return cityMap;
		
	}
	
	/**
	 * reads the link file
	 * 
	 * @param file
	 * @param cityMap
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void readLinkFile(String file, HashMap<String, Vertex<City>> cityMap) throws IOException, FileNotFoundException{
		BufferedReader linkFile =  new BufferedReader(new FileReader(file));
		String newLine = linkFile.readLine();
		String comma = ",";
		
		while (newLine != null) {
			int distance = 0;
			int time = 0;
			String [] links = newLine.split(comma);
			String city1 = links[0];
			String city2 = links[1];
			try {
				distance = Integer.parseInt(links[2]);
				time = (Integer.parseInt(links[3]) * 60) + Integer.parseInt(links[4]);
			} catch (NumberFormatException e) {}
			
			Highway newHighway = new Highway(distance, time);
			
			roadMap.insertEdge(cityMap.get(city1), cityMap.get(city2), newHighway);
	
			newLine = linkFile.readLine();
		}
		
		linkFile.close();
		
	}
	
	/**
	 * creates an iterator for the vertices of a road map
	 * 
	 * @param roadmap
	 * @return
	 */
	private Iterable<Vertex<City>> getVertices(AdjacencyMapGraph<City, Highway> roadmap) {
		return roadmap.vertices();
	}
	
	/**
	 * returns a city if there is one located in a certain tolerance of a given point
	 * uses isNear() method of City object
	 * 
	 * @param p
	 * @return
	 */
	public Vertex<City> cityAt(Point p){
		
		Iterable<Vertex<City>> iter = getVertices(roadMap);
		Vertex<City> newCity;
		
		for (Vertex<City> vertices :iter) {
			newCity = vertices;
			
			if (newCity.getElement().isNear(p))
				return newCity;
			
		}
		
		return null;
		
	}
	
	/**
	 * uses Dijkstra's Algorithm to create a Map of shortest path predecessors based on a certain weight
	 * 
	 * @param s
	 * @param weightType
	 */
	public void dijkstra(Vertex<City> s, String weightType){
		HashMap<Vertex<City>, Double> shortPathWeight = new HashMap<>();
		Comparator<Vertex<City>> comparator = new QueueComparator(shortPathWeight);
		PriorityQueue<Vertex<City>> queue = new PriorityQueue<>(100, comparator);
		
		for (Vertex<City> cityVertex : roadMap.vertices()) {
		  shortPathWeight.put(cityVertex, Double.POSITIVE_INFINITY);
		  queue.add(cityVertex);
		  shortPathPred.put(cityVertex, null);
		  //System.out.println(cityVertex.getElement().getName() + "");
		}   
		
		shortPathWeight.put(s, 0.0);

		while (!queue.isEmpty()) {
		  Vertex<City> u = queue.poll();
		  for (Edge<Highway> e : roadMap.outgoingEdges(u)) {
		  	Vertex<City> v = roadMap.opposite(u, e);
				double edgeWeight;
				
				if (weightType.equals("distance"))
			  	edgeWeight = roadMap.getEdge(u, v).getElement().getDistance();
				else
					edgeWeight = roadMap.getEdge(u, v).getElement().getTravelTime();
			  	
				if ((shortPathWeight.get(u) + edgeWeight) < shortPathWeight.get(v)) {
					shortPathWeight.put(v, (shortPathWeight.get(u) + edgeWeight));
			  	shortPathPred.put(v, u);
			  	queue.add(v);
				}
		  } 
		}

	}
	
	/**
	 * getter method for the Map of shortest path predecessors
	 * 
	 * @return
	 */
	public HashMap<Vertex<City>, Vertex<City>> getPredMap() {
		return shortPathPred;
	}
	
}
