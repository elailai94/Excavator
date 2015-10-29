//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with Sprite objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// Sprite module (Sprite.java)

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Vector;


// A building block for creating your own shapes that can be
// transformed and that can respond to input. This class is
// provided as an example; you will likely need to modify it
// to meet the assignment requirements.
public abstract class Sprite {
    
    // Tracks our current interaction mode after a mouse-down
    protected enum InteractionMode {
        IDLE,
        DRAGGING,
        SCALING,
        ROTATING
    }

    private Vector<Sprite> children = new Vector<Sprite>();           // Holds all of our children
    private Sprite parent = null;                                     // Pointer to our parent
    
    protected AffineTransform transform = new AffineTransform();      // Our transformation matrix
    protected Point2D lastPoint = null;                               // Last mouse point
    protected InteractionMode interactionMode = InteractionMode.IDLE; // Current interaction mode
    
    public Sprite() {
       ; // No operation
    } // Constructor
    
    public Sprite(Sprite parent) {
       if (parent != null) {
          parent.addChild(this);
       } // if
    } // Constructor

    public void addChild(Sprite sprite) {
       children.add(sprite);
       sprite.setParent(this);
    } // addChild

    protected Vector<Sprite> getChildren() {
       return children;
    } // getChildren

    public Sprite getParent() {
       return parent;
    } // getParent

    private void setParent(Sprite sprite) {
       this.parent = sprite;
    } // setParent

    // Test whether a point, in world coordinates, is within our sprite
    public abstract boolean pointInside(Point2D point);

    // Handles a mouse down event, assuming that the event has already
    // been tested to ensure the mouse point is within our sprite
    protected abstract void handleMouseDownEvent(MouseEvent event);

    // Handle mouse drag event, with the assumption that we have already
    // been "selected" as the sprite to interact with.
    // This is a very simple method that only works because we
    // assume that the coordinate system has not been modified
    // by scales or rotations. You will need to modify this method
    // appropriately so it can handle arbitrary transformations.
    protected abstract void handleMouseDragEvent(MouseEvent event);
    
    // Handles mouse up event.
    protected abstract void handleMouseUpEvent(MouseEvent event);
    
    protected void handleMouseScrollEvent(MouseEvent event) {
       ;
    } // handleMouseScrollEvent

    // Locates the sprite that was hit by the given event.
    // You *may* need to modify this method, depending on
    // how you modify other parts of the class. 
    // @return The sprite that was hit, or null if no sprite was hit
    public Sprite getSpriteHit(MouseEvent event) {
       for (Sprite sprite : children) {
           Sprite s = sprite.getSpriteHit(event);
           if (s != null) {
              return s;
           } // if
       } // for
       if (this.pointInside(event.getPoint())) {
            return this;
       } // if
       return null;
    } // getSpriteHit
    
    // Returns the full transform to this object from the root
    public AffineTransform getFullTransform() {
       AffineTransform returnTransform = new AffineTransform();
       Sprite curSprite = this;
       while (curSprite != null) {
          returnTransform.preConcatenate(curSprite.getLocalTransform());
          curSprite = curSprite.getParent();
       } // while
       return returnTransform;
    } // getFullTransform

    // Returns our local transform
    public AffineTransform getLocalTransform() {
       return (AffineTransform) transform.clone();
    } // getLocalTransform
    
    // Performs an arbitrary transform on this sprite
    public void transform(AffineTransform t) {
       transform.concatenate(t);
    } // transform

    public void preConcatenate(AffineTransform t) {
       transform.preConcatenate(t);
    } // preConcatenate

    protected Point2D getLocalPoint(Point2D point) {
       AffineTransform fullTransform = getFullTransform();
       AffineTransform inverseTransform = null;

       try {
          inverseTransform = fullTransform.createInverse();
       } catch (NoninvertibleTransformException e) {
          e.printStackTrace();
       }

       Point2D newPoint = new Point2D.Double(point.getX(), point.getY());
       inverseTransform.transform(newPoint, newPoint);
       return newPoint;
    } // getLocalPoint

    // Draws the sprite. This method will call drawSprite after
    // the transform has been set up for this sprite.
    public void draw(Graphics2D graphics) {
       // Draws children
       for (Sprite sprite : children) {
          sprite.draw(graphics);
       } // for

       AffineTransform oldTransform = graphics.getTransform();
       AffineTransform fullTransform = this.getFullTransform();
       
       // Sets to our transform
       graphics.transform(fullTransform);
        
       // Draws the sprite (delegated to sub-classes)
       this.drawSprite(graphics);
       
       // Restores original transform
       graphics.setTransform(oldTransform);
    } // draw
    
    // The method that actually does the sprite drawing. This method
    // is called after the transform has been set up in the draw() method.
    // Sub-classes should override this method to perform the drawing.
    protected abstract void drawSprite(Graphics2D graphics);

}
