package com.joshuacrotts.sidescroller.main;

import java.awt.BasicStroke;
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

	public int x;
	private int y;

	private double velX;
	private double velY;

	public static int width = 64;
	public static int height = 64;

	private boolean jumping = false;
	private boolean falling = false;
	// Recommend this be changed to private and not static. Doesn't make sense.
	public static boolean attacking = false;
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
	private int velyInit = 16;
	private double accel = .3;
	private double time = 0;

	private int hVel = 5;

	private double timer = -15.8;

	private Handler handler;
	private Game game;
	
	private Graphics g;

	public Player(int x, int y, ID id, Handler handler, Game game) {
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
		this.game = game;

		handler.add(this);
	}

	public void tick() {

		if (lastDirection.equals("left")) {
			stillSprite = lSprites.get(0);
		} else if (lastDirection.equals("right")) {
			stillSprite = rSprites.get(0);
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
			time++;
			velY = -(velyInit - (accel * time));
		}

		else if (falling) {
			time++;
			velY = accel * time;
		}

		int[] collisions = testForCollisions(handler.getEntities());

		if (collisions[0] == 1 || collisions[1] == 1) { // Left or right
														// collisions
			velX = 0;
		}
		
		if (collisions[2] == 1 || collisions[3] == 1) { // Top or bottom
														// collisions
			velY = 0;
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
		for (int i = 0; i < collisions.length; i++) {
			if (i == 0) {
				System.out.println();
			}
			System.out.println("Index " + i + " is: " + collisions[i]);
		}

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

		// Reset array.
		for (int i = 0; i < isCollision.length; i++) {
			isCollision[i] = 0;
		}
		
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
				if ((this.x + velX) <= (tempObj.getX() + tempObj.getWidth())) {
					isCollision[0] = 1;
				}

				// Tests x's will intersect and are in the same y range (Right)
				if ((this.x + this.getWidth() + velX) >= tempObj.getX()) {
					isCollision[1] = 1;
				}

				// Tests y's will intersect and are in the same x range (Above)
				if ((this.y + velY) <= tempObj.getY() + tempObj.getHeight()) {
					isCollision[2] = 1;
				}

				// Tests y's will intersect and are in the same x range (Below)
				if ((this.y + this.getHeight() + velY) >= tempObj.getY()) {
					isCollision[3] = 1;
				}
				
				boolean allFourSidesCollisions = false;
				
				for (int a : isCollision){
					if (a == 0){
						allFourSidesCollisions = false;
					}
				}
				if (allFourSidesCollisions){
					System.out.println("NOT GOOD!");
				}
			}
		}
		return isCollision;
	}
	//DOESN'T WORK
//		private int[] testForNewCollisions(ArrayList<GameObject> objects){
//			// Reset array.
//			
//			Graphics2D g2 = (Graphics2D) g;
//			
//			for (int i = 0; i < isCollision.length; i++) {
//				isCollision[i] = 0;
//			}
//			
//			// Test for collisions with each object
//			for (int i = 0; i < handler.getEntities().size(); i++) {
//	
//				GameObject tempObj = handler.getEntities().get(i);
//	
//				// Obviously there will be a collision with the player's self.
//				if (tempObj.id == ID.Player) {
//					continue;
//				}
//				
//				
//				
//				if(tempObj.id == ID.Block){
//					 if(getBoundsLeft().intersects(tempObj.getBoundsRight())){
//						isCollision[0] = 1;
//						System.out.println("A fucking collision");
//						
//					}
//					
//					 if(getBoundsRight().intersects(tempObj.getBoundsLeft())){
//						isCollision[1] = 1;
//						
//					}
//					
//					 if(getBoundsTop().intersects(tempObj.getBoundsBottom())){
//						isCollision[2] = 1;
//					}
//					
//					 if(getBoundsBottom().intersects(tempObj.getBoundsTop())){
//						
//						isCollision[3] = 1;
//					}
//				}
//			}
//			
//			return isCollision;
//		}

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

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
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

	public double getTimer() {
		return timer;
	}

	public void setTimer(double timer) {
		this.timer = timer;
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
