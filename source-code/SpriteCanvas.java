//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: Module for providing functions to work with SpriteCanvas objects
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

// SpriteCanvas module (SpriteCanvas.java)

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

// A canvas that draws sprites and provides capabilities for recording
// events and playing them back.
public class SpriteCanvas extends JPanel {
    
   // Tracks our current interactive mode
   private enum InteractionMode {
	  IDLE,
	  RECORDING,
	  PLAYBACK
   }

   private Vector<Sprite> sprites = new Vector<Sprite>();          // All sprites we're managing
   private Sprite interactiveSprite = null;                        // Sprite with which user is interacting
   private InteractionMode interactionMode = InteractionMode.IDLE; // Current interactive mode
   private Vector<Serializable> eventStream = null;                // Event stream for recording events
   private long lastTime = -1;                                     // Time of last event

   public SpriteCanvas() {
	  initialize();
   } // Constructor

   private void initialize() {
	  // Install our event handlers
	  this.addMouseListener(new MouseAdapter() {
		 public void mousePressed(MouseEvent e) {
			handleMousePressed(e);
		 }

		 public void mouseReleased(MouseEvent e) {
			handleMouseReleased(e);
		 }
	  });
		
	  this.addMouseMotionListener(new MouseMotionAdapter() {
		 public void mouseDragged(MouseEvent e) {
			handleMouseDragged(e);
		 }
	  });
   } // initialize

   // Handles mouse press events from user or demo file
   private void handleMousePressed(MouseEvent e) {
	  for (Sprite sprite : sprites) {
		 interactiveSprite = sprite.getSpriteHit(e);
		 if (interactiveSprite != null) {
			recordEvent(e);
			interactiveSprite.handleMouseDownEvent(e);
			break;
		 } // if
	  } // for
   } // handleMousePress

   // Handles mouse dragged events from user or demo file
   private void handleMouseDragged(MouseEvent e) {
	  if (interactiveSprite != null) {
		 recordEvent(e);
		 interactiveSprite.handleMouseDragEvent(e);
		 repaint();
	  } // if
   } // handleMouseDragged

   // Handles mouse released events from user or demo file
   private void handleMouseReleased(MouseEvent e) {
	  if (interactiveSprite != null) {
		 recordEvent(e);
		 interactiveSprite.handleMouseUpEvent(e);
		 repaint();
	  } // if
	  interactiveSprite = null;
   } // handleMouseReleased

   // Records an event, but only if recording is turned on
   private void recordEvent(MouseEvent event) {
	  if (interactionMode.equals(InteractionMode.RECORDING)) {
		 long thisTime = System.currentTimeMillis();
		 eventStream.add(thisTime - lastTime);
		 eventStream.add(event);
		 lastTime = thisTime;
	  } // if
   } // recordEvent

   // Adds a top-level sprite to manage
   public void addSprite(Sprite s) {
	  sprites.add(s);
   } // addSprite

   // Removes all sprite we're managing
   public void removeAllSprites() {
      sprites.removeAllElements();
   } // removeAllSprites

   // Paints our canvas
   public void paint(Graphics graphics) {
	  graphics.setColor(Color.WHITE);
	  graphics.fillRect(0, 0, getWidth(), getHeight());
	  graphics.setColor(Color.BLACK);
	  for (Sprite sprite : sprites) {
		 sprite.draw((Graphics2D) graphics);
	  } // for
   } // paint

   // Starts recording of events
   public void startRecording() {
	  interactionMode = InteractionMode.RECORDING;
	  eventStream = new Vector<Serializable>();
	  lastTime = System.currentTimeMillis();
   } // startRecording

   // Stops recording events, writes them to a file called "demo"
   public void stopRecording() {
	  interactionMode = InteractionMode.IDLE;
	  if (eventStream != null) {
		 try {
			ObjectOutputStream out =
			   new ObjectOutputStream(new FileOutputStream("demo"));
		    for (Serializable item : eventStream) {
			   out.writeObject(item);
			} // for
			out.close();
		 } catch (FileNotFoundException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 }
	  } // if
   } // stopRecording

   /**
	* Starts the demo. Important note: This simply plays back events that were
	* previously recorded. Thus, it is assumed that the sprites are in the
	* *exact same state* as when recording started. If not, you will get
	* unexpected results. This demo code DOES NOT try to reset the sprites to
	* the initial state they were in when recording started. You may want to
	* add this feature.
	* 
	* A message box will be shown if the demo file cannot be found.
	*/
   public void startDemo() {
	  interactionMode = InteractionMode.PLAYBACK;
	  final Vector<Serializable> playbackStream = new Vector<Serializable>();
	  try {
		 ObjectInputStream in = new ObjectInputStream(new FileInputStream("demo"));
		 while (true) {
			Serializable event = (Serializable) in.readObject();
			playbackStream.add(event);
		 } // while
	  } catch (FileNotFoundException e) {
		 JOptionPane.showMessageDialog(this, "Demo file not found.");
	  } catch (EOFException e) {
		 ; // ignore: This is how we know we're done reading the file
	  } catch (IOException e) {
		 e.printStackTrace();
	  } catch (ClassNotFoundException e) {
		 e.printStackTrace();
	  }

	  // Creates and starts a thread to play back the events
	  Thread playbackThread = new Thread(new Runnable() {
		 public void run() {
			boolean hadFirstWait = false;
			Iterator<Serializable> iter = playbackStream.iterator();
			while (iter.hasNext()
				   && interactionMode.equals(InteractionMode.PLAYBACK)) {
			   Long waitTime = (Long) iter.next();
			   MouseEvent event = (MouseEvent) iter.next();
			   try {
				  if (hadFirstWait) {
					 Thread.sleep(waitTime);
				  } else {
					 hadFirstWait = true;
				  } // fi
			   } catch (InterruptedException e) {
				  e.printStackTrace();
			   }
				
			   switch (event.getID()) {
				  case MouseEvent.MOUSE_PRESSED:
					 handleMousePressed(event);
					 break;
				  case MouseEvent.MOUSE_DRAGGED:
					 handleMouseDragged(event);
					 break;
				  case MouseEvent.MOUSE_RELEASED:
					 handleMouseReleased(event);
					 break;
			   } // switch
			} // while
		 }
      });
	  playbackThread.start();
   } // startDemo

   // Stops the demo
   public void stopDemo() {
	  interactionMode = InteractionMode.IDLE;
   } // stopDemo

}
