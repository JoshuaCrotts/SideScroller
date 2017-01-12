package com.sidescroller.blocks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sidescroller.main.Game;
import com.sidescroller.main.GameObject;
import com.sidescroller.main.ID;
import com.sidescroller.main.Player;

public class Block extends GameObject {

	public static boolean drawBounds;
	
	private boolean collision = false;

	private Player p = Game.player;

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
		if (p.getY() + p.getHeight() + p.getVelY() >= this.y && Game.handler.sameX_Range(this, p) && Game.handler.sameY_Range(this, p)) {
			collision = true;
			
			// Collision
			p.grounded = true;

			// Can't be falling or jumping = not airborne
			p.falling = false;
			p.jumping = false;
			p.airborne = false;
			p.blockBelow = true;

			// Setup for next jump
			p.canJump = true;
			p.setTime(0);
			p.standingVerticalValue = (short) (this.y - p.getHeight());
		}
		else{
			collision = false;
		}
	}

	@Override
	public void render(Graphics g) {
		if (!withinRenderField())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);
		if (Game.borders) {
			if (collision){
				g2.setStroke(new BasicStroke(5));
				g2.setColor(Color.BLUE);
			}
			else{
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.RED);
			}
			
			g2.draw(getBounds());
		}

	}

	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), 32, 32);
	}
}