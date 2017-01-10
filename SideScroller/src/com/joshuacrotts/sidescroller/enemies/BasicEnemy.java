package com.joshuacrotts.sidescroller.enemies;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.joshuacrotts.sidescroller.main.Animator;
import com.joshuacrotts.sidescroller.main.Bullet;
import com.joshuacrotts.sidescroller.main.Game;
import com.joshuacrotts.sidescroller.main.GameObject;
import com.joshuacrotts.sidescroller.main.Handler;
import com.joshuacrotts.sidescroller.main.ID;
import com.joshuacrotts.sidescroller.main.Player;

public class BasicEnemy extends Enemy {

	private int strength;
	private int health;

	// Movement operations
	private boolean moving = true;
	private boolean attacking = false;
	private int timer = 0;

	// Movement Frames and Animators
	private ArrayList<BufferedImage> rSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> lSprites = new ArrayList<BufferedImage>();
	private Animator rAnimator;
	private Animator lAnimator;
	public BufferedImage stillSprite;
	public BufferedImage currentSprite;

	private double velX = 5;
	private double velY = 5;
	
	private int width;
	private int height;

	private String lastDirection;

	private Handler handler;
	private Game game;
	
	private boolean jumping;
	private boolean falling;
	
	
	private double time = 0;
	private int velyInit = 6;
	private double accel = .3;
	private int[] isCollision = {0,0,0,0};
	

	public BasicEnemy(int x, int y, Game game, Handler handler) {
		super(x, y);
		this.lastDirection = "right";
		this.handler = handler;
		this.game = game;
		this.setId(ID.Enemy);
		this.rSprites = new ArrayList<BufferedImage>();
		this.lSprites = new ArrayList<BufferedImage>();
		this.rAnimator = new Animator(rSprites, 30, this);
		this.lAnimator = new Animator(lSprites, 30, this);
		this.loadSprites();
		this.stillSprite = this.rSprites.get(0);
		this.currentSprite = this.stillSprite;
		this.width = this.currentSprite.getWidth();
		this.height = this.currentSprite.getHeight();
		this.handler.add(this);

		this.velX = 5;
		this.velY = 5;
	}

	@Override
	public void tick() {

		System.out.println(this.velX);
		if (lastDirection.equals("left")) {
			this.stillSprite = lSprites.get(0);
		} else if (lastDirection.equals("right")) {
			this.stillSprite = rSprites.get(0);
		}
		
		//System.out.println("velx: "+super.velX);
		//System.out.println("vely "+super.velY);
		System.out.println("moving: "+moving);

		if (moving) {
			if (timer >= 400) {
				timer = 0;
			}
			if (timer > 200 && timer < 400) {
				lastDirection = "left";
				//System.out.println("left");
				super.setX(super.getX() - this.velX);
				lAnimator.animate();
			} else {

				lastDirection = "right";
				super.setX(super.getX() + this.velX);
				rAnimator.animate();
			}

			timer++;
		}
		
		if (attacking) {
			currentSprite = stillSprite;
			new Bullet((int)super.getX() + stillSprite.getWidth(), (int)super.getY() + stillSprite.getHeight() / 2, handler, game, this);
			attacking = false;
		}

		if (jumping) { // This probably needs to go in the counter.
			time+=.5;
			velY = -(velyInit - (accel * time));
		}

		else if (falling) {
			time++; //increments the timer and allows for the Player to fall
			velY = accel * time;
		}
		
		int[] collisions = testForCollisions(handler.getEntities());

		if (collisions[0] == 1 || collisions[1] == 1) { // Left or right
			// collisions
			this.velX = 0;
		}

		if (collisions[2] == 1 || collisions[3] == 1) { // Top or bottom
			// collisions
			this.velY = 0;
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

		if(collisions[2] == 1){
			velY = 0;
			time = 0;
			jumping = false;
			falling = true;
		}

		if(collisions[3] == 1){
			jumping = false;
		}

		// System.out.println("Enemy X: "+super.getX());
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(super.currentSprite, (int)super.getX(), (int)super.getY(), null);

	}
	
	private int[] testForCollisions(ArrayList<GameObject> arrayList) {

		// Reset array.
		for (int i = 0; i < isCollision.length; i++) {
			isCollision[i] = 0;
		}

		boolean allFourSidesCollisions = false;
		
		// Test for collisions with each object
		for (int i = 0; i < handler.getEntities().size(); i++) {

			GameObject tempObj = handler.getEntities().get(i);

			// Obviously there will be a collision with the enemy's self.
			if (tempObj.getId() == ID.Enemy) {
				continue;
			}

			
			// If there will be a collision
			if(tempObj.getId() == ID.Block){

				if (handler.sameX_Range(this, tempObj) && handler.sameY_Range(this, tempObj)) {

					// Tests x's will intersect and are in the same y range (Left)
					if ((this.getX() + velX) <= (tempObj.getX() + tempObj.getWidth())) {
						isCollision [0] = 1;
					}

					// Tests x's will intersect and are in the same y range (Right)
					if ((this.getX() + this.getWidth() + velX) >= tempObj.getX()) {
						isCollision[1] = 1;
					}

					// Tests y's will intersect and are in the same x range (Above)
					if ((this.getY() + velY) <= tempObj.getY() + tempObj.getHeight()) {
						isCollision[2] = 1;
					}

					// Tests y's will intersect and are in the same x range (Below)
					if ((this.getY() + this.getHeight() + velY) >= tempObj.getY()) {
						isCollision[3] = 1;
					}
				}
			}
		}
		return isCollision;
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

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle((int)super.getX(),(int)super.getY(),width,height);
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int)super.getX(), (int)super.getY(), currentSprite.getWidth(), 1);
	}
	public Rectangle getBoundsBottom(){
		return new Rectangle((int)super.getX(),(int)super.getY()+currentSprite.getHeight(),currentSprite.getWidth(), 1);
	}
	public Rectangle getBoundsLeft(){
		return new Rectangle((int)super.getX(),(int)super.getY(),1,currentSprite.getHeight());
	}
	public Rectangle getBoundsRight(){
		return new Rectangle((int)super.getX()+currentSprite.getWidth(),(int) super.getY(), 1, currentSprite.getHeight());
	}
	
}
