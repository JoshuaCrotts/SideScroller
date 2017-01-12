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

public class Player extends GameObject implements KeyListener {

	private Direction playerFacing;

	// Sprites
	public static BufferedImage stillSprite; // Standing still

	private ArrayList<BufferedImage> rSprites = new ArrayList<BufferedImage>();
	private ArrayList<BufferedImage> lSprites = new ArrayList<BufferedImage>();

	// Animators
	private Animator rAnimator;
	private Animator lAnimator;

	// Constants
	private static final short MAX_VELY = 100;
	private static final short MAX_VELX = 5;
	private static final short RUNNINGSPEED = 100;

	// Key codes
	private boolean rightKeyDown = false;
	private boolean leftKeyDown = false;
	private boolean upKeyDown = false;
	private boolean downKeyDown = false;

	// Player states
	public boolean grounded = false;
	public boolean airborne = true;
	public boolean falling = true;
	public boolean jumping = false;
	public boolean movingHorizontal = false;
	public boolean canJump = false;
	public boolean blockBelow = false;

	// Physics (jumping and falling)
	private int velyInit = -10;
	private double acclerationOfGravity = .5;
	private double acceleration = 0;
	private double time = 0;

	// While the player is running, player maintains y = standingVer...
	public short standingVerticalValue;

	public Player(short x, short y) {
		super(x, y, ID.Player);

		this.loadSprites();
		this.rAnimator = new Animator(rSprites, (byte) 30, this);
		this.lAnimator = new Animator(lSprites, (byte) 30, this);

		this.playerFacing = Direction.Right;
		this.stillSprite = rSprites.get(0);
		this.currentSprite = stillSprite;

		super.setWidth((byte) currentSprite.getWidth());
		super.setHeight((byte) currentSprite.getHeight());

		Game.handler.add(this);
	}

	public void tick() {

		// Player Direction
		if (this.velX > 0) {
			playerFacing = Direction.Right;
		}
		if (this.velX < 0) {
			playerFacing = Direction.Left;
		}
		if (playerFacing == Direction.Left) {
			stillSprite = lSprites.get(0);
		} else if (playerFacing == Direction.Right) {
			stillSprite = rSprites.get(0);
		}

		// Begin Physics
		configureStates();

		if (airborne) {
			this.canJump = false;
			acceleration = acclerationOfGravity;
			time++;

			if (falling) {
				this.velY = (short) (acceleration * time);
			} else if (jumping) {
				velY = ((short) (velyInit + (acceleration * time)));
			}
		} else if (grounded) {
			// Turn off gravity and reset time.
			jumping = false;
			//acceleration = 0;
			time = 0;

			canJump = true;
			this.y = standingVerticalValue;
		}

		// Horizontal stuff
		if (movingHorizontal) {
			if (rightKeyDown) {
				velX += RUNNINGSPEED;
			}
			if (leftKeyDown) {
				velX -= RUNNINGSPEED;
			}
		} else {
			velX = 0;
		}
		setVelocities();
		
		//Will loop through every block between player ticks.
		this.blockBelow = false;

	}

	private void setVelocities() {

		// Limit Velocities
		if (Math.abs(this.velX) >= MAX_VELX) {
			this.velX = (short) (MAX_VELX * (this.velX / Math.abs(this.velX)));
		}
		if (Math.abs(this.velY) >= MAX_VELY) {
			this.velY = MAX_VELY;
		}

		this.x += this.velX;

		// If on the ground, this value will be with the box it collided with
		if (grounded) {
			this.y = standingVerticalValue;
		} else {
			this.y += velY;
		}
	}

	private void configureStates() {

		if (canJump && upKeyDown) {
			jumping = true;
			canJump = false;
		}
		
		if (!blockBelow && !jumping){
			falling = true;
		}

		if (jumping || falling) {
			airborne = true;
			grounded = false;
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
		}

		if (keyCode == KeyEvent.VK_X) {
			if (!Game.debug) {
				Game.debug = true;
				return;
			} else {
				Game.debug = false;
				return;
			}
		}

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
		case KeyEvent.VK_S:
			downKeyDown = false;
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