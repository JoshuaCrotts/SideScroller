package com.sidescroller.enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.sidescroller.main.Animator;
import com.sidescroller.main.Direction;
import com.sidescroller.main.Game;
import com.sidescroller.main.ID;

public class Runner extends Enemy{

	private ArrayList<BufferedImage> lSprites;
	private ArrayList<BufferedImage> rSprites;
	
	private Animator lAnimator;
	private Animator rAnimator;

	private Direction dir;
	
	public Runner(short x, short y){
		super(x,y);

		this.setID(ID.Runner);

		lSprites = new ArrayList<BufferedImage>();
		rSprites = new ArrayList<BufferedImage>();
		
		lAnimator = new Animator(lSprites,30, this);
		rAnimator = new Animator(rSprites,30, this);
		
		loadImages();
		
		this.currentSprite = lSprites.get(0);
		
		this.setVelX((byte)5);
		
		this.dir = Direction.Left;

		Game.handler.add(this);
	}
	
	public void tick(){
		this.setWidth((byte) currentSprite.getWidth());
		this.setHeight((byte) currentSprite.getHeight());
		
		System.out.println("X:" +this.x);
		System.out.println("Y:" +this.y);
		
		if(this.dir == Direction.Left){
			lAnimator.animate();
		}else{
			rAnimator.animate();
		}
		
		if(x <= 800){
			this.dir = Direction.Right;
			this.setVelX((byte) 5);
		}
		if(x >= 1600){
			this.dir = Direction.Left;
			this.setVelX((byte) -5);
		}
		
		x+=velX;
		y+=velY;
		
	}
	
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(this.currentSprite, this.x, this.y, null);
		
		g2.setColor(Color.RED);
		
		if(Game.borders)
			g2.draw(getBounds());
	}

	private void loadImages(){

		for(int i = 0; i<6; i++){
			try{
				lSprites.add(ImageIO.read(new File("resources/img/sprites/enemies/enemy0/l/el"+i+".png")));
				rSprites.add(ImageIO.read(new File("resources/img/sprites/enemies/enemy0/r/er"+i+".png")));
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x,y,getWidth(),getHeight());
	}
}
