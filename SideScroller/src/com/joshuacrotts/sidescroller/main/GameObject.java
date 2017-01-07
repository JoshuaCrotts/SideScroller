package com.joshuacrotts.sidescroller.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class GameObject {
		
	protected int x;
	protected int y;
	
	protected ID id;
	
	protected double velX;
	protected double velY;
	public BufferedImage currentSprite;
	
	public GameObject(int x, int y, ID id){
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public GameObject(){
		
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double f) {
		this.velY = f;
	}
	
	public void addVelY(float add){
		if (Math.abs(this.getVelY() + add) <= 5)
			this.setVelY(this.getVelY() + add);
	}
	
	public void addVelX(float add){
		if (Math.abs(this.getVelX() + add) <= 5)
			this.setVelX(this.getVelX() + add);
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract Rectangle getBounds();
	
	
}
