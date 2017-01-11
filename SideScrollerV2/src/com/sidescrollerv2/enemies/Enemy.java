package com.sidescrollerv2.enemies;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.sidescrollerv2.main.GameObject;
import com.sidescrollerv2.main.ID;

public abstract class Enemy extends GameObject{
	
	public Enemy(short x, short y){
		super(x,y, ID.Enemy);
	}
	
	public Enemy(short x, short y, String fileLocation){
		super(x,y,ID.Enemy);
		
		try{
			super.currentSprite = ImageIO.read(new File(fileLocation));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		super.setWidth((byte) super.currentSprite.getWidth());
		super.setHeight((byte) super.currentSprite.getHeight());
	}
	
	public abstract void tick();

	public abstract void render(Graphics g);

}