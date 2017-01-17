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

	// For debugging
	private boolean collision = false;

	private Player p = Game.player;

	public Block(short x, short y, String fileLocation) {
		super(x, y, ID.Block);

		try {
			super.currentSprite = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			System.err.println("Error! Could not load in Block Image");
		}

		this.setWidth((byte) 32);
		this.setHeight((byte) 32);
	}

	@Override
	public void tick() {

		testForCollisionWithPlayer();

	}

	private void testForCollisionWithPlayer() {
		collision = false;
		testTopOfBlockCollision();
		testLeftOfBlockCollision();
		testRightOfBlockCollision();
		testBottomOfBlockCollision();
	}

	private void testTopOfBlockCollision() {
		if (p.getY() + p.getHeight() + p.getVelY() + 1 >= this.y && p.getY() < this.y
				&& Game.handler.sameX_Range(this, p, false) && Game.handler.sameY_Range(this, p, true)) {
			collision = true;

			// Set states
			p.falling = false;
			p.jumping = false;
			p.airborne = false;
			p.setTime(0);

			p.belowCollision = true;
			p.belowCollisionYValue = (short) (this.y - p.getHeight() - 1);
		}
	}

	private void testBottomOfBlockCollision() {
		if (p.getY() + p.getVelY() <= this.y + this.getHeight() && this.y < p.getY()
				&& Game.handler.sameX_Range(this, p, false) && Game.handler.sameY_Range(this, p, true)) {
			collision = true;
			p.falling = true;
			p.jumping = false;

			p.aboveCollisionYValue = (short) (this.y + this.getHeight() + 1);
			p.aboveCollision = true;
		}
	}

	private void testRightOfBlockCollision() {
		if (this.x == 0 && this.y == 544) {
			System.out.println("\nSame X:" + Game.handler.sameX_Range(this, p, true));
			System.out.println("Same Y: " + Game.handler.sameY_Range(this, p, true));
		}
		if (p.getX() + p.getVelX() - 1 <= this.x + this.getWidth() && p.getX() > this.x
				&& Game.handler.sameX_Range(this, p, true) && Game.handler.sameY_Range(this, p, false)) {
			// When true, box is outlined
			collision = true;

			p.leftCollisionXValue = (short) (this.x + this.getWidth() + 1);
			p.leftCollision = true;
		}
	}

	private void testLeftOfBlockCollision() {
		if (p.getX() + p.getWidth() + p.getVelX() + 1 >= this.x && p.getX() < this.x
				&& Game.handler.sameX_Range(this, p, true) && Game.handler.sameY_Range(this, p, false)) {

			// When true, box is outlined
			collision = true;

			p.rightCollisionXValue = (short) (this.x - p.getWidth() - 1);
			p.rightCollision = true;
		}
	}

	@Override
	public void render(Graphics g) {
		if (!withinRenderField())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);
		if (Game.borders) {
			if (collision) {
				g2.setStroke(new BasicStroke(5));
				g2.setColor(Color.BLUE);
			} else {
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.YELLOW);
			}

			g2.draw(getBounds());
		}

		// this.tick();

	}

	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), 32, 32);
	}
}