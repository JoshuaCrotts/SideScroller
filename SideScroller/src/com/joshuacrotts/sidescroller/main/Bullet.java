package com.joshuacrotts.sidescroller.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bullet extends GameObject{

	private int x;
	private int y;
	
	private int width = 10;
	private int height = 10;
	
	private Handler handler;
	
	private Direction dir;
	
	public Bullet(int x, int y, Handler handler){
		this.x = x;
		this.y = y;
		
		this.handler = handler;
		
		super.setId(ID.Bullet);
		
		this.dir = Game.player.getLastDirection();
		handler.add(this);
		
	}
	
	public void tick(){
		
		if(dir == Direction.Left){
			x-=35;
		}else{
			x += 35;
		}
		
		if(x <= -25){
			handler.getEntities().remove(this);
		}
		
		if(x >= 3225){
			handler.getEntities().remove(this);
		}
	}
	
	@Override
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.ORANGE);
		g2.fillOval(x, y, width, height);
		g2.setColor(Color.RED);
		g2.draw(getBounds());
		
	}

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
		return new Rectangle(x,y,width,height);
	}
}
