//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with BucketSprite objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// BucketSprite module (BucketSprite.java)

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class BucketSprite extends Sprite {

   private Polygon bucket     = null;
   private Point2D pivot      = null;
   private double  startAngle = 0;

   // Creates the excavator bucket based at the origin
   public BucketSprite(int pivotX, int pivotY) {
      super();
      initialize(pivotX, pivotY);
   } // Constructor

   // Initializes the excavator bucket
   private void initialize(int pivotX, int pivotY) {
      int[][] points =
         {{0, 0}, {-10, -40}, {50, -40}, {55, -50}, {60, -40}, {50, 0}};
   	int[] xPoints = new int[points.length];
   	int[] yPoints = new int[points.length];
   	  
   	for (int i = 0; i < points.length; ++i) {
   	  	xPoints[i] = points[i][0];
   	  	yPoints[i] = points[i][1];
   	} // for
      
      bucket = new Polygon(xPoints, yPoints, points.length);
      pivot = new Point2D.Double(pivotX, pivotY);
   } // initialize

   // Tests if the excavator bucket contains the point
   public boolean pointInside(Point2D point) {
      Point2D newPoint = getLocalPoint(point);
      return bucket.contains(newPoint);
   } // pointInside

   // Calculates the clockwise angle between the point and
   // the positive x-axis
   private double calculateAngle(Point2D point) {
      Point2D newPoint = getLocalPoint(point);
      double yDiff = newPoint.getY() - pivot.getY();
      double xDiff = newPoint.getX() - pivot.getX();
      double angle = Math.atan2(yDiff, xDiff);
      return angle;
   } //  calculateAngle

   // Checks if the angle after the rotation is still within the
   // rotation range
   private boolean isWithinRotationRange(double angleDiff) {
      double newStartAngle = (startAngle + angleDiff);
      if ((newStartAngle >= Math.toRadians(-50)) && 
         (newStartAngle  <= Math.toRadians(100))) {
         return true;
      } else {
         return false;
      } // if
   } // isWithinRotationRange

   // Calculates the angle that we can rotate by, assuming that we have
   // tested to ensure that the rotation is outside of the rotation range
   private double getAngleDiffLimit(double angleDiff) {
      double newStartAngle = (startAngle + angleDiff);
      if (newStartAngle < Math.toRadians(-50)) {
         return (Math.toRadians(-50) - startAngle);
      } else {
         return (Math.toRadians(100) - startAngle);
      } // if
   } // getAngleDiffLimit

   // Handles a mouse down event, assuming that the event has already
   // been tested to ensure the mouse point is within our sprite
   protected void handleMouseDownEvent(MouseEvent event) {
      lastPoint = event.getPoint();
      if (event.getButton() == MouseEvent.BUTTON1) {
         interactionMode = InteractionMode.ROTATING;
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
            ;
            break;
         case ROTATING:
            double oldPointAngle = calculateAngle(oldPoint);
            double newPointAngle = calculateAngle(newPoint);
            double angleDiff     = (newPointAngle - oldPointAngle);
            
            if (!isWithinRotationRange(angleDiff)) {
               angleDiff = getAngleDiffLimit(angleDiff);
            } // if
            
            startAngle += angleDiff;
            transform.rotate(angleDiff, pivot.getX(), pivot.getY());
            break;
         case SCALING:
            ;
            break;
      } // switch

      // Saves our last point, if it's needed next time around
      lastPoint = event.getPoint();
   } // handleMouseDragEvent

   protected void handleMouseUpEvent(MouseEvent event) {
       interactionMode = InteractionMode.IDLE;
    } // handleMouseUpEvent

   // Draws the excavator bucket on the screen
   protected void drawSprite(Graphics2D graphics) {
   	graphics.setColor(Color.BLACK);
   	graphics.setStroke(new BasicStroke(2));
   	graphics.drawPolygon(bucket);
   	graphics.setColor(Color.ORANGE);
   	graphics.fillPolygon(bucket);
   } // drawSprite

}
