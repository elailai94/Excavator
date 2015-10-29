//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with TreadsSprite objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// TreadsSprite module (TreadsSprite.java)

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class TreadsSprite extends Sprite {

   private RoundRectangle2D treads = null;

   // Creates the excavator treads with the specified width and height
   public TreadsSprite(int width, int height) {
      super();
      initialize(width, height);
   } // Constructor

   // Initializes the excavator treads
   private void initialize(int width, int height) {
      treads = new RoundRectangle2D.Double(0, 0, width, height, height, height);
   } // initialize

   // Tests if the excavator treads contains the point
   public boolean pointInside(Point2D point) {
      Point2D newPoint = getLocalPoint(point);
      return treads.contains(newPoint);
   } // pointInside

   // Handles a mouse down event, assuming that the event has already
   // been tested to ensure the mouse point is within our sprite
   protected void handleMouseDownEvent(MouseEvent event) {
      lastPoint = event.getPoint();
      if (event.getButton() == MouseEvent.BUTTON1) {
         interactionMode = InteractionMode.IDLE;
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
            ;
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

   // Draws the excavator body on the screen
   protected void drawSprite(Graphics2D graphics) {
   	graphics.setColor(Color.BLACK);
   	graphics.setStroke(
         new BasicStroke(4, BasicStroke.CAP_BUTT,
         BasicStroke.JOIN_MITER, 10, (new float[] {20}), 0));
   	graphics.draw(treads);
   	graphics.setColor(Color.LIGHT_GRAY);
   	graphics.fill(treads);
   } // drawSprite

}
