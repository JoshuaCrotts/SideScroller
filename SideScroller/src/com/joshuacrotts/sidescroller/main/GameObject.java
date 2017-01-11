package com.joshuacrotts.sidescroller.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public abstract class GameObject {

	protected int x;
	protected int y;
	
	protected int velX;
	protected int velY;
	
	protected ID id;

	
	public BufferedImage currentSprite;

	public GameObject(int x, int y, ID id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public GameObject() {

	}

	public abstract void tick();

	public abstract void render(Graphics g);

	public boolean withinRenderField() {
		
		// If within x-render field
		if (Game.camera.getRenderMinX() < this.x && this.x < Game.camera.getRenderMaxX()) {
			// If within y-render field
			if (Game.camera.getRenderMinY() < this.y && this.y < Game.camera.getRenderMaxY()) {
				return true;
			}
		}
		return false;
	}

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

	public int getVelX() {
		return velX;
	}

	public void setVelX(int velX) {
		this.velX = velX;
	}

	public int getVelY() {
		return velY;
	}

	public void setVelY(int f) {
		this.velY = f;
	}

	public void addVelY(int add) {
		if (Math.abs(this.getVelY() + add) <= 5)
			this.setVelY(this.getVelY() + add);
	}

	public void addVelX(int add) {
		if (Math.abs(this.getVelX() + add) <= 5)
			this.setVelX(this.getVelX() + add);
		else{
			this.setVelX(5);
		}
	}

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract Rectangle getBounds();

}
