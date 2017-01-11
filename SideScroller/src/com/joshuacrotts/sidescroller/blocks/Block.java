package com.joshuacrotts.sidescroller.blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.joshuacrotts.sidescroller.main.GameObject;
import com.joshuacrotts.sidescroller.main.Handler;
import com.joshuacrotts.sidescroller.main.ID;

public class Block extends GameObject {

	private BufferedImage sprite;
	private Handler handler;

	public Block(int x, int y, String fileLocation, Handler handler) {
		super(x, y, ID.Block);
		this.handler = handler;

		try {
			sprite = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			System.err.println("Error! Could not load in Block Image");
		}

		this.handler.add(this);

	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		if (!withinRenderField())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(this.sprite, this.getX(), this.getY(), null);
		g2.setColor(Color.RED);
		g2.draw(getBounds());

	}

	@Override
	public int getWidth() {
		return sprite.getWidth();
	}

	@Override
	public int getHeight() {
		return sprite.getHeight();
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(this.getX(), this.getY(), getWidth(), getHeight());
	}
}