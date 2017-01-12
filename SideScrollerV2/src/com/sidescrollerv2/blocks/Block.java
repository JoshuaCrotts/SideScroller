package com.sidescrollerv2.blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sidescrollerv2.main.Game;
import com.sidescrollerv2.main.GameObject;
import com.sidescrollerv2.main.ID;

public class Block extends GameObject {

	public static boolean drawBounds;
	
	public Block(short x, short y, String fileLocation) {
		super(x, y, ID.Block);

		try {
			super.currentSprite = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			System.err.println("Error! Could not load in Block Image");
		}
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		if (!withinRenderField())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);
		if(Game.borders){
			g2.setColor(Color.RED);
			g2.draw(getBounds());
		}

	}

	public Rectangle getBounds() {
		return new Rectangle(super.getX(),super.getY(),32,32);
	}
}