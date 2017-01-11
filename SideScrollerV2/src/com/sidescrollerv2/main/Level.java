package com.sidescrollerv2.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sidescrollerv2.enemies.Enemy;

public class Level {
	
	public final short WIDTH;
	public final short HEIGHT;
	
	private short x;
	private short y;
	
	private String fileLocation;
	
	private BufferedImage image;
	
	private ArrayList<Enemy> enemies;
	
	public Level(String fileLocation, short width, short height){
		this.x = 0;
		this.y = 0;
		this.enemies = new ArrayList<Enemy>();
		this.WIDTH = width;
		this.HEIGHT = height;
		
		this.fileLocation = fileLocation;
		
		try{
			image = ImageIO.read(new File(fileLocation));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void tick(){

	}
	
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.image, 0, 0, null);
	}

	public void add(Enemy e){
		this.enemies.add(e);
	}
	
	public int getPlayerSpeed(){
		for(int i = 0; i<Game.handler.getEntities().size(); i++){
			if(Game.handler.getEntities().get(i).getID() == ID.Player){
				return (int) Game.handler.getEntities().get(i).getVelX();
			}
		}
		return -1;
	}
	
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
	
	
}
