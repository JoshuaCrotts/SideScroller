package com.sidescroller.main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GUI {

	private BufferedImage life;
	
	public GUI(){
		try {
			this.life = ImageIO.read(new File("resources/img/sprites/items/lifeblue.png"));
		} catch (IOException e) {
			System.err.println("Error! Could not find LIFE BLUE image");
			e.printStackTrace();
		}
	}
	
	public void tick(){
		
	}
	
	public void render(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		short x = 32;
		
		for(int i = 0; i<Player.health; i++){
			g2.drawImage(this.life, x, 32, null);
			x+=32;
		}
	}
}
