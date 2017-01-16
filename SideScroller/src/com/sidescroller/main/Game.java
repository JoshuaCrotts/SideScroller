package com.sidescroller.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.sidescroller.blocks.Block;
import com.sidescroller.blocks.NCBlock;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 7451407222234826899L;

	// Variables relating to the Window
	public static final short WIDTH = 1280;
	public static final short HEIGHT = 720;
	private Window w;
	private static TitleFrame titleFrame;

	// BufferStrategy/Graphics related objects

	// Etc Objects
	public static Player player;
	public static Level[] levels;
	public static Handler handler;
	public static Camera camera;
	public static BlockHandler blockHandler;

	//Memory variables
	private Runtime instance;
	private short kb = 1024;

	// Level variables
	public static byte currentLevelInt = 0;

	// Objects/variables relating to the thread
	private Thread t;
	private boolean running = false;
	private short frames;
	private short updates;
	private short currentFPS;
	private short currentUPS;

	//States
	public static STATE gameState;

	public enum STATE{
		Menu,
		Help,
		Select,
		Game,
		End
	};

	// Debug tools
	public static boolean debug = true;
	public static boolean borders = true;
	public static boolean unlimitedFPS = false; //Debug Tool to allow for unlimited FPS

	public Game() {
		titleFrame = new TitleFrame();
		handler = new Handler(this);
		blockHandler = new BlockHandler(this);
		camera = new Camera(0, 0);
		this.w = new Window(WIDTH, HEIGHT, "Side Scroller V.2", this);
		player = new Player((short) 90, (short) 500);
		levels = new Level[1];

		this.addLevels();
		this.loadImageLevel(levels[currentLevelInt].getImage());
		this.addKeyListener(player);
		this.addKeyListener(titleFrame);
		this.addMouseListener(titleFrame);

		this.start();
	}

	public synchronized void start() {
		if (running)
			return;
		else {
			this.t = new Thread(this);
			this.t.start();
			this.running = true;
			gameState = STATE.Menu;
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
		if(!unlimitedFPS){
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

		if(unlimitedFPS){
			requestFocus();
			long lastTime = System.nanoTime();
			double amountOfTicks = 60.0;
			double ns = 1000000000 / amountOfTicks;
			double delta = 0;
			long timer = System.currentTimeMillis();
			this.frames = 0;

			while(running)
			{
				long now = System.nanoTime();
				delta += (now - lastTime) / ns;
				lastTime = now;
				while(delta >=1)
				{
					tick();
					delta--;
				}
				if(running)
					render();
				this.frames++;

				if(System.currentTimeMillis() - timer > 1000)
				{
					timer += 1000;
					//System.out.println("FPS: "+ this.frames);
					currentFPS = this.frames;
					this.frames = 0;
				}
			}
			stop();
		}
	}

	private void tick() {

		if(gameState == STATE.Menu){
			titleFrame.tick();
		}

		if(gameState == STATE.Game){
			titleFrame = null;
			if(debug)
				instance = Runtime.getRuntime();

			levels[currentLevelInt].tick();
			handler.tick();
			camera.tick();
			blockHandler.tick();
		}
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

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		//****************MENU STATE**********************//
		if(gameState == STATE.Menu){
		
			titleFrame.render(g);
		}
		//****************END MENU STATE******************//
		
		
		
		
		//****************GAME STATE**********************//
		if(gameState == STATE.Game){
			titleFrame = null;

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
				g2.setColor(Color.WHITE);
				g2.drawString("Side Scroller Alpha 1.1.0 - DEBUG MODE", 40, 50);
				g2.drawString("| FPS: " + currentFPS, 40, 70);
				g2.drawString("| UPS: " + currentUPS, 40, 90);

				g2.setColor(Color.RED);
				g2.drawString("| Player X: " + player.getX(), 120, 70);
				g2.drawString("| Player Y: " + player.getX(), 120, 90);
				g2.drawString("| Player Vel X: " + player.getVelX(), 120, 110);
				g2.drawString("| Player Vel Y: " + player.getVelY(), 120, 130);

				g2.setColor(Color.BLUE);
				g2.drawString("| Collision Borders Enabled: " + Game.borders, 250, 70);

				g2.setColor(Color.BLACK);
				g2.drawString("| Player Last Direction: " + player.getDirection(), 500, 70);
				g2.drawString("| # of Block Entities: " + blockHandler.getBlocks().size(), 500, 90);
				g2.drawString("| # of GameObject Entities: " + handler.getEntities().size(), 500, 110);
				g2.drawString("| Z to enable borders", 500, 130);
				g2.drawString("| X to toggle debug", 500, 150);

				g2.setColor(Color.BLACK);
				g2.drawString("| airborne: " + player.airborne, 900, 70);
				g2.drawString("| falling: " + player.falling, 900, 90);
				g2.drawString("| jumping: " + player.jumping, 900, 110);
				g2.drawString("| movingHorizontal: " + player.movingHorizontal, 900, 130);
				g2.drawString("| canJump: " + player.canJump, 900, 170);
				g2.drawString("| canMoveRight: " + player.rightCollision, 900, 190);
				g2.drawString("| canMoveLeft: " + player.leftCollision, 900, 210);

				g2.setColor(Color.ORANGE);

				if(!unlimitedFPS){
					g2.drawString("Heap utilization statistics [KB]", 40, 180);
					g2.drawString("Total Memory: "+instance.totalMemory()/kb, 40, 200);
					g2.drawString("Free Memory: "+instance.freeMemory()/kb, 40, 220);
					g2.drawString("Used Memory: "+(instance.totalMemory() - instance.freeMemory()) / kb,40,240);
					g2.drawString("Max Memory: "+instance.maxMemory()/kb,40, 260);
				}

				/*
				 * public boolean grounded = false; public boolean airborne = true;
				 * public boolean falling = true; public boolean jumping = false;
				 * public boolean movingHorizontal = false; public boolean canJump =
				 * false;
				 */

			}
		}
		// END DRAWING

		g.dispose();
		bs.show();
	}

	private void loadImageLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();

		Random aesthetic = new Random();
		int block = 0;

		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);

				block = aesthetic.nextInt(3);

				short r = (short) ((pixel >> 16) & 0xff);
				short g = (short) ((pixel >> 8) & 0xff);
				short b = (short) ((pixel) & 0xff);

				if (r == 255 && g == 255 && b == 255) {
					blockHandler.add(new Block((short) (x * 32), (short) (y * 32), "resources/img/sprites/items/dirt.png"));
				}

				//Grass 
				if(r == 0 && g == 255 && b == 33){
					blockHandler.add(new Block((short) (x*32), (short) (y*32), "resources/img/sprites/items/grass.png"));
				}
				//Left Side Grass
				if(r == 199 && g == 255 && b == 45){
					blockHandler.add(new Block((short) (x*32), (short) (y*32), "resources/img/sprites/items/lsidegrass1.png"));
				}
				//Right Side Grass
				if(r == 160 && g == 255 && b == 207){
					blockHandler.add(new Block((short) (x*32), (short) (y*32), "resources/img/sprites/items/rsidegrass1.png"));
				}

				//Left Corner Grass
				if(r == 0 && g == 127 && b == 14){
					blockHandler.add(new Block((short) (x*32), (short) (y*32), "resources/img/sprites/items/clgrass1.png"));
				}
				//Right Corner Grass
				if(r == 95 && g == 226 && b == 165){
					blockHandler.add(new Block((short) (x*32), (short) (y*32), "resources/img/sprites/items/crgrass1.png"));
				}

				//Little bits of extra colors of course; adds stone blocks
				if(r == 0 && g == 38 && b == 255){
					blockHandler.add(new Block((short) (x*32), (short) (y*32), "resources/img/sprites/items/stone1.png"));
				}

				//Random chooser for the shrubs and flowers.
				if(r == 255 && g == 255 && b == 0){
					if(block == 0)
						blockHandler.add(new NCBlock((short) (x*32), (short) (y*32), "resources/img/sprites/items/shrub1.png"));
					if(block == 1)
						blockHandler.add(new NCBlock((short) (x*32), (short) (y*32), "resources/img/sprites/items/shrub2.png"));
					if(block == 2)
						blockHandler.add(new NCBlock((short) (x*32), (short) (y*32), "resources/img/sprites/items/flower1.png"));
				}

				if (r == 0 && g == 0 && b == 0) {
					Color c = Color.WHITE;
					image.setRGB(x, y, c.getRGB());
				}
			}
		}

	}

	private void addLevels() {
		// new Level(File, handler, width, height)//lvl2 3360 
		levels[0] = new Level("resources/img/backgrounds/level6.png", "resources/img/backgrounds/l1.png", (short) 6240, (short) 704);
	}

	public static void main(String[] args) {
		new Game();

	}
}