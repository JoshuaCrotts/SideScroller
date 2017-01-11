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

	private int xx;
	private int yy;
	private int ww = 64;
	private int hh = 64;
	
	public Block(short x, short y, String fileLocation) {
		super(x, y, ID.Block);
		this.xx = x;
		this.yy = y;

		try {
			super.currentSprite = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			System.err.println("Error! Could not load in Block Image");
		}

		Game.handler.add(this);

	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		if (!withinRenderField())
			return;

		Graphics2D g2 = (Graphics2D) g;
		//g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);
		g2.setColor(Color.RED);
		g2.draw(getBounds());

	}

	public Rectangle getBounds() {
		return new Rectangle(xx,yy,ww,hh);
	}
}