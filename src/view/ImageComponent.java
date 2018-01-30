package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class ImageComponent extends JPanel {
    
	private BufferedImage image;
	private int divideByImgSize = 8;
    
    public ImageComponent(String path, int divideByImgSize){
    	this.divideByImgSize = divideByImgSize;
    	this.setBackground(Color.WHITE);

        try{
        	image = ImageIO.read(new File(path));
        	int height = image.getHeight() / divideByImgSize;
         	int width = image.getWidth() / divideByImgSize;
            this.setPreferredSize(new Dimension(width, height));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public void paintComponent (Graphics g){
    	super.paintComponent(g);
    	int height = image.getHeight() / divideByImgSize;
    	int width = image.getWidth() / divideByImgSize;
        g.drawImage(image, 0, 0, width, height, this);
        if (this.getPreferredSize().getHeight() != height &&
        	this.getPreferredSize().getWidth() != width) {
        	this.setPreferredSize(new Dimension(width, height));
        }
    }
}