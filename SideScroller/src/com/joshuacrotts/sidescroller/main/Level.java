package com.joshuacrotts.sidescroller.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.joshuacrotts.sidescroller.enemies.Enemy;

public class Level {

	private int x;
	private int y;
	
	private Game game;
	
	private String fileLocation;
	
	private BufferedImage image;
	
	private Handler handler;
	
	private ArrayList<Enemy> enemies;
	
	private boolean scrolling = false;
	
	public static int minView, maxView;
	
	public Level(String fileLocation, Game game, Handler handler){
		this.x = 0;
		this.y = 0;
		this.game = game;
		this.handler = handler;
		this.enemies = new ArrayList<Enemy>();
		
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
		for(int i = 0; i<handler.getEntities().size(); i++){
			if(handler.getEntities().get(i).getId() == ID.Player){
				return (int) handler.getEntities().get(i).getVelX();
			}
		}
		return -1;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
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
