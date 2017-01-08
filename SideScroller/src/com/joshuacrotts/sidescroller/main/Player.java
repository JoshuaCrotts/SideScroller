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
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Player extends GameObject implements KeyListener {

	// {left, right, above, below} relative to player
	int[] isCollision = { 0, 0, 0, 0 };

	public static int x;
	private static int y;

	private double velX;
	private double velY;

	private int width;
	private int height;

	private boolean jumping = false;
	private boolean falling = false;
	public static boolean attacking = false;
	private boolean right, left;
	private String lastDirection;

	// Sprites
	public static BufferedImage currentSprite;
	public static BufferedImage stillSprite; //Standing still
	private ArrayList<BufferedImage> rSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> lSprites = new ArrayList<BufferedImage>();

	// Animators
	private Animator rAnimator;
	private Animator lAnimator;

	// Physics
	private int velyInit = 4;
	private double accel = .1;
	private double t = 0;

	private int hVel = 5;

	private double timer = -15.8;

	private Handler handler;
	private Game game;

	private Level[] levels;

	public Player(int x, int y, ID id, Handler handler, Game game, Level[] levels) {
		super();
		this.x = x;
		this.y = y;
		super.setId(id);

		this.loadSprites();
		this.rAnimator = new Animator(rSprites, 30, this);
		this.lAnimator = new Animator(lSprites, 30, this);
		this.lastDirection = "right";
		this.stillSprite = rSprites.get(0);
		this.currentSprite = stillSprite;

		this.levels = levels;

		this.handler = handler;
		this.game = game;

		handler.add(this);
	}

	public void tick() {

		if (lastDirection.equals("left")) {
			this.stillSprite = lSprites.get(0);
		} else if (lastDirection.equals("right")) {
			this.stillSprite = rSprites.get(0);
		}

		if (left) {
			lAnimator.animate();
			velX = -hVel;
			lastDirection = "left";
		} else if (right) {
			rAnimator.animate();
			velX = hVel;
			lastDirection = "right";
		}

		if (attacking) {
			currentSprite = stillSprite;
			new Bullet(x + stillSprite.getWidth(), y + stillSprite.getHeight() / 2, handler, game, this);
			attacking = false;
		}

		if (jumping) { // This probably needs to go in the counter.
			t++;
			velY = -(velyInit - (accel * t));
			velY *= -1;

		}

		if (falling) {
			if (y < Game.HEIGHT - (currentSprite.getHeight() + (currentSprite.getHeight() / 2))) {
				velY = Math.abs(timer);
				timer -= .6;
			} else {
				velY = 0;
				timer = -15.8;
				jumping = false;
				falling = false;
				this.y = Game.HEIGHT - (currentSprite.getHeight() + (currentSprite.getHeight() / 2));
			}
		}

		int[] collisions = testForCollisions(handler.getEntities());
		
		if (collisions[0] == 1 || collisions[1] == 1){ //Left or right collisions
			velX = 0;
		}
		if (collisions[2] == 1 || collisions[3] == 1){ //Top or bottom collisions
			velY = 0;
		}

		// System.out.println(x);
		this.x += velX;
		this.y += velY;

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

	private int[] testForCollisions(ArrayList<GameObject> arrayList) {

		for (int i : isCollision) { // reset isCollision array
			i = 0;
		}

		// Test for collisions with each object
		for (int i = 0; i < handler.getEntities().size(); i++) {

			GameObject tempObj = handler.getEntities().get(i);

			// Tests x's will intersect and are in the same y range (Right)
			if ((this.x + velX) <= (tempObj.getX() + tempObj.getWidth()) && handler.sameY_Range(this, tempObj)) {
				isCollision[1] = 1;
			}

			// Tests x's will intersect and are in the same y range (Right)
			if ((this.x + velX) >= tempObj.getX() && handler.sameY_Range(this, tempObj)) {
				isCollision[2] = 1;
			}

			// Tests y's will intersect and are in the same x range (Above)
			if ((this.y + velY) <= tempObj.getY() + tempObj.getHeight() && handler.sameX_Range(this, tempObj)) {
				isCollision[2] = 1;
			}

			// Tests y's will intersect and are in the same x range (Below)
			if ((this.y + velY) >= tempObj.getY() && handler.sameX_Range(this, tempObj)) {
				isCollision[3] = 1;
				
				t = 0; //Unique. If something is below, this will stop the jump method.
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
		// TODO Auto-generated method stub

	}

	private void loadSprites() {
		for (int i = 0; i < 6; i++) {
			try {
				rSprites.add(ImageIO.read(new File("img/sprites/p/r/r" + i + ".png")));
				lSprites.add(ImageIO.read(new File("img/sprites/p/l/l" + i + ".png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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

	public double getT() {
		return t;
	}

	public void setT(double t) {
		this.t = t;
	}

	public double getTimer() {
		return timer;
	}

	public void setTimer(double timer) {
		this.timer = timer;
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return new Rectangle(x, y, currentSprite.getWidth(), currentSprite.getHeight());

	}

	public Rectangle getBoundsTop() {
		return new Rectangle(x, y, currentSprite.getWidth(), 1);
	}

	public boolean isMoving() {
		return left || right;
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
