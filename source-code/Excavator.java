//==============================================================================
// CS349 Assignment 02, Excavator
//
// @description: A directly manipulatable excavator in Java
// @author: Ah Hoe Lai
// @userid: ahlai
// @version: 1.0 17/10/2015
//==============================================================================

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Excavator {

   public static void main(String[] args) {		
      SpriteCanvas canvas = new SpriteCanvas();
	  canvas.addSprite(Excavator.makeSprite());

	  JFrame f = new JFrame("Excavator");
	  JMenuBar menuBar = Excavator.makeMenuBar(canvas);
	  //f.setJMenuBar(menuBar);
      f.getContentPane().add(canvas);
	  f.getContentPane().setLayout(new GridLayout(1, 1));
	  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  f.setSize(800, 600);
	  f.setVisible(true);	  
   } // main

   private static Sprite makeSprite() {
   	  int[][] bodyPoints =
   	     {{0, 0}, {0, -30}, {40, -100}, {90, -100}, {90, -55}, {200, -55}, {200, 0}};
      Sprite body = new BodySprite(bodyPoints);
      int[][] doorPoints =
         {{0, 0}, {0, -20}, {35, -80}, {70, -80}, {70, 0}};
      Sprite door = new DoorSprite(doorPoints);
      int[][] slewingPoints =
         {{0, 0}, {0, -10}, {150, -10}, {150, 0}};
      Sprite slewing = new SlewingSprite(slewingPoints);
      Sprite treads = new TreadsSprite(250, 50);
      
      body.transform(AffineTransform.getTranslateInstance(200, 200));
      door.transform(AffineTransform.getTranslateInstance(10, -10));
      slewing.transform(AffineTransform.getTranslateInstance(25, 11));
      treads.transform(AffineTransform.getTranslateInstance(-50, 11));

      body.addChild(door);
      body.addChild(slewing);
      body.addChild(treads);

      return body;
   } // makeSprite

   // Makes a menu with resetting and quitting and another menu
   // with recording and playback
   private static JMenuBar makeMenuBar(final SpriteCanvas canvas) {
	  JMenuBar menuBar = new JMenuBar();

      JMenu file = new JMenu("File");
      final JMenuItem reset = new JMenuItem("Reset");
      reset.setAccelerator(KeyStroke.getKeyStroke("control R"));
      final JMenuItem quit = new JMenuItem("Quit");

      file.add(reset);
      file.addSeparator();
      file.add(quit);

      reset.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         	if (reset.getText().equals("Reset")) {

         	} else {
         	   assert false;
         	} // if
         }
      });

      quit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         	if (quit.getText().equals("Quit")) {
               System.exit(0);
         	} else {
         	   assert false;
         	} // if
         }
      });

	  JMenu script = new JMenu("Scripting");
	  final JMenuItem record = new JMenuItem("Start recording");
	  final JMenuItem play = new JMenuItem("Start script");

	  script.add(record);
	  script.add(play);

	  record.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
			if (record.getText().equals("Start recording")) {
			   record.setText("Stop recording");
			   canvas.startRecording();
			} else if (record.getText().equals("Stop recording")) {
			   record.setText("Start recording");
			   canvas.stopRecording();
			} else {
			   assert false;
			} // if
		 }
	  });

	  play.addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
			if (play.getText().equals("Start script")) {
			   play.setText("Stop script");
			   record.setEnabled(false);
			   canvas.startDemo();
			} else if (play.getText().equals("Stop script")) {
			   play.setText("Start script");
			   record.setEnabled(true);
			   canvas.stopRecording();
			} else {
			   assert false;
			} // if
		 }
	  });

      menuBar.add(file);
	  menuBar.add(script);
	  return menuBar;
   } // makeMenuBar

}
