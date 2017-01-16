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

public class Player extends GameObject implements KeyListener {

	private Direction playerFacing;

	// Sprites
	private BufferedImage stillSprite; // Standing still

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
	private static final short RUNNINGSPEED = 100;

	// Key codes
	private boolean rightKeyDown = false;
	private boolean leftKeyDown = false;
	private boolean upKeyDown = false;

	// Player states
	public boolean airborne = true;
	public boolean falling = true;
	public boolean jumping = false;
	public boolean movingHorizontal = false;
	public boolean attacking = false;
	public boolean canJump = false;

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
	public boolean rightCollision = true;
	public boolean leftCollision = true;

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

		setVelX((short) 0);
		setVelY((short) 0);

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

			this.canJump = false;
			acceleration = acclerationOfGravity;
			time++;

			if (falling) {
				this.setVelY((short) (acceleration * time));
			} else if (jumping) {
				this.setVelY((short) (velyInit + (acceleration * time)));
			}

		} else if (belowCollision) {
			// Turn off gravity and reset time.
			jumping = false;
			// acceleration = 0;
			time = 0;

			canJump = true;
			this.setY(belowCollisionYValue);
		}

		if (attacking) {
			this.currentSprite = stillSprite;
			new Bullet((short) (this.getX() + stillSprite.getWidth()),
					(short) (this.getY() + stillSprite.getHeight() / 2));
			attacking = false;
		}

		// Horizontal stuff
		if (movingHorizontal) {
			if (rightKeyDown && !rightCollision) {
				setVelX((short) (getVelX() + RUNNINGSPEED));
			}
			if (leftKeyDown && !leftCollision) {
				setVelX((short) (getVelX() - RUNNINGSPEED));
			}
		} else {
			setVelX((short) 0);
		}

		// Player Direction
		if (this.getVelX() > 0) {
			playerFacing = Direction.Right;
			if (!airborne)
				rAnimator.animate();

		} else if (this.getVelX() < 0) {
			playerFacing = Direction.Left;
			if (!airborne)
				lAnimator.animate();
		}

		else {
			this.currentSprite = stillSprite;
		}

		// Will loop through every block between player ticks.
		this.belowCollision = false;
		this.aboveCollision = false;
		this.leftCollision = false;
		this.rightCollision = false;
	}

	private void setVelocities() {

		// Limit Velocities
		if (Math.abs(this.getVelX()) >= MAX_VELX) {
			this.setVelX((short) (MAX_VELX * (this.getVelX() / Math.abs(this.getVelX()))));
		}
		if (Math.abs(this.getVelY()) >= MAX_VELY) {
			this.setVelY((short) (MAX_VELY * (this.getVelY() / Math.abs(this.getVelX()))));
		}

		this.setX((short) (this.getX() + this.getVelX()));

		// If on the ground, this value will be with the box it collided with
		if (belowCollision) {
			this.setY(belowCollisionYValue);
		} else if (aboveCollision) {
			this.setY(aboveCollisionYValue);
		} else {
			this.setY((short)(this.getY() + this.getVelY()));
		}
	}

	private void configureStates() {

		if (canJump && upKeyDown) {
			jumping = true;
			canJump = false;
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
		if (getVelX() == 0 && getVelY() == 0) {
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

		/*
		 * Keeping out until physics is perfect if (keyCode ==
		 * KeyEvent.VK_SPACE) { if (attacking) return; attacking = true; }
		 */

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch (keyCode) {

		case KeyEvent.VK_W:
			upKeyDown = false;
			break;

		// Left and right are special cases
		case KeyEvent.VK_A:
			leftKeyDown = false;
			setVelX((short) (getVelX() + RUNNINGSPEED));
			break;
		case KeyEvent.VK_D:
			rightKeyDown = false;
			setVelX((short) (getVelX() - RUNNINGSPEED));
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