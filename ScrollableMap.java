/**

 * ScrollableMap.java

 * Class for a scrollable roadmap that responds to user actions.

 * For CS 10 Lab Assignment 4.

 * 

 * @author Yu-Han Lyu, Tom Cormen, and Jonathan Kramer

 */



import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import net.datastructures.*;



public class ScrollableMap extends JLabel implements Scrollable,

    MouseMotionListener, MouseListener {

  private static final long serialVersionUID = 1L;

  // The first two instance variables are independent of our roadmap application.

  private int maxUnitIncrement = 1;         // increment for scrolling by dragging

  private boolean missingPicture = false;   // do we have an image to display?


  private JLabel infoLabel;                 // where to display the result, in words

  private JButton destButton;              // the destination button, so that it can be enabled

  private RoadMap roadmap;                  // the roadmap

  private Vertex<City> source;
  
  private Vertex<City> destination;
  
  private Point currentPoint;
  
  private boolean findingSource, findingDest;

  String weight;

  /**

   * Constructor.

   * @param i the highway roadmap image

   * @param m increment for scrolling by dragging

   * @param infoLabel where to display the result

   * @param destButton the destination button

   * @param roadmap the RoadMap object, a graph

   */

  public ScrollableMap(ImageIcon i, int m, JLabel infoLabel, JButton destButton, RoadMap roadmap) {

    super(i);
    source = null;
    destination = null;
    currentPoint = new Point(0,0);
    findingDest = false; 
    weight = "distance";
    
    if (i == null) {

      missingPicture = true;

      setText("No picture found.");

      setHorizontalAlignment(CENTER);

      setOpaque(true);

      setBackground(Color.white);

    }

    maxUnitIncrement = m;

    this.infoLabel = infoLabel;

    this.destButton = destButton;
    
    this.roadmap = roadmap;



    // Let the user scroll by dragging to outside the window.

    setAutoscrolls(true);         // enable synthetic drag events

    addMouseMotionListener(this); // handle mouse drags

    addMouseListener(this);

    this.requestFocus();

    

    findSource();     // start off by having the user click a source city

  }



  // Methods required by the MouseMotionListener interface:

  @Override

  public void mouseMoved(MouseEvent e) { }



  @Override

  public void mouseDragged(MouseEvent e) {

    // The user is dragging us, so scroll!

    Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);

    scrollRectToVisible(r);

  }



  // Draws the map and shortest paths, as appropriate.

  // If shortest paths have been computed, draws either the entire shortest-path tree

  // or just a shortest path from the source vertex to the destination vertex.

  @Override

  public void paintComponent(Graphics page) {

    Graphics2D page2D = (Graphics2D) page;

    setRenderingHints(page2D);

    super.paintComponent(page2D);

    Stroke oldStroke = page2D.getStroke();  // save the current stroke

    page2D.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_BUTT,

        BasicStroke.JOIN_MITER));

    
    // If shortest paths have been computed and there is a destination vertex, draw
    // a shortest path from the source vertex to the destination vertex.
    if ((source != null) && (destination != null)) {
    	
    	Vertex<City> current = destination;
    	
    	while(current != source) {
    		Vertex<City> next = roadmap.getPredMap().get(current);
    		int x1 = current.getElement().getLocation().x;
    		int y1 = current.getElement().getLocation().y;
    		int x2 = next.getElement().getLocation().x;
    		int y2 = next.getElement().getLocation().y;
    		page2D.drawLine(x1, y1, x2, y2);
    		current = next;
    		infoLabel.setText("Shortest Path from " + source.getElement().getName()+ " to " + destination.getElement().getName() + " using " + weight);
    		
    	}	
    		
    }
    
    // If shortest paths have been computed and there is not a destination vertex,
    // draw the entire shortest-path tree.
    if ((source != null)  && (destination == null)) {	
    	infoLabel.setText("Shortest Path from " + source.getElement().getName() + " using " + weight);
    	for (Vertex<City> newCity : roadmap.getPredMap().keySet()) {
    		Vertex<City> current = newCity;
    		if (roadmap.getPredMap().get(newCity) != null) {
    			Vertex<City> pred = roadmap.getPredMap().get(newCity);
    			int x1 = current.getElement().getLocation().x;
    			int y1 = current.getElement().getLocation().y;
    			int x2 = pred.getElement().getLocation().x;
    			int y2 = pred.getElement().getLocation().y;
    			page2D.drawLine(x1, y1, x2, y2);
    		}
    	}
    }
    
    // If shortest paths have not been computed, draw nothing.
    //if (roadmap.getPredMap().isEmpty());

    page2D.setStroke(oldStroke);    // restore the saved stroke

  }



  // Enable all rendering hints to enhance the quality.

  public static void setRenderingHints(Graphics2D page) {

    page.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,

        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

    page.setRenderingHint(RenderingHints.KEY_ANTIALIASING,

        RenderingHints.VALUE_ANTIALIAS_ON);

    page.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,

        RenderingHints.VALUE_COLOR_RENDER_QUALITY);

    page.setRenderingHint(RenderingHints.KEY_INTERPOLATION,

        RenderingHints.VALUE_INTERPOLATION_BICUBIC);

  }



  // Methods required by the MouseListener interface.

  

  // When the mouse is clicked, find which vertex it's over.

  // If it's over a vertex and we're finding the source,

  // record the source, clear the destination, enable the destination

  // button, and find and draw the shortest paths from the source.

  // If it's over a vertex and we're finding the destination, record

  // the destination, and find and draw a shortest path from the source

  // to the destination.

  public void mouseClicked(MouseEvent e) {
  
    currentPoint = e.getPoint();
    
    //System.out.print(roadmap.getPredMap() + "");
    
    if (findingSource) {
    	findSource();
    	source = roadmap.cityAt(currentPoint);
    	useDistance();
    	destButton.setEnabled(true);
    }	
    if (findingDest) {
    	findDest();
    	destination = roadmap.cityAt(currentPoint);
    	useDistance();
    }	
    
    findingSource = false;
    findingDest = false;
    
  }



  public void mousePressed(MouseEvent e) { }

  public void mouseReleased(MouseEvent e) { }

  public void mouseEntered(MouseEvent e) { }

  public void mouseExited(MouseEvent e) { }



  // Return the preferred size of this component.

  @Override

  public Dimension getPreferredSize() {

    if (missingPicture)

      return new Dimension(320, 480);

    else

      return super.getPreferredSize();

  }



  // Needs to be here.

  @Override

  public Dimension getPreferredScrollableViewportSize() {

    return getPreferredSize();

  }



  // Needs to be here.

  @Override

  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation,

      int direction) {

    // Get the current position.

    int currentPosition = 0;

    if (orientation == SwingConstants.HORIZONTAL)

      currentPosition = visibleRect.x;

    else

      currentPosition = visibleRect.y;



    // Return the number of pixels between currentPosition

    // and the nearest tick mark in the indicated direction.

    if (direction < 0) {

      int newPosition = currentPosition - (currentPosition / maxUnitIncrement)

          * maxUnitIncrement;

      return (newPosition == 0) ? maxUnitIncrement : newPosition;

    }

    else

      return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement

          - currentPosition;

  }



  // Needs to be here.

  @Override

  public int getScrollableBlockIncrement(Rectangle visibleRect,

      int orientation, int direction) {

    if (orientation == SwingConstants.HORIZONTAL)

      return visibleRect.width - maxUnitIncrement;

    else

      return visibleRect.height - maxUnitIncrement;

  }



  // Needs to be here.

  @Override

  public boolean getScrollableTracksViewportWidth() {

    return false;

  }



  // Needs to be here.

  @Override

  public boolean getScrollableTracksViewportHeight() {

    return false;

  }



  // Needs to be here.

  public void setMaxUnitIncrement(int pixels) {

    maxUnitIncrement = pixels;

  }



  // Called when the source button is pressed.

  public void findSource() {
  		findingSource = true;
  		destination = null;
  }



  // Called when the destination button is pressed.

  public void findDest() {
  		findingDest = true;	
  }



  // Called when the time button is pressed.  Tells the roadmap to use time

  // for edge weights, and finds and draws shortest paths.

  public void useTime() {
  	
  	weight = "time";
    roadmap.dijkstra(source, "time");
    roadmap.getPredMap();
    repaint();

  }



  // Called when the distance button is pressed.  Tells the roadmap to use distance

  // for edge weights, and finds and draws shortest paths.

  public void useDistance() {

  	weight = "distance";
  	roadmap.dijkstra(source, "distance");
    roadmap.getPredMap();
    repaint();

  }

}