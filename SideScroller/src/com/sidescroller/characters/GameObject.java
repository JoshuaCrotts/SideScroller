package com.sidescroller.characters;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.sidescroller.game.Game;
import com.sidescroller.game.ID;

public abstract class GameObject {

	//Motion variables
	protected short x;
	protected short y;
	
	protected short velX;
	protected short velY;
	
	private byte width;
	private byte height;
	
	private ID ID;
	
	//Current Sprite
	public BufferedImage currentSprite;
	
	public GameObject(short x, short y, ID id){
		this.x = x;
		this.y = y;
		this.ID = id;
	}
	
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
	
	public abstract void tick();
	
	public abstract void render(Graphics g);

	public short getX() {
		return x;
	}

	public void setX(short x) {
		this.x = x;
	}

	public short getY() {
		return y;
	}

	public void setY(short y) {
		this.y = y;
	}
	
	public short getVelX(){
		return velX;
	}
	
	public short getVelY(){
		return velY;
	}
	
	public void setVelX(short velX){
		this.velX = velX;
	}
	
	public void setVelY(short velY){
		this.velY = velY;
	}
	
	public void setWidth(byte width){
		this.width = width;
	}
	public void setHeight(byte height){
		this.height = height;
	}
	
	public byte getWidth(){
		return width;
	}
	public byte getHeight(){
		return height;
	}

	public ID getID() {
		return ID;
	}

	public void setID(ID iD) {
		ID = iD;
	}

	public BufferedImage getImage() {
		return currentSprite;
	}

	public void setImage(BufferedImage currentSprite) {
		this.currentSprite = currentSprite;
	}
	
	public abstract Rectangle getBounds();
	
	
}