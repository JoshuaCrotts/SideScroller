package com.sidescroller.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import com.sidescroller.blocks.Block;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 7451407222234826899L;

	// Variables relating to the Window
	public static final short WIDTH = 1280;
	public static final short HEIGHT = 720;
	private Window w;

	// BufferStrategy/Graphics related objects

	// Etc Objects
	public static Player player;
	public static Level[] levels;
	public static Handler handler;
	public static Camera camera;
	public static BlockHandler blockHandler;

	// Level variables
	public static byte currentLevelInt = 0;

	// Objects/variables relating to the thread
	private Thread t;

	// Debug tools
	public static boolean debug = true;
	public static boolean borders = true;

	private boolean running = false;
	private short frames;
	private short updates;
	private short currentFPS;
	private short currentUPS;

	public Game() {
		handler = new Handler(this);
		blockHandler = new BlockHandler(this);
		camera = new Camera(0, 0);
		this.w = new Window(WIDTH, HEIGHT, "Side Scroller V.2", this);
		player = new Player((short) 90, (short) 500);
		levels = new Level[1];

		this.addLevels();
		this.loadImageLevel(levels[currentLevelInt].getImage());
		this.addKeyListener(player);

		this.start();
	}

	public synchronized void start() {
		if (running)
			return;
		else {
			this.t = new Thread(this);
			this.t.start();
			this.running = true;
		}
	}

	public synchronized void stop() {
		if (!running)
			return;
		else {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		running = false;
	}

	public void run() {
		requestFocus();

		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		long timer = System.currentTimeMillis();
		this.frames = 0;
		this.updates = 0;

		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				tick();
				delta--;
				updates++;
				render();
				frames++;
			}
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				w.setTitle(" | " + updates + " ups, " + frames + " fps");
				currentFPS = frames;
				currentUPS = updates;
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	// public void run() {
	// requestFocus();
	// long lastTime = System.nanoTime();
	// double amountOfTicks = 60.0;
	// double ns = 1000000000 / amountOfTicks;
	// double delta = 0;
	// long timer = System.currentTimeMillis();
	// this.frames = 0;
	//
	// while (running) {
	//
	// long now = System.nanoTime();
	// delta += (now - lastTime) / ns;
	// lastTime = now;
	// while (delta >= 1) {
	// tick();
	// delta--;
	// }
	// if (running)
	// render();
	// this.frames++;
	//
	// if (System.currentTimeMillis() - timer > 1000) {
	// timer += 1000;
	// // System.out.println("FPS: "+ this.frames);
	// currentFPS = this.frames;
	// this.frames = 0;
	// System.out.println(currentFPS);
	// }
	// }
	// stop();
	// }

	private void tick() {
		levels[currentLevelInt].tick();
		handler.tick();
		camera.tick();
		blockHandler.tick();
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;

		// DRAW HERE

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// Begin of camera
		g2.translate(camera.getTranslationX(), camera.getTranslationY());

		levels[currentLevelInt].render(g);
		handler.render(g);
		blockHandler.render(g);

		// End of camera
		g2.translate(-camera.getTranslationX(), -camera.getTranslationY());

		// FPS drawer

		if (debug) {
			Font f = new Font("Arial", Font.BOLD, 14);
			g2.setFont(f);
			g2.setColor(Color.BLACK);
			g2.drawString("Side Scroller Indev - DEBUG MODE", 40, 50);
			g2.drawString("| FPS: " + currentFPS, 40, 70);
			g2.drawString("| UPS: " + currentUPS, 40, 90);

			g2.setColor(Color.RED);
			g2.drawString("| Player X: " + player.getX(), 120, 70);
			g2.drawString("| Player Y: " + player.getX(), 120, 90);
			g2.drawString("| Player Vel X: " + player.getVelX(), 120, 110);
			g2.drawString("| Player Vel Y: " + player.getVelY(), 120, 130);

			g2.setColor(Color.BLUE);
			g2.drawString("| Collision Borders Enabled: " + Block.drawBounds, 250, 70);
			/*
			 * g2.drawString("| Left Collision: "+Player.isCollision[0],250,
			 * 90);
			 * g2.drawString("| Right Collision: "+Player.isCollision[1],250,
			 * 110);
			 * g2.drawString("| Top Collision: "+Player.isCollision[2],250,
			 * 130);
			 * g2.drawString("| Bottom Collision: "+Player.isCollision[3],250,
			 * 150);
			 */

			g2.setColor(Color.BLACK);
			g2.drawString("| Player Last Direction: " + player.getDirection(), 500, 70);
			g2.drawString("| # of Block Entities: " + blockHandler.getBlocks().size(), 500, 90);
			g2.drawString("| # of GameObject Entities: " + handler.getEntities().size(), 500, 110);
			g2.drawString("| Z to enable borders", 500, 130);
			g2.drawString("| X to toggle debug", 500, 150);
			
			g2.setColor(Color.GREEN);
			g2.drawString("| airborne: " + player.airborne, 900, 70);
			g2.drawString("| falling: " + player.falling, 900, 90);
			g2.drawString("| jumping: " + player.jumping, 900, 110);
			g2.drawString("| movingHorizontal: " + player.movingHorizontal, 900, 130);
			g2.drawString("| canJump: " + player.canJump, 900, 150);
			g2.drawString("| grounded: " + player.grounded, 900, 170);
			/*
			 * public boolean grounded = false;
	public boolean airborne = true;
	public boolean falling = true;
	public boolean jumping = false;
	public boolean movingHorizontal = false;
	public boolean canJump = false;
			 */

		}
		// END DRAWING

		g.dispose();
		bs.show();
	}

	private void loadImageLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);

				short r = (short) ((pixel >> 16) & 0xff);
				short g = (short) ((pixel >> 8) & 0xff);
				short b = (short) ((pixel) & 0xff);

				if (r == 255 && g == 255 && b == 255) {
					blockHandler.add(new Block((short) (x * 32), (short) (y * 32), "img/sprites/items/block1.png"));
				}
				if (r == 0 && g == 0 && b == 0) {
					Color c = Color.WHITE;
					image.setRGB(x, y, c.getRGB());
				}
			}
		}

	}

	private void addLevels() {
		// new Level(File, handler, width, height)
		levels[0] = new Level("img/backgrounds/level2.png", (short) 3360, (short) 704);
	}

	public static void main(String[] args) {
		new Game();

	}
}
