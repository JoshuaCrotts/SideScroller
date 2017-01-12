package com.sidescrollerv2.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Bullet extends GameObject{
	
	private Direction dir;
	
	public Bullet(short x, short y){
		super(x,y, ID.Bullet);
		
		this.dir = Game.player.getDirection();
		
		super.setWidth((byte) 10);
		super.setHeight((byte) 10);
		
		Game.handler.add(this);
		
	}
	
	public void tick(){
		
		if(dir.equals("left")){
			super.setX((short) (super.getX()-35));
		}else{
			super.setX((short) (super.getX()+35));
		}
		
		if(super.getX() <= -25){
			Game.handler.getEntities().remove(this);
		}
		
		//This may need to be extended to compensate for the larger map frame.
		if(super.getX() >= 3225){
			Game.handler.getEntities().remove(this);
		}
	}
	
	@Override
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.ORANGE);
		g2.fillOval(super.getX(), super.getY(), 10, 10);
		g2.setColor(Color.RED);
		g2.draw(getBounds());
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), super.getWidth(), super.getHeight());
	}
}