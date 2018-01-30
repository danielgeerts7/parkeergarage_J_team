package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class ImageComponent extends JPanel {
    
	private BufferedImage image;
	private double divideByImgSize = 8;
	private int paddingLeft = 60;
	private int paddingTop = 15;
    
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