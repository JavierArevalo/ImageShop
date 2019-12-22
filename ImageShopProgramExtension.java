/*
 * CS 106A ImageShop
 *
 * This instructor-provided file implements the graphical user interface (GUI)
 * for the ImageShop program and allows you to test the behavior of your image algorithms.
 *
 * Author : Nick Troccoli (based on previous versions by Marty Stepp and Eric Roberts)
 * Version: Sun 2017/07/30
 *
 * Your program should work properly with an UNMODIFIED version of this file.
 * If you want to modify this file for testing or for fun, that is your choice,
 * but when we grade your program we will do so with the original unmodified
 * version of this file, so your code must still work properly with that code.
 *
 * This file and its contents are copyright (C) Stanford University and Nick Troccoli,
 * licensed under Creative Commons Attribution 2.5 License.  All rights reserved.
 */

import acm.program.*;
import acm.util.*;
import acm.graphics.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.imageio.*;
import javax.swing.*;

import stanford.cs106.diff.*;
import stanford.cs106.gui.*;


//This class contains the implementations needed to perform the extensions in the algorithms file. 
//The code for brush tool is here because it consists of a mouse event. The color is selected based
//on what button the user pressed. The crop tool starts working when the user clicks crop button.
//Then the user can select the rectangle in the screen that he wants to crop. 

public class ImageShopProgramExtension extends GraphicsProgram {
	
	Color color;

	// Valid file extensions for image types that we can write.
	private static final String[] SAVE_IMAGE_EXTENSIONS = new String[] { "png", "bmp", "wbmp" };

	// Valid file extensions for images that we can read.
	private static final String[] LOAD_IMAGE_EXTENSIONS = new String[] { "png", "bmp", "wbmp",
			"jpg", "gif", "jpeg" };

	// The general info label displayed at the top of the window
	private JLabel infoLabel;

	// The x/y/r/g/b label displayed at the bottom of the window
	private JLabel statsLabel;

	// The current image displayed on the canvas (or null if no image)
	private GImage currentImage;

	// The image algorithms object that runs the algorithms
	private ImageShopAlgorithmsExtension algorithms;

	public void init() {
		addButtons();
		addActionListeners();
		setTitle("CS 106A ImageShop");
		algorithms = new ImageShopAlgorithmsExtension();
	}
	
	// Add the interactors to the screen
	private void addButtons() {
		add(new JButton("Load Image"), WEST);
		add(new JButton("Save Image"), WEST);
		add(new JButton("Overlay Image"), WEST);
		add(new JButton("Compare To Image"), WEST);
		add(new JSeparator(), WEST);
		add(new JButton("Negative"), WEST);
		add(new JButton("Green Screen"), WEST);
		add(new JButton("Rotate Left"), WEST);
		add(new JButton("Rotate Right"), WEST);
		add(new JButton("Flip Horizontal"), WEST);
		add(new JButton("Translate"), WEST);
		add(new JButton("Blur"), WEST);
		add(new JButton("Equalize"), WEST);
		add(new JButton("Red"), WEST);
		add(new JButton("Green"), WEST);
		add(new JButton("Blue"), WEST);
		//The following button will start the crop function and allow the user to select where to crop. 
		add(new JButton("Crop"), WEST);
		//In the following buttons, the user can pick any of the three new filters.
		add(new JButton("Red"), WEST);
		add(new JButton("Green"), WEST);
		add(new JButton("Blue"), WEST);
		//In the following buttons, the user can pick the color of the brush or can erase them.
		add(new JButton("Blue Brush"), SOUTH);
		add(new JButton("Red Brush"), SOUTH);
		add(new JButton("Green Brush"), SOUTH);
		add(new JButton("Yellow Brush"), SOUTH);
		add(new JButton("Orange Brush"), SOUTH);
		add (new JButton ("Erase"), SOUTH);

		infoLabel = new JLabel("Welcome to CS 106A ImageShop!");
		add(infoLabel, NORTH);

		statsLabel = new JLabel(" ");
		add(statsLabel, SOUTH);
	}

	//Whenever the button cropped is pressed, it will set the cropImage boolean to be true. This will
	//make sure that the code runs only when the user wants to select the cropped image and not everytime
	//the mouse is pressed or released. 
	boolean cropImage;
	int x;
	int y;
	//when the mouse is pressed, we get the x and y values of where the rectangle cropped image started. 
	public void mousePressed (MouseEvent e) {
		if (cropImage) {
		x = e.getX();
		y = e.getY();
		GOval oval = new GOval (x, y, 5, 5);
		oval.setFilled(true);
		add(oval);
		pause(100);
		remove(oval);
		}
	}
	//When the mouse is released we get the x and y values of the low right corner of the rectangle cropped
	//image and call the crop algorithm giving it the information it needs. 
	public void mouseReleased (MouseEvent e) {
		if (cropImage) {
		int YY = e.getY() ;
		int XX = e.getX() ;
		GOval oval = new GOval (XX, YY, 5, 5);
		oval.setFilled(true);
		add(oval);
		pause(200);
		remove(oval);
		GImage newImage = algorithms.crop(currentImage, x, y, XX, YY);
		setImage(newImage);
		cropImage=false;
		}
	}
	
	//This is the main code for the brush function. Whenever the mouse is dragged, it will create a
	//oval of the selected color. If no color is selected, the brush will be black. 
	public void mouseDragged (MouseEvent e) {
		if (!cropImage) {
		double x = e.getX();
		double y = e.getY();
		GOval oval = new GOval(x,y, 10,10);
		oval.setFilled(true);
		if (color != null) {
		oval.setColor(color);
		
		}
		add(oval);
		}
		
	}
	
	// Respond to one of the buttons on the left side being clicked
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		//This command set the brush color depending on what button was clicked. 
				if (command.equals("Blue Brush")) {
					color = Color.BLUE;
				}
				if (command.equals("Red Brush")) {
					color = Color.RED;
				}
				if (command.equals("Green Brush")) {
					color = Color.GREEN;
				}
				if (command.equals("Yellow Brush")) {
					color = Color.YELLOW;
				}
		if (command.equals("Orange Brush")) {
			color = Color.ORANGE;
		}
		if (command.equals("Load Image")) {
			loadImage();
		} else if (command.equals("Save Image")) {
			saveImage();
		} else if (command.equals("Overlay Image")) {
			overlayImage();
		} else if (command.equals("Compare To Image")) {
			diffImage();
		} else if (currentImage == null) {
			showErrorPopup("please load an image.");
		} else if (command.equals("Flip Horizontal")) {
			GImage newImage = algorithms.flipHorizontal(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Rotate Left")) {
			GImage newImage = algorithms.rotateLeft(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Rotate Right")) {
			GImage newImage = algorithms.rotateRight(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Green Screen")) {
			GImage newImage = algorithms.greenScreen(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Equalize")) {
			GImage newImage = algorithms.equalize(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Negative")) {
			GImage newImage = algorithms.negative(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
			//When the crop button is pressed, the boolean is set to true.
		} else if (command.equals("Crop")) {
			cropImage = true;
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Translate")) {
			int dx = readInteger("dx?");
			int dy = readInteger("dy?");
			GImage newImage = algorithms.translate(currentImage, dx, dy);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Blur")) {
			GImage newImage = algorithms.blur(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		} else if (command.equals("Red")){
			GImage newImage = algorithms.red(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		
		} else if (command.equals("Green")){
			GImage newImage = algorithms.green(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		
		} else if (command.equals("Blue")){
			GImage newImage = algorithms.blue(currentImage);
			setImage(newImage);
			infoLabel.setText(command + " filter applied.");
		
		} else if (command.equals("Erase")) {
			clearCanvas();
			setImage(currentImage);
		
	} else {
			infoLabel.setText("Unknown command " + command + ".");
		}
	}

	// When the mouse moves in the image, update the info label at the bottom
	public void mouseMoved(MouseEvent e) {
		if (inImageBounds(e.getX(), e.getY())) {
			String status = "(x=" + e.getX() + ", y=" + e.getY() + ")";
			int pixel = currentImage.getPixelArray()[e.getY()][e.getX()];
			status += " (R=" + GImage.getRed(pixel) + ", G=" + GImage.getGreen(pixel) + ", B="
					+ GImage.getBlue(pixel) + ")";
			statsLabel.setText(status);
		} else {
			statsLabel.setText(" ");
		}
	}
	

	// Returns whether or not the given coordinate is in the current image
	private boolean inImageBounds(int x, int y) {
		if (currentImage == null) {
			return false;
		} else {
			double height = currentImage.getHeight();
			double width = height <= 0 ? 0 : currentImage.getWidth();
			return x >= 0 && x < width && y >= 0 && y < height;
		}
	}

	/*
	 * Pops up dialog boxes asking the user to type an integer repeatedly until
	 * the user types a valid integer.
	 */
	private int readInteger(String prompt) {
		while (true) {
			try {
				String result = JOptionPane.showInputDialog(prompt);
				int num = Integer.parseInt(result);
				return num;
			} catch (NumberFormatException e) {
				// empty; re-prompt
			} catch (NullPointerException e) {
				// empty; re-prompt
			}
		}
	}

	// Sets the given image as the current image on the canvas, and resizes canvas.
	private void setImage(GImage image) {
		if (currentImage != null) {
			remove(currentImage);
		}

		setBackground(new Color(238, 238, 238));
		setCanvasSize(image.getWidth(), image.getHeight());
		add(image);
		currentImage = image;
	}

	/* Returns a File representing the image directory, which is either the res/
	 * directory or the user directory.
	 */
	private File getImageDirectory() {
		File dir = new File(System.getProperty("user.dir") + "/res");
		if (!dir.isDirectory()) {
			dir = new File(System.getProperty("user.dir"));
		}
		return dir;
	}
	
	/* Returns a File representing the output directory, which is either the output/
	 * directory or the user directory.
	 */
	private File getOutputDirectory() {
		File dir = new File(System.getProperty("user.dir") + "/output");
		if (!dir.isDirectory()) {
			dir = new File(System.getProperty("user.dir"));
		}
		return dir;
	}

	// Shows a file prompt to load in a new image, and displays the chosen image onscreen.
	private void loadImage() {
		// Initialize the file chooser prompt
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(GuiUtils.getExtensionFileFilter("Image files", LOAD_IMAGE_EXTENSIONS));
		chooser.setCurrentDirectory(getImageDirectory());

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			// Load the image and add it to the canvas
			File currentFile = chooser.getSelectedFile();
			GImage image = new GImage(currentFile.getAbsolutePath());
			setImage(image);
			infoLabel.setText("Loaded image " + currentFile.getName() + ".");
		}
	}

	// Shows a file prompt to save the current image (if any) to a file.
	private void saveImage() {
		if (currentImage == null) {
			showErrorPopup("no image to save.");
			return;
		}

		// Initialize the file chooser prompt
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(GuiUtils.getExtensionFileFilter(".png, .bmp, and .wbmp files", SAVE_IMAGE_EXTENSIONS));
		chooser.setCurrentDirectory(getImageDirectory());

		if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {	
			// If they are overwriting and made a mistake, cancel
			if (chooser.getSelectedFile().exists()) {
				if (JOptionPane.showConfirmDialog(this,
								"File already exists. Overwrite?\n(You probably shouldn't overwrite the instructor-provided images; save them with a different name)",
								"Overwrite?", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
					return;
				}
			}
			
			// Save the image to file
			try {
				currentImage.saveImage(chooser.getSelectedFile());
				infoLabel.setText("Saved image to " + chooser.getSelectedFile().getName() + ".");
			} catch (ErrorException e) {
				showErrorPopup("Invalid filename (make sure to include a file extension (.png, etc.).");
			}
		}
	}

	// Overlays a selected image on top of the currently-loaded image.
	private void overlayImage() {
		if (currentImage == null) {
			showErrorPopup("no image on which to overlay.  Please load an image.");
			return;
		}

		// Initialize the file chooser prompt
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(GuiUtils.getExtensionFileFilter("Image files", LOAD_IMAGE_EXTENSIONS));
		chooser.setCurrentDirectory(getImageDirectory());

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			// Create an Image from the current image
			Image oldImage = currentImage.getImage();
			int width = oldImage.getWidth(getGCanvas());
			int height = oldImage.getHeight(getGCanvas());

			// Create a new Image for the loaded overlay image
			Image newImage = getGCanvas().createImage(width, height);
			Graphics g = newImage.getGraphics();
			g.drawImage(oldImage, 0, 0, getGCanvas());
			File file = chooser.getSelectedFile();
			Image overlay = new GImage(file.getAbsolutePath()).getImage();
			int x0 = (width - overlay.getWidth(getGCanvas())) / 2;
			int y0 = (height - overlay.getHeight(getGCanvas())) / 2;
			g.drawImage(overlay, x0, y0, getGCanvas());

			currentImage.setImage(newImage);
			infoLabel.setText("Overlayed image " + file.getName() + ".");
		}
	}

	//Shows a "Diff Image" window to compare the pixels of two images.
	private void diffImage() {
		if (currentImage == null) {
			showErrorPopup("no image currently displayed.");
			return;
		}
		
		// Initialize the file chooser prompt
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(GuiUtils.getExtensionFileFilter("Image files", LOAD_IMAGE_EXTENSIONS));
		chooser.setCurrentDirectory(getOutputDirectory());
		
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File imageFile2 = chooser.getSelectedFile();
			try {
				Image image2 = ImageIO.read(imageFile2);
				Image image1 = currentImage.getImage();
				new DiffImage(image1, image2);
			} catch (IOException ioe) {
				showErrorPopup("could not read diff image data from " + imageFile2.getName() + ": " + ioe.getMessage());
			}
		}
	}
	
	/*
	 * Displays a popup message dialog box to display the given error message.
	 * Also puts the error message into the top info label.
	 */
	private void showErrorPopup(String text) {
		JOptionPane.showMessageDialog(this, "Error: " + text, "Error", JOptionPane.ERROR_MESSAGE);
		infoLabel.setText("Error: " + text);
	}
}

