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
import java.lang.Math;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Excavator {

   public static void main(String[] args) {		
      SpriteCanvas canvas = new SpriteCanvas();
	  canvas.addSprite(makeSprite());

	  JFrame f = new JFrame("Excavator");
	  f.setJMenuBar(makeMenuBar(canvas));
      f.getContentPane().add(canvas);
	  f.getContentPane().setLayout(new GridLayout(1, 1));
	  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  f.setSize(800, 600);
	  f.setVisible(true);	  
   } // main

   private static Sprite makeSprite() {
      Sprite body = new BodySprite();
      Sprite treads = new TreadsSprite(250, 50);
      Sprite boom = new BoomSprite(250, 30, 15, 15);
      Sprite arm = new ArmSprite(210, 30, 35, 15);
      Sprite bucket = new BucketSprite(-10, -40);
      
      body.transform(AffineTransform.getTranslateInstance(400, 350));
      treads.transform(AffineTransform.getTranslateInstance(-50, 11));
      boom.transform(AffineTransform.getTranslateInstance(30, -35));
      boom.transform(AffineTransform.getRotateInstance(Math.toRadians(225)));
      arm.transform(AffineTransform.getTranslateInstance(220, 50));
      arm.transform(AffineTransform.getRotateInstance(Math.toRadians(270)));
      bucket.transform(AffineTransform.getTranslateInstance(230, 10));
      bucket.transform(AffineTransform.getRotateInstance(Math.toRadians(270)));

      body.addChild(boom);
      boom.addChild(arm);
      arm.addChild(bucket);
      body.addChild(treads);
      
      return body;
   } // makeSprite

   private static void resetCanvas(final SpriteCanvas canvas) {
      canvas.removeAllSprites();
      canvas.addSprite(makeSprite());
      canvas.repaint();
   } // resetCanvas

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
               resetCanvas(canvas);
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
			   resetCanvas(canvas);
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
