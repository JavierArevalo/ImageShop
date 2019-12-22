/* 
 * Note: these methods are public in order for them to be used by other files
 * in this assignment; DO NOT change them to private.  You may add additional
 * private methods to implement required functionality if you would like.
 * 
 * You should remove the stub lines from each method and replace them with your
 * implementation that returns an updated image.
 */


//This class contains all the algorithms used for the imageshop program extension. The program is also 
//commented. The extensions are: three new color filters (red, blue and green), brush tool where user can
//select the color of the brush and when to erase what he draw, and the crop function. 
import java.awt.event.MouseEvent;
import java.util.*;
import acm.graphics.*;

public class ImageShopAlgorithmsExtension implements ImageShopAlgorithmsInterface {

	//The following three extensions will turn the screen into red, blue or green. The code gets the
	//luminosity of each pixel and puts it in the red, blue or green value while setting the other two 
	//to zero. Doing this creates a red image or green or blue. 
	public GImage red(GImage source) {
		int [][]pixels = source.getPixelArray();
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {
				int pixel = pixels[r][c];

				int red = GImage.getRed(pixel);
				int green = GImage.getGreen(pixel);
				int blue = GImage.getBlue(pixel);
				
				int luminosity = computeLuminosity(red, green, blue);
				int newPixel = GImage.createRGBPixel(luminosity, 0, 0);
				pixels[r][c] = newPixel;
			}
		}
		source.setPixelArray(pixels);
		return source;
	}
	
	public GImage green(GImage source) {
		int [][]pixels = source.getPixelArray();
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {
				int pixel = pixels[r][c];

				int red = GImage.getRed(pixel);
				int green = GImage.getGreen(pixel);
				int blue = GImage.getBlue(pixel);
				
				int luminosity = computeLuminosity(red, green, blue);
				int newPixel = GImage.createRGBPixel(0, luminosity, 0);
				pixels[r][c] = newPixel;
			}
		}
		source.setPixelArray(pixels);
		return source;
	}
	
	public GImage blue(GImage source) {
		int [][]pixels = source.getPixelArray();
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {
				int pixel = pixels[r][c];

				int red = GImage.getRed(pixel);
				int green = GImage.getGreen(pixel);
				int blue = GImage.getBlue(pixel);
				
				int luminosity = computeLuminosity(red, green, blue);
				int newPixel = GImage.createRGBPixel(0, 0, luminosity);
				pixels[r][c] = newPixel;
			}
		}
		source.setPixelArray(pixels);
		return source;
	}
	
	//This extension fills the new 2D array or the cropped image. The parameters x, y are the starting 
	//location of the cropped rectangle. W and L are the locations of the down-right corner of the rectangle.
	//The code grabs the pixels inside the cropped rectangle, and creates a new image from them. 
	//This method is called when the user has clicked crop and the user releases the mouse after he has 
	//selected the area he wants to crop. 
	public GImage crop(GImage source, int x, int y, int w, int l) {
		int [][] pixels = source.getPixelArray();
		int [][]croppedImage = new int [l-y][w-x];
		for (int r = y; r < l -1; r++) {
			for (int c = x; c <  w -1; c++) {
				int pixel = pixels[r][c];
				int newR = r - y;
				int newC = c-x;
				croppedImage[newR][newC] = pixel;
			}
		}
		GImage cropped = new GImage (croppedImage);
		return cropped;
	}
	
	public GImage flipHorizontal(GImage source) {

		//This method will create an array with the same dimensions to be able to flip the image.
		//Because the changed image is the same size, we just modify the array in place. 
		int[][] pixels = source.getPixelArray();
		int [][] flipHorizontal = new int[pixels.length][pixels[0].length];

		//column = x-coordinate, row = y-coordinate. 
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {	 
				//To flip the image, the code will flip the x position by using the length of the screen 
				//and the original x position. The y position or row will remain the same. 
				int pixel = pixels[r][c];
				flipHorizontal[r][pixels[0].length - 1 - c]= pixel;
			}
		}
		GImage newImage = new GImage(flipHorizontal); 

		return newImage;
	}

	//When rotating the image, the modified image can have different dimensions (if it is not a square).
	//As a result, we create a new array to store the modified pixel values with the correct dimensions. 
	public GImage rotateLeft(GImage source) {
		source.setLocation(0,0);
		int[][] pixels = source.getPixelArray();
		int [][] rotatedLeft = new int[pixels[0].length][pixels.length];

		for (int c = 0; c < pixels[0].length; c++) {
			for (int r = 0; r < pixels.length; r++) {
				//To create the effect of rotating left, we first switch the r and c positions. 
				//Then the new row value needs to be flipped using the length of the row and the current position.
				int pixel = pixels[r][c];
				rotatedLeft[pixels[0].length - 1 - c][r]= pixel;
			}
		}
		GImage newImage = new GImage(rotatedLeft); 

		return newImage;
	}

	//As in the previous algorithm, the modified image can have different dimensions so we create a new array. 
	public GImage rotateRight(GImage source) {
		source.setLocation(0,0);
		int[][] pixels = source.getPixelArray();
		int [][] rotatedRight = new int[pixels[0].length][pixels.length];

		for (int c = 0; c < pixels[0].length; c++) {
			for (int r = 0; r < pixels.length; r++) {
				int pixel = pixels[r][c];
				//To create the effect of rotating left, we first switch the r and c positions. 
				//Then the new column (second) value needs to be flipped using the length of the column
				//and the current position. The new column refers to the second []. 
				rotatedRight[c][pixels.length - 1 - r] = pixel;
			}
		}

		GImage newImage = new GImage(rotatedRight); 

		return newImage;
	}

	public GImage greenScreen(GImage source) {
		int[][] pixels = source.getPixelArray();

		//Comment
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {
				int pixel = pixels[r][c];
				int red = GImage.getRed(pixel);
				int blue = GImage.getBlue(pixel);
				int green = GImage.getGreen(pixel);
				int bigger = Math.max(blue, red);
				int bigger2 = Math.max(green, bigger * 2);
				if (green >= bigger2) {
					pixel = GImage.createRGBPixel(255, 255, 255, 0);
					pixels[r][c] = pixel;
				} else {
					int newPixel = GImage.createRGBPixel(red, green, blue);
					pixels[r][c] = newPixel;
				}
			}
			source.setPixelArray(pixels);
		}
		return source;
	}

	//To create the effect of equalize, the code will follow the steps given in the handout. 
		public GImage equalize(GImage source) {
			int [][] pixels = source.getPixelArray();
			int [] luminosityHistogram = new int[256];
			//First step is to create the luminocity histogram using the pixel's luminocity as index. 
			for (int r = 0; r < pixels.length; r++) {
				for (int c = 0; c < pixels[0].length; c++) {
					luminosityHistogram[getLuminocity(pixels[r][c])]++;
				}
			}
			//The second step is to create cumulative luminosity histogram. When creating the value at index j
			//the program will add the value at the cumulative distribution at j-1, already includes the sum of
			//values up to that index, and add the value at the luminosity histogram at j. 
			int [] cumulativeLuminosity = new int[256];
			for ( int j = 1; j < 256; j ++) {
				cumulativeLuminosity[j] = cumulativeLuminosity[j-1] + luminosityHistogram[j];
			}
			//The last step is to modify each pixel using cumulative luminosity to get the values needed in 
			//the equation when calculating the new RGB values. 
			//#PixelsWithLuminocity<=ThisPixel'sLuminocity = cumulativLuminosity[luminocity].
			for (int r = 0; r < pixels.length; r++) {
				for (int c = 0; c < pixels[0].length; c++) {
					int newRGB = 255 * cumulativeLuminosity[getLuminocity(pixels[r][c])] /cumulativeLuminosity[255];
					pixels[r][c] = GImage.createRGBPixel(newRGB, newRGB, newRGB);
				}
			}
			source.setPixelArray(pixels);
			return source;
		}


		//This method will return the luminocity of the pixel at r, c. 
		private int getLuminocity (int pixel) {
			int red = GImage.getRed(pixel);
			int green = GImage.getGreen(pixel);
			int blue = GImage.getBlue(pixel);
			int luminosity = computeLuminosity(red, green, blue);
			return luminosity;
		}

	public GImage negative(GImage source) {
		int[][] pixels = source.getPixelArray();

		// To get the negative of an image, we get the RGB values of each pixel and subtract them from
		//255 to create the new RGB values. Then we modify the current image array. 
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {
				int pixel = pixels[r][c];
				int red = 255 - GImage.getRed(pixel);
				int green = 255 - GImage.getGreen(pixel);
				int blue = 255 - GImage.getBlue(pixel);
				int newPixel = GImage.createRGBPixel(red, green, blue);
				pixels[r][c] = newPixel;
			}
			source.setPixelArray(pixels);
		}
		return source;
	}

	//The basic idea for this algorithm is for dx and dy to be able to wrap around if they are bigger
	//than the screen size or if they are negative to appropriately translate the image. 
	public GImage translate(GImage source, int dx, int dy) {
		int[][] pixels = source.getPixelArray();
		//We need a new array so that information/pixels dont get lost in the translation process.
		int [][] translatedImage = new int[pixels.length][pixels[0].length];
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {

				int pixel = pixels [r][c];
				//It wraps around using remainder operator so it knows what to do if the translation number
				//is greater than the image size. 
				int x = (c + dx) % pixels[0].length;
				if (x < 0) {
					x = pixels[0].length + x;
				}  
				//If the translated value is negative some pixels need to be translated from the left side of 
				//an image to the right side. To do so, we add the negative value to the other edge. 
				//Because the value is negative, adding it will result in a value smaller than the length/edge 
				//so it wraps around the image as it should. The rest of the pixels just shift to the left. 
				int y = (r + dy) % pixels.length;
				if (y < 0) {
					y = pixels.length +y;
				} 
				translatedImage[y][x] = pixel;

			}
		}
		GImage newImage = new GImage(translatedImage);
		return newImage;
	}

	private void println(int x) {
		// TODO Auto-generated method stub

	}

	//To blur the image, the algorithm will get the rgb values of every pixel surrounding it and its current
	//rgb values and average them. Then it will set the current pixel to the average of the rgb values. 
	public GImage blur(GImage source) {
		int[][] pixels = source.getPixelArray();
		int [][] blurred = new int[pixels.length][pixels[0].length];

		//To be able to access them throughout the method, we declare them at the beggining. 
		for (int r = 0; r < pixels.length; r++) {
			for (int c = 0; c < pixels[0].length; c++) {
				int redSum = 0;
				int redCount = 0;
				int greenSum = 0;
				int greenCount= 0;
				int blueSum = 0;
				int blueCount = 0;
				
				//The following code will loop over all the possible neighbors the current pixel has.
				//At each candidate, it will check if it exists and if it does add its RGB values and 
				//the counts. The pixel itself is also included in the loop. 
				for (int a = r-1; a < r+2; a ++) {
					for (int b = c-1; b < c+2; b ++) {
						if (a >= 0 && a < pixels.length && b >= 0 && b < pixels[0].length) {
						int currentPixel = pixels[a][b];
						redSum +=  GImage.getRed(currentPixel);
						redCount++;
						greenSum +=  GImage.getGreen(currentPixel);
						greenCount++;
						blueSum +=  GImage.getBlue(currentPixel);
						blueCount++;
						}
					}
				}
				
				//Once it has gotten the sum of all the RGB values of its neighbors and the count of how many neighbors it
				//many neighbors it has, it will average them and set them to the RGB values of the current pixel. 
				int redFinal = redSum/redCount;
				int greenFinal = greenSum/greenCount;
				int blueFinal = blueSum/blueCount;
				int newPixel = GImage.createRGBPixel(redFinal, greenFinal, blueFinal);
				blurred[r][c] = newPixel;

			}
		}
		GImage newImage = new GImage(blurred); 

		return newImage;
	}
}
