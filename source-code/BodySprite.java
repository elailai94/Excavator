//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with BodySprite objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// BodySprite module (BodySprite.java)

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class BodySprite extends Sprite {

   private Polygon     door    = null;
   private Rectangle2D slewing = null;
   private Polygon     body    = null;
   
   // Creates the excavator body based at the origin
   public BodySprite() {
      super();
      initializeDoor();
      initializeSlewing();
      initializeBody();
   } // Constructor

   // Initializes the excavator door
   private void initializeDoor() {
      int[][] points =
         {{10, -10}, {10, -30}, {45, -90}, {80, -90}, {80, -10}};
      int[] xPoints = new int[points.length];
   	  int[] yPoints = new int[points.length];
   	  
   	  for (int i = 0; i < points.length; ++i) {
   	  	 xPoints[i] = points[i][0];
   	  	 yPoints[i] = points[i][1];
   	  } // for
      
      door = new Polygon(xPoints, yPoints, points.length);
   } // initializeDoor

   // Initializes the excavator slewing
   private void initializeSlewing() {
      slewing = new Rectangle2D.Double(25, 1, 150, 10);
   } // initializeSlewing

   // Initializes the excavator body
   private void initializeBody() {
   	  int[][] points =
   	     {{0, 0}, {0, -30}, {40, -100}, {90, -100}, {90, -55}, {200, -55}, {200, 0}};
   	  int[] xPoints = new int[points.length];
   	  int[] yPoints = new int[points.length];
   	  
   	  for (int i = 0; i < points.length; ++i) {
   	  	 xPoints[i] = points[i][0];
   	  	 yPoints[i] = points[i][1];
   	  } // for
      
      body = new Polygon(xPoints, yPoints, points.length);
   } // initializeBody

   // Tests if the excavator body contains the point
   public boolean pointInside(Point2D point) {
      Point2D newPoint = getLocalPoint(point);
      return door.contains(newPoint) || 
             slewing.contains(newPoint) || 
             body.contains(newPoint);
   } // pointInside

   // Calculates the difference between the x-coordinates of
   // two points
   private double getXDiff(Point2D point1, Point2D point2) {
      Point2D newPoint1 = getLocalPoint(point1);
      Point2D newPoint2 = getLocalPoint(point2);
      return (newPoint2.getX() - newPoint1.getX());
   } // getXDiff

   // Calculates the difference between the y-coordinates of
   // two points
   private double getYDiff(Point2D point1, Point2D point2) {
      Point2D newPoint1 = getLocalPoint(point1);
      Point2D newPoint2 = getLocalPoint(point2);
      return (newPoint2.getY() - newPoint1.getY());
   } // getYDiff

   // Calculates the vertical scale factor between point1 and point2
   private double calculateVerticalScaleFactor(Point2D point1, Point2D point2) {
      Point2D newPoint1 = getLocalPoint(point1);
      Point2D newPoint2 = getLocalPoint(point2);
      return newPoint2.getY() / newPoint1.getY();
   } // calculateHorizontalScaleFactor

   // Handles a mouse down event, assuming that the event has already
   // been tested to ensure the mouse point is within our sprite
   protected void handleMouseDownEvent(MouseEvent event) {
      lastPoint = event.getPoint();
      if (event.getButton() == MouseEvent.BUTTON1) {
         interactionMode = InteractionMode.DRAGGING;
      } else if (event.getButton() == MouseEvent.BUTTON3) {
      	 interactionMode = InteractionMode.SCALING;
      } // if
   } // handleMouseDownEvent

   // Handle mouse drag event, with the assumption that we have already
   // been "selected" as the sprite to interact with.
   protected void handleMouseDragEvent(MouseEvent event) {
      Point2D oldPoint = lastPoint;
      Point2D newPoint = event.getPoint();
        
      switch (interactionMode) {
         case IDLE:
            ;
            break;
         case DRAGGING:
            double xDiff = getXDiff(oldPoint, newPoint);
            double yDiff = getYDiff(oldPoint, newPoint);
            transform.translate(xDiff, yDiff);
            break;
         case ROTATING:
            ; 
            break;
         case SCALING:
            double verticalScaleFactor = calculateVerticalScaleFactor(oldPoint, newPoint);
            transform.scale(verticalScaleFactor, verticalScaleFactor);
            break;
      } // switch

      // Saves our last point, if it's needed next time around
      lastPoint = event.getPoint();
   } // handleMouseDragEvent

   protected void handleMouseUpEvent(MouseEvent event) {
       interactionMode = InteractionMode.IDLE;
    } // handleMouseUpEvent

   // Draws the excavator body on the screen
   protected void drawSprite(Graphics2D graphics) {
   	  graphics.setColor(Color.BLACK);
   	  graphics.setStroke(new BasicStroke(2));
   	  graphics.drawPolygon(body);
      graphics.setColor(Color.ORANGE);
      graphics.fillPolygon(body);

      graphics.setColor(Color.BLACK);
      graphics.setStroke(new BasicStroke(2));
      graphics.drawPolygon(door);
      graphics.setColor(Color.WHITE);
      graphics.fillPolygon(door);

      graphics.setColor(Color.BLACK);
      graphics.setStroke(new BasicStroke(2));
      graphics.draw(slewing);
   	  graphics.setColor(Color.ORANGE);
   	  graphics.fill(slewing);
   } // drawSprite

}
