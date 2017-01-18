package com.sidescroller.weapons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;

import javax.imageio.ImageIO;

import com.sidescroller.characters.Direction;
import com.sidescroller.characters.GameObject;
import com.sidescroller.game.Game;
import com.sidescroller.game.ID;

public class Bullet extends GameObject{
	
	private Direction dir;
	
	public Bullet(short x, short y){
		super(x,y, ID.Bullet);
		
		this.dir = Game.player.getDirection();
		
		try{
			this.currentSprite = ImageIO.read(new File("resources/img/sprites/p/bullet/bullet1.png"));
		}catch(Exception e){
			System.err.println("Error! Could not load bullet image.");
			e.printStackTrace();
		}
		
		super.setWidth((byte) 10);
		super.setHeight((byte) 10);
		
		Game.handler.add(this);
		
	}
	
	public void tick(){
		
		if(dir == Direction.Left){
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
		
		g2.drawImage(this.currentSprite, x, y, null);
		g2.setColor(Color.BLUE);
		
		if(Game.borders)
			g2.draw(getBounds());
		
	}
	
	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), super.getWidth(), super.getHeight());
	}
}