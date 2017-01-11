package com.joshuacrotts.sidescroller.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Player extends GameObject implements KeyListener {

	// {left, right, above, below} relative to player
	int[] collisions = { 0, 0, 0, 0 };

	// Size of player
	public static int width = 64;
	public static int height = 64;

	// Are certain actions being performed?
	private boolean jumping = false;
	private boolean falling = false;
	private boolean attacking = false;

	private boolean right, left;
	private String lastDirection;

	// Sprites
	public static BufferedImage currentSprite;
	public static BufferedImage stillSprite; // Standing still
	private ArrayList<BufferedImage> rSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> lSprites = new ArrayList<BufferedImage>();

	// Animators
	private Animator rAnimator;
	private Animator lAnimator;

	// Physics
	private int velyInit = 10;
	private double accel = .5;
	private double time = 0;

	private int hVel = 0;

	private Handler handler;

	public Player(int x, int y, ID id, Handler handler) {
		super();
		super.setId(id);

		this.x = x;
		this.y = y;

		this.loadSprites();
		this.rAnimator = new Animator(rSprites, 30, this);
		this.lAnimator = new Animator(lSprites, 30, this);
		this.lastDirection = "right";
		stillSprite = rSprites.get(0);
		currentSprite = stillSprite;

		this.handler = handler;

		handler.add(this);
	}

	public void tick() {

		// Moving left and right sprite
		if (lastDirection.equals("left")) {
			stillSprite = lSprites.get(0);
		} else if (lastDirection.equals("right")) {
			stillSprite = rSprites.get(0);
		}

		// Running
		if (left) {
			lAnimator.animate();
			velX = -hVel;
			lastDirection = "left";
		} else if (right) {
			rAnimator.animate();
			velX = hVel;
			lastDirection = "right";
		}

		// Airborne
		if (jumping) {
			time++;
			velY = (int) -(velyInit - (accel * time));
		} else if (falling) {
			time++;
			velY = (int) (accel * time);
		}

		// Attacking
		if (attacking) {
			currentSprite = stillSprite;
			new Bullet(x + stillSprite.getWidth(), y + stillSprite.getHeight() / 2, handler);
			attacking = false;
		}

		handleCollisions();

		this.x += velX;
		this.y += velY;

	}

	private void handleCollisions() {

		/**
		 * At this point, velY and velX should be all set Now all we have to do
		 * is make sure that they are safe to execute. If there will be a
		 * collision, we must prevent it.
		 * 
		 * @author Brandon Willis
		 */

		resetCollisionsArray();

		// Test for collisions with each object
		for (int i = 0; i < handler.getEntities().size(); i++) {

			GameObject tempObj = handler.getEntities().get(i);

			// Obviously there will be a collision with the player's self.
			if (tempObj.id == ID.Player) {
				continue;
			}

			// If there will be a collision
			if (handler.sameX_Range(this, tempObj) && handler.sameY_Range(this, tempObj)) {

				// Tests x's will intersect and are in the same y range (Left)
				if ((this.x + velX - Handler.pad) <= (tempObj.getX() + tempObj.getWidth() + Handler.pad)
						&& handler.sameY_Range(this, tempObj)) {
					System.out.println("\nLeft collisions\nThis: " + (this.x + velX - Handler.pad) + " is less than: "
							+ (tempObj.getX() + tempObj.getWidth()));
					collisions[0] = 1;
				}

				// Tests x's will intersect and are in the same y range (Right)
				if ((this.x + this.getWidth() + velX + Handler.pad) >= tempObj.getX() - Handler.pad
						&& handler.sameY_Range(this, tempObj)) {
					collisions[1] = 1;
				}

				// Tests y's will intersect and are in the same x range (Above)
				if ((this.y + velY - Handler.pad) <= tempObj.getY() + tempObj.getHeight() + Handler.pad
						&& handler.sameX_Range(this, tempObj)) {
					collisions[2] = 1;

					System.out.println("\nAbove collision");
					System.out.println("X: " + tempObj.getX() + " Y: " + tempObj.getY());
				}

				// Tests y's will intersect and are in the same x range (Below)
				if ((this.y + this.getHeight() + velY + Handler.pad) >= tempObj.getY() - Handler.pad
						&& handler.sameX_Range(this, tempObj)) {
					collisions[3] = 1;
				}
			}
		}

		// HANDLING COLLISIONS
		// ---------------------

		// Left or right collisions
		if (collisions[0] == 1 || collisions[1] == 1) {
			velX = 0;
			currentSprite = stillSprite;
		}

		// Top or bottom collisions
		if (collisions[2] == 1 || collisions[3] == 1) {
			velY = 0;
			time = 0;
			if (collisions[2] == 1) {
				falling = true;
				jumping = false;
			}
			if (collisions[3] == 1) {
				jumping = false;
				falling = false;
			}
		}

		// If nothing below, start falling
		if (collisions[3] == 0 && jumping == false) {
			falling = true;
		}

		printCollisionsArray();
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (!isMoving()) {
			g2.drawImage(stillSprite, x, y, null);
		} else {
			g2.drawImage(super.currentSprite, x, y, null);
		}
		g2.setColor(Color.RED);
		g2.draw(getBounds());
		g2.draw(getBoundsTop());
	}

	private void resetCollisionsArray() {
		for (int i = 0; i < collisions.length; i++) {
			collisions[i] = 0;
		}
	}

	private void printCollisionsArray() {
		// Print out collisions array
		for (int i = 0; i < collisions.length; i++) {
			if (i == 0) {
				System.out.println("\nPlayer y-velocity: " + this.velY);
			}
			System.out.println("Index " + i + " is: " + collisions[i]);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_W) {

			if (jumping)
				return;
			else
				jumping = true;
		}

		if (keyCode == KeyEvent.VK_A) {
			left = true;
			// levels[0].setX(levels[0].getX() -14);
		}

		if (keyCode == KeyEvent.VK_D) {
			right = true;
			// levels[0].setX(levels[0].getX() +14);
		}

		if (keyCode == KeyEvent.VK_SPACE) {
			if (attacking)
				return;
			attacking = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_A) {
			left = false;

			velX = 0;
		}
		if (keyCode == KeyEvent.VK_D) {
			right = false;
			velX = 0;
		}

		if (keyCode == KeyEvent.VK_SPACE) {
			attacking = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	private void loadSprites() {
		for (int i = 0; i < 6; i++) {
			try {
				rSprites.add(ImageIO.read(new File("img/sprites/p/r/r" + i + ".png")));
				lSprites.add(ImageIO.read(new File("img/sprites/p/l/l" + i + ".png")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public boolean isFalling() {
		return falling;
	}

	public void setFalling(boolean falling) {
		this.falling = falling;
	}

	public int getVelyInit() {
		return velyInit;
	}

	public void setVelyInit(int velyInit) {
		this.velyInit = velyInit;
	}

	public double getAccel() {
		return accel;
	}

	public void setAccel(int accel) {
		this.accel = accel;
	}

	public double getTime() {
		return time;
	}

	public void setT(double t) {
		this.time = t;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, currentSprite.getWidth(), currentSprite.getHeight());
	}

	public Rectangle getBoundsTop() {
		return new Rectangle(x, y, currentSprite.getWidth(), 1);
	}

	public boolean isMoving() {
		return left || right;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public boolean goingLeft() {
		return left;
	}

	public boolean goingRight() {
		return right;
	}

	public String getLastDirection() {
		return lastDirection;
	}

}
