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
import com.joshuacrotts.sidescroller.main.Game;
import com.joshuacrotts.sidescroller.main.Handler;
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

	private int velX;
	private int velY;

	private String lastDirection;

	private Handler handler;
	private Game game;

	public BasicEnemy(int x, int y, Game game, Handler handler) {
		super(x, y);
		this.lastDirection = "right";
		this.handler = handler;
		this.game = game;

		this.rSprites = new ArrayList<BufferedImage>();
		this.lSprites = new ArrayList<BufferedImage>();
		this.rAnimator = new Animator(rSprites, 30, this);
		this.lAnimator = new Animator(lSprites, 30, this);
		this.loadSprites();
		this.stillSprite = this.rSprites.get(0);
		this.currentSprite = this.stillSprite;

		this.handler.add(this);

	}

	@Override
	public void tick() {

		if (lastDirection.equals("left")) {
			this.stillSprite = lSprites.get(0);
		} else if (lastDirection.equals("right")) {
			this.stillSprite = rSprites.get(0);
		}

		if (moving) {
			if (timer >= 400) {
				timer = 0;
			}
			if (timer > 200 && timer < 400) {
				lastDirection = "left";
				super.setX(super.getX() - velX);
				lAnimator.animate();
			} else {

				lastDirection = "right";
				super.setX(super.getX() + velX);
				rAnimator.animate();
			}

			timer++;
		}

		// System.out.println("Enemy X: "+super.getX());

	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);

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
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}
}
