package com.joshuacrotts.sidescroller.enemies;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.sidescroller.main.GameObject;

public abstract class Enemy extends GameObject{

	private double x;
	private double y;
	
	private int width;
	private int height;
	
	private String fileLocation;
	private BufferedImage image;
	
	public Enemy(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Enemy(int x, int y, String fileLocation){
		this.x = x;
		this.y = y;
		
		this.fileLocation = fileLocation;
		
		try{
			image = ImageIO.read(new File(fileLocation));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	@Override
	public abstract void tick();

	@Override
	public abstract void render(Graphics g);

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)x,(int)y,width,height);
	}

	public double getX() {
		return x;
	}

	public void setX(double d) {
		this.x = d;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

	
}
