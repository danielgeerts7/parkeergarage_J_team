package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class makes it easier to add a image to the application
 * Because it extends from JPanel the developer can easily add is to the main JFrame
 * 
 * @author danielgeerts7
 * @version 30-01-2018
 */
class ImageComponent extends JPanel {
    
	private BufferedImage image;
	private double divideByImgSize = 8;
	private int paddingLeft = 60;
	private int paddingTop = 15;
    
	/**
	 * 
	 * @param path divines to path where the images is stored
	 * @param the divideByImgSize is an double that is being divided by the height and width
	 *		  this way the developer can easily decrease the size of the image
	 */
    public ImageComponent(String path, double divideByImgSize){
    	this.divideByImgSize = divideByImgSize;
    	this.setBackground(Color.WHITE);

        try{
        	image = ImageIO.read(new File(path));
        	int height = (int)(image.getHeight() / divideByImgSize);
         	int width = (int)(image.getWidth() / divideByImgSize);
            this.setPreferredSize(new Dimension(width + paddingLeft, height + paddingTop));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * This overridden method takes care of the reduction of the given image 
     */
    @Override
    public void paintComponent (Graphics g){
    	super.paintComponent(g);
    	int height = (int)(image.getHeight() / divideByImgSize);
     	int width = (int)(image.getWidth() / divideByImgSize);
        g.drawImage(image, paddingLeft, paddingTop, width, height, this);
        if (this.getPreferredSize().getHeight() != height &&
        	this.getPreferredSize().getWidth() != width) {
            this.setPreferredSize(new Dimension(width + paddingLeft, height + paddingTop));
        }
    }
}