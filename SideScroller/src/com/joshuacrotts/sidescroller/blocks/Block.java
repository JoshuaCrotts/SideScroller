package com.joshuacrotts.sidescroller.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.sidescroller.main.Game;
import com.joshuacrotts.sidescroller.main.GameObject;
import com.joshuacrotts.sidescroller.main.Handler;
import com.joshuacrotts.sidescroller.main.ID;
import com.joshuacrotts.sidescroller.main.Player;

public class Block extends GameObject{
	
	private BufferedImage sprite;
	private Game game;
	private Handler handler;
	private Player player;

	
	public Block(int x, int y, String fileLocation, Handler handler, Game game, Player p){
		super(x,y,ID.Block);
		this.handler = handler;
		this.game = game;
		this.player = p;
		
		try{
			sprite = ImageIO.read(new File(fileLocation));
		}catch(IOException e){
			System.err.println("Error! Could not load in Block Image");
		}
		
		handler.add(this);
		
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(this.sprite, (int)x, (int)y, null);
		//g2.draw(getBounds());
		g2.setColor(Color.BLUE);
		g2.draw(getBoundsTop());
		g2.setColor(Color.green);
		g2.draw(getBoundsBottom());
		g2.setColor(Color.ORANGE);
		g2.draw(getBoundsLeft());
		g2.setColor(Color.yellow);
		g2.draw(getBoundsRight());
		
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return sprite.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return sprite.getHeight();
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle((int)x,(int)y,getWidth(),getHeight());
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int)x, (int)y, sprite.getWidth(), 1);
	}
	public Rectangle getBoundsBottom(){
		return new Rectangle((int)x,(int)y+sprite.getHeight(),sprite.getWidth(), 1);
	}
	public Rectangle getBoundsLeft(){
		return new Rectangle((int)x,(int)y,1,sprite.getHeight());
	}
	public Rectangle getBoundsRight(){
		return new Rectangle((int)x+sprite.getWidth(), (int)y, 1, sprite.getHeight());
	}
}
