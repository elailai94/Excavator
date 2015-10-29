//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with ArmSprite objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// ArmSprite module (ArmSprite.java)

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import java.util.Vector;

public class ArmSprite extends Sprite {

   private RoundRectangle2D arm        = null;
   private Point2D          pivot      = null;
   private double           startAngle = 0;
   private double           currLength = 0;
   private double           maxLength  = 0;

   // Creates the excavator arm with the specified width, height, and
   // x and y coordinates of the pivot
   public ArmSprite(int width, int height, int pivotX, int pivotY) {
      super();
      initialize(width, height, pivotX, pivotY);
   } // Constructor

   // Initializes the excavator arm
   private void initialize(int width, int height, int pivotX, int pivotY) {
      arm = new RoundRectangle2D.Double(0, 0, width, height, height, height);
      pivot = new Point2D.Double(pivotX, pivotY);
      currLength = width;
      maxLength = (2 * width);
   } // initialize

   // Tests if the excavator arm contains the point
   public boolean pointInside(Point2D point) {
      Point2D newPoint = getLocalPoint(point);
      return arm.contains(newPoint);
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
         (newStartAngle  <= Math.toRadians(80))) {
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
         return (Math.toRadians(80) - startAngle);
      } // if
   } // getAngleDiffLimit

   // Calculates the horizontal scale factor between point1 and point2
   private double calculateHorizontalScaleFactor(Point2D point1, Point2D point2) {
      Point2D newPoint1 = getLocalPoint(point1);
      Point2D newPoint2 = getLocalPoint(point2);
      return newPoint2.getX() / newPoint1.getX();
   } // calculateHorizontalScaleFactor

   // Checks if the length after the scaling is still within the
   // scaling range
   private boolean isWithinScalingRange(double scaleFactor) {
      double newLength = (currLength * scaleFactor);
      if ((newLength >= arm.getWidth()) &&
         (newLength <= maxLength)) {
         return true;
      } else {
         return false;
      } // if
   } // isWithinScalingRange

   // Calculates the difference between the x-coordinates of
   // two points
   private double getXDiff(Point2D point1, Point2D point2) {
      Point2D newPoint1 = getLocalPoint(point1);
      Point2D newPoint2 = getLocalPoint(point2);
      return (newPoint2.getX() - newPoint1.getX());
   } // getXDiff

   // Handles a mouse down event, assuming that the event has already
   // been tested to ensure the mouse point is within our sprite
   protected void handleMouseDownEvent(MouseEvent event) {
      lastPoint = event.getPoint();
      if (event.getButton() == MouseEvent.BUTTON1) {
         interactionMode = InteractionMode.ROTATING;
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
            double horizontalScaleFactor =
               calculateHorizontalScaleFactor(oldPoint, newPoint);
            double newLength = currLength * horizontalScaleFactor;

            if (isWithinScalingRange(horizontalScaleFactor)) {
               currLength = newLength;
               transform.scale(horizontalScaleFactor, 1);

               AffineTransform inverseTransform = null;
            
               try {
                  inverseTransform = AffineTransform.getScaleInstance(horizontalScaleFactor, 1).createInverse();
               } catch (NoninvertibleTransformException e) {
                  e.printStackTrace();
               }

               for (Sprite sprite : getChildren()) {
                  sprite.preConcatenate(inverseTransform);

                  //sprite.transform(AffineTransform.getTranslateInstance(getXDiff(oldPoint, newPoint),1));
               } // for
            } // if
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
   	graphics.draw(arm);
   	graphics.setColor(Color.ORANGE);
   	graphics.fill(arm);
   } // drawSprite

}
