package com.joshuacrotts.sidescroller.blocks;

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
	private Handler handler;

	public Block(int x, int y, String fileLocation, Handler handler){
		super(x,y,ID.Block);
		this.handler = handler;
		
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
		
		g2.drawImage(this.sprite, x, y, null);
		g2.setColor(Color.RED);
		g2.draw(getBounds());
		
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
		return new Rectangle(x,y,getWidth(),getHeight());
	}
}
