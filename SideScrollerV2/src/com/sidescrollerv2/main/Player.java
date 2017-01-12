package com.sidescrollerv2.main;

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
	int[] isCollision = { 0, 0, 0, 0 };

	private boolean jumping = false;
	private boolean falling = false;
	private boolean attacking = false;

	private boolean right, left;
	private boolean canRun = true;

	private String lastDirection;

	// Sprites
	public static BufferedImage stillSprite; // Standing still

	private ArrayList<BufferedImage> rSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> lSprites = new ArrayList<BufferedImage>();

	// Animators
	private Animator rAnimator;
	private Animator lAnimator;

	// Physics
	private byte velyInit = 10;
	private double accel = .5;
	private double time = 0;

	private byte hVel = 5;

	public Player(short x, short y) {
		super(x,y, ID.Player);

		this.loadSprites();
		this.rAnimator = new Animator(rSprites, (byte)30, this);
		this.lAnimator = new Animator(lSprites, (byte)30, this);
		this.lastDirection = "right";
		this.stillSprite = rSprites.get(0);
		this.currentSprite = stillSprite;

		super.setWidth((byte) currentSprite.getWidth());
		super.setHeight((byte) currentSprite.getHeight());

		Game.handler.add(this);
	}

	public void tick() {

		System.out.println(super.getVelX());
		System.out.println(super.getVelY());

		if (lastDirection.equals("left")) {
			stillSprite = lSprites.get(0);
		} else if (lastDirection.equals("right")) {
			stillSprite = rSprites.get(0);
		}

		//This will determine if the player can run or not.
		if (left) {
			lAnimator.animate();
			this.setVelX((short)(-this.hVel));
			lastDirection = "left";
		} else if (right) {
			System.out.println("does this happen");
			rAnimator.animate();
			this.setVelX((short)(this.hVel));
			lastDirection = "right";
		}

		if (attacking) {
			currentSprite = stillSprite;
			new Bullet((short)(this.getX() + stillSprite.getWidth()), (short)(this.getY() + stillSprite.getHeight() / 2));
			attacking = false;
		}

		if (jumping) { // This probably needs to go in the counter.
			time++;
			this.setVelY((short) -(velyInit - (accel * time)));
		}

		else if (falling) {
			time++;
			this.setVelY((short) (accel * time));
		}

		//All of your collision stuff is the same.
		int[] collisions = testForCollisions(Game.handler.getEntities());

		if (collisions[0] == 1 || collisions[1] == 1) { // Left or right
			// collisions
			this.setVelX((short) 0);
			canRun = false;
		}

		if (collisions[2] == 1 || collisions[3] == 1) { // Top or bottom
			// collisions
			this.setVelX((short) 0);
			time = 0;
			if (collisions[2] == 1) {
				falling = true;
				jumping = false;
			}
			if (collisions[3] == 1){
				jumping = false;

			}
		}

		//If nothing below, start falling
		if (collisions[3] == 0 && jumping == false){
			falling = true;
		}

		//Print out collisions array
		for (byte i = 0; i < collisions.length; i++) {
			if (i == 0) {
				System.out.println();
			}
			System.out.println("Index " + i + " is: " + collisions[i]);
		}

		this.setX((short) (this.getX() + this.getVelX()));
		this.setY((short) (this.getY() + this.getVelY()));

	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (!isMoving()) {
			g2.drawImage(stillSprite, this.getX(), this.getY(), null);
		} else {
			g2.setColor(Color.ORANGE);
			g2.fillRect(this.getX(), this.getY(), 64, 64);
			g2.drawImage(this.currentSprite, this.getX(), this.getY(), null);
		}
		g2.setColor(Color.RED);
		g2.draw(getBounds());
	}

	private int[] testForCollisions(ArrayList<GameObject> arrayList) {

		// Reset array.
		for (int i = 0; i < isCollision.length; i++) {
			isCollision[i] = 0;
		}

		// Test for collisions with each object
		for (int i = 0; i < Game.blockHandler.getBlocks().size(); i++) {

			GameObject tempObj = Game.blockHandler.getBlocks().get(i);

			// Obviously there will be a collision with the player's self.
			if (tempObj.getID() == ID.Player) {
				continue;
			}

			// If there will be a collision
			if (Game.handler.sameX_Range(this, tempObj) && Game.handler.sameY_Range(this, tempObj)) {

				// Tests x's will intersect and are in the same y range (Left)
				if ((this.getX() + this.getVelX()) <= (tempObj.getX() + tempObj.getWidth())) {
					isCollision[0] = 1;
				}

				// Tests x's will intersect and are in the same y range (Right)
				if ((this.getX() + this.getWidth() + this.getVelX()) >= tempObj.getX()) {
					isCollision[1] = 1;
				}

				// Tests y's will intersect and are in the same x range (Above)
				if ((this.getY() + this.getVelY()) <= tempObj.getY() + tempObj.getHeight()) {
					isCollision[2] = 1;
				}

				// Tests y's will intersect and are in the same x range (Below)
				if ((this.getY() + this.getHeight() + this.getVelY()) >= tempObj.getY()) {
					isCollision[3] = 1;
					this.setY((short)(tempObj.getY() - this.getHeight()));
					System.out.println("Does this???");
				}
			}
		}
		return isCollision;
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
		}

		if (keyCode == KeyEvent.VK_D) {
			right = true;
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

			this.setVelX((short) 0);
		}
		if (keyCode == KeyEvent.VK_D) {
			right = false;
			this.setVelX((short) 0);
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

	public void setVelyInit(byte velyInit) {
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

	public Rectangle getBounds() {
		return new Rectangle(this.getX(), this.getY(), currentSprite.getWidth(), currentSprite.getHeight());
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