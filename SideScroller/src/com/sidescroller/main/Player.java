package com.sidescroller.main;

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

import com.sidescroller.main.Bullet;
import com.sidescroller.main.Game.State;

public class Player extends GameObject implements KeyListener {

	

	// Sprites
	private BufferedImage stillSprite; // Standing still

	private ArrayList<BufferedImage> dSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> rSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> lSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> uRSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> uLSprites = new ArrayList<BufferedImage>();

	// Animators
	private Animator rAnimator;
	private Animator lAnimator;
	private Animator uRAnimator;
	private Animator uLAnimator;

	// Constants
	private static final short MAX_VELY = 100;
	private static final short MAX_VELX = 5;
	private static final short RUNNINGSPEED = 5;

	// Key codes
	private boolean rightKeyDown = false;
	private boolean leftKeyDown = false;
	private boolean upKeyDown = false;

	// Player states
	private Direction playerFacing;
	public boolean airborne = true;
	public boolean falling = true;
	public boolean jumping = false;
	public boolean movingHorizontal = false;
	public boolean attacking = false;
	public boolean down = false;

	// Physics (jumping and falling)
	private int velyInit = -10;
	private double acclerationOfGravity = .5;
	private double acceleration = 0;
	private double time = 0;

	// Collision values
	public short belowCollisionYValue = 0;
	public short aboveCollisionYValue = 0;
	public short leftCollisionXValue = 0;
	public short rightCollisionXValue = 0;

	// When above collision, make it appear perfect
	public boolean aboveCollision = false;
	public boolean belowCollision = false;
	public boolean rightCollision = false;
	public boolean leftCollision = false;
	
	//Actual game states (health, powerup, etc)
	public static int health = 3;
	public static int[] powerUps = {0,1,2,3,4,5};

	public Player(short x, short y) {
		super(x, y, ID.Player);

		this.loadSprites();

		this.rAnimator = new Animator(rSprites, (byte) 30, this);
		this.lAnimator = new Animator(lSprites, (byte) 30, this);
		this.uRAnimator = new Animator(uRSprites, (byte) 30, this);
		this.uLAnimator = new Animator(uLSprites, (byte) 30, this);

		this.playerFacing = Direction.Right;
		this.stillSprite = rSprites.get(0);
		this.currentSprite = stillSprite;

		super.setWidth((byte) currentSprite.getWidth());
		super.setHeight((byte) currentSprite.getHeight());

		Game.handler.add(this);
	}

	public void tick() {

		if (playerFacing == Direction.Left) {
			stillSprite = lSprites.get(0);
		} else if (playerFacing == Direction.Right) {
			stillSprite = rSprites.get(0);
		}

		setVelocities();

		/**
		 * 
		 * The reason I've decided to make the first thing I do is setting the
		 * velocities is so that the velocities will only be added after it is
		 * checked with every object that there will not be a collision
		 * 
		 * @author Brandon Willis
		 */

		// Begin Physics
		configureStates();

		if (airborne) {
			// Determines which sprites to use when jumping
			if (playerFacing == Direction.Right)
				uRAnimator.animate();
			if (playerFacing == Direction.Left)
				uLAnimator.animate();

			acceleration = acclerationOfGravity;
			time++;

			if (falling) {
				this.velY = (short) (acceleration * time);
			} else if (jumping) {
				velY = ((short) (velyInit + (acceleration * time)));
			}

		} else if (belowCollision) {
			// Turn off gravity and reset time.
			jumping = false;
			time = 0;
		}

		if (attacking) {
			this.currentSprite = stillSprite;
			new Bullet((short) (this.getX() + stillSprite.getWidth()-35),
					(short) ((this.getY() + stillSprite.getHeight() / 2)-15));
			attacking = false;
		}

		// Horizontal stuff
		if (movingHorizontal) {
			if (rightKeyDown && !rightCollision) {
				velX += RUNNINGSPEED;
			}
			if (leftKeyDown && !leftCollision) {
				velX -= RUNNINGSPEED;
			}
		} else {
			velX = 0;
		}

		// Player Direction
		if (this.velX > 0) {
			playerFacing = Direction.Right;
			if (!airborne)
				rAnimator.animate();

		} else if (this.velX < 0) {
			playerFacing = Direction.Left;
			if (!airborne)
				lAnimator.animate();
		}

		else {
			this.currentSprite = stillSprite;
		}

		//Crap for player lying down
//		if(down){
//			if(this.getDirection() == Direction.Left)
//				this.currentSprite = dSprites.get(0);
//			else
//				this.currentSprite = dSprites.get(1);
//		}

		// Will loop through every block between player ticks.
		this.belowCollision = false;
		this.aboveCollision = false;
		this.leftCollision = false;
		this.rightCollision = false;
	}

	private void setVelocities() {
		limitVelocities();

		setXCoordinate();

		setYCoordinate();

		velX = 0;
		velY = 0;

	}

	private void setXCoordinate() {

		if (leftCollision) {
			this.x = leftCollisionXValue;
		} else if (rightCollision) {
			this.x = rightCollisionXValue;
		} else {
			this.x += this.velX;
		}
		//System.out.println("player.x = " + this.x + " left Collision: " + leftCollision);
	}

	private void setYCoordinate() {
		if (belowCollision) {
			this.y = belowCollisionYValue;
		} else if (aboveCollision) {
			this.y = aboveCollisionYValue;
		} else {
			this.y += velY;
		}
	}

	private void limitVelocities() {
		if (Math.abs(this.velX) >= MAX_VELX) {
			this.velX = (short) (MAX_VELX * (this.velX / Math.abs(this.velX)));
		}
		if (Math.abs(this.velY) >= MAX_VELY) {
			this.velY = (short) (MAX_VELY * (this.velY / Math.abs(this.velX)));
		}
	}

	private void configureStates() {

		if (belowCollision && upKeyDown) {
			jumping = true;
		}

		if (!belowCollision && !jumping) {
			falling = true;
		}

		if (jumping || falling) {
			airborne = true;
			belowCollision = false;
		}

		if (this.leftKeyDown || this.rightKeyDown) {
			this.movingHorizontal = true;
		} else {
			this.movingHorizontal = false;
		}
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (!isMoving()) {
			g2.drawImage(stillSprite, this.getX(), this.getY(), null);
		} else {
			g2.drawImage(this.currentSprite, this.getX(), this.getY(), null);
		}

		if (Game.borders) {
			g2.setColor(Color.RED);
			g2.draw(getBounds());
		}
	}

	private boolean isMoving() {
		if (velX == 0 && velY == 0) {
			return false;
		}
		return true;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		// Movement keys and attacking keys
		switch (keyCode) {
		case KeyEvent.VK_W:
			upKeyDown = true;
			break;
		case KeyEvent.VK_S:
			down = true;
			break;
		case KeyEvent.VK_A:
			leftKeyDown = true;
			break;
		case KeyEvent.VK_D:
			rightKeyDown = true;
			break;
		case KeyEvent.VK_SPACE:
			attacking = true;
			break;
		}

		// Draws the Debug screen
		if (keyCode == KeyEvent.VK_X) {
			if (!Game.debug) {
				Game.debug = true;
				return;
			} else {
				Game.debug = false;
				return;
			}
		}

		// Draws the borders
		if (keyCode == KeyEvent.VK_Z) {
			if (!Game.borders) {
				Game.borders = true;
				return;
			} else {
				Game.borders = false;
				return;
			}
		}
		// Pauses the game
		if(keyCode == KeyEvent.VK_P){
			if(Game.gameState != State.Paused && Game.gameState == State.Game){
				Game.gameState = State.Paused;
				return;
			}else{
				Game.gameState = State.Game;
				return;
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch (keyCode) {

		case KeyEvent.VK_W:
			upKeyDown = false;
			break;

		case KeyEvent.VK_S:
			down = false;
			break;


			// Left and right are special cases
		case KeyEvent.VK_A:
			leftKeyDown = false;
			velX += RUNNINGSPEED;
			break;
		case KeyEvent.VK_D:
			rightKeyDown = false;
			velX -= RUNNINGSPEED;
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	private void loadSprites() {
		for (int i = 0; i < 6; i++) {
			try {
				rSprites.add(ImageIO.read(new File("resources/img/sprites/p/r/r" + i + ".png")));
				lSprites.add(ImageIO.read(new File("resources/img/sprites/p/l/l" + i + ".png")));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < 4; i++) {
			try {
				uRSprites.add(ImageIO.read(new File("resources/img/sprites/p/ur/u" + i + ".png")));
				uLSprites.add(ImageIO.read(new File("resources/img/sprites/p/ul/u" + i + ".png")));
			} catch (IOException e) {
				System.err.println("Error! Could not load UP images");
			}
		}

		try{
			dSprites.add(ImageIO.read(new File("resources/img/sprites/p/l/down0.png")));
			dSprites.add(ImageIO.read(new File("resources/img/sprites/p/r/down0.png")));
		}catch(IOException e){
			System.err.println("Error! Could not load DOWN images");
			e.printStackTrace();
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

	public double getTime() {
		return time;
	}

	public void setT(double t) {
		this.time = t;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.getX(), this.getY(), currentSprite.getWidth(), currentSprite.getHeight());
	}

	public Direction getDirection() {
		return playerFacing;
	}

	public void setTime(int i) {
		this.time = i;
	}
}