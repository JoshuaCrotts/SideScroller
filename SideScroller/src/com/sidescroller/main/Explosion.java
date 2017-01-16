package com.sidescroller.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Explosion extends GameObject{

	private ArrayList<BufferedImage> images;
	private Animator expAni;
	
	private int animate;
	
	public Explosion(short x, short y, ID id){
		super(x,y,ID.Explosion);
		
		this.images = new ArrayList<BufferedImage>();
		
		try{
			
			for(int i = 1; i<5; i++)
				this.images.add(ImageIO.read(new File("resources/img/sprites/p/bullet/b"+i+".png")));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		this.expAni = new Animator(images,20,this);

		Game.handler.add(this);
	}
	
	public void tick(){
		expAni.animate();
		animate++;
		System.out.println(animate);
		if(animate >= 20){
			
			Game.handler.getEntities().remove(this);
		}
	}
	
	public void render(Graphics g){
		g.drawImage(this.currentSprite, super.x, super.y, null);
	}
	
	public Rectangle getBounds(){
		return new Rectangle(super.x,y,super.getWidth(),super.getHeight());
	}
}
