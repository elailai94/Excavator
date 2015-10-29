//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with BoomSprite objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// BoomSprite module (BoomSprite.java)

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.lang.Math;

public class BoomSprite extends Sprite {

   private RoundRectangle2D boom       = null;
   private Point2D          pivot      = null;
   private double           startAngle = 0;

   // Creates the excavator boom with the specified width, height, and
   // x and y coordinates of the pivot
   public BoomSprite(int width, int height, int pivotX, int pivotY) {
      super();
      initialize(width, height, pivotX, pivotY);
   } // Constructor

   // Initializes the excavator boom
   private void initialize(int width, int height, int pivotX, int pivotY) {
      boom = new RoundRectangle2D.Double(0, 0, width, height, height, height);
      pivot = new Point2D.Double(pivotX, pivotY);
   } // initialize

   // Tests if the excavator boom contains the point
   public boolean pointInside(Point2D point) {
      Point2D newPoint = getLocalPoint(point);
      return boom.contains(newPoint);
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
      if ((newStartAngle >= Math.toRadians(-35)) && 
         (newStartAngle  <= Math.toRadians(45))) {
         return true;
      } else {
         return false;
      } // if
   } // isWithinRotationRange

   // Calculates the angle that we can rotate by, assuming that we have
   // tested to ensure that the rotation is outside of the rotation range
   private double getAngleDiffLimit(double angleDiff) {
      double newStartAngle = (startAngle + angleDiff);
      if (newStartAngle < Math.toRadians(-35)) {
         return (Math.toRadians(-35) - startAngle);
      } else {
         return (Math.toRadians(45) - startAngle);
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

   // Handles mouse drag event, with the assumption that we have already
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

   // Draws the excavator arm on the screen
   protected void drawSprite(Graphics2D graphics) {
   	graphics.setColor(Color.BLACK);
   	graphics.setStroke(new BasicStroke(2));
   	graphics.draw(boom);
   	graphics.setColor(Color.ORANGE);
   	graphics.fill(boom);
   } // drawSprite

}
