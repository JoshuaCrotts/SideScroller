package com.joshuacrotts.sidescroller.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Random;

import com.joshuacrotts.sidescroller.enemies.BasicEnemy;

public class Game extends Canvas implements Runnable{

	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int SCROLLSPOT = 640;

	private Thread thread;
	private boolean running = false;
	public static boolean paused = false;

	private BufferStrategy bs;

	public int difficulity;

	//private Handler handler;
	private Window window;
	
	//Objects
	private Player player;

	//Etc objects
	private Random randomInt = new Random();
	private Camera camera;
	
	//Levels
	private Level[] levels;

	private Handler handler;
	
	
	private int frames;
	private int currentFPS;

	public enum STATE{
		Menu,
		Help,
		Select,
		Shop,
		Game,
		End
	};

	public STATE gameState = STATE.Menu;

	public Game(){

		//Initializes the Handlers needed for essentially everything and the Menu for the menus. 
		handler = new Handler(this);
		this.camera = new Camera(0,0);
		this.window = new Window(WIDTH,HEIGHT, "Game", this);
		this.levels = new Level[1];
		this.player = new Player(640, 624, ID.Player, handler, this, levels);
		
		this.addLevels();
		this.addEnemies();
		this.addKeyListener(player);
		this.start();

	}
	

	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop(){
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		running = false;
	}

	/**Game Loop
	 * src code not mine
	 * @author not me lol
	 */
	public void run()
	{
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
				System.out.println(currentFPS);
			}
		}
		stop();
	}

	/**
	 * tick() will update enemy positions in all of the 
	 * main classes such as HUD and Spawner
	 */
	private void tick(){
		//System.out.println("Px :"+player.getX());
		//System.out.println("levelx :"+levels[0].getX());
		
		levels[0].tick();
		handler.tick();
		
		for(int i = 0; i<handler.getEntities().size(); i++){
			if(handler.getEntities().get(i).getId() == ID.Player){
				camera.tick(handler.getEntities().get(i));
			}
		}
	
	}

	/**
	 * render() makes the Double/Triple Buffering possible.
	 * It fills the screen up with black, renders everything,
	 * ends the drawing, then shows the buffered image, then repeats.
	 * Updates all the main classes such as Handler and HUD and MENU 
	 * graphically.
	 */
	private void render(){
		bs = this.getBufferStrategy();

		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		//DRAW HERE
		
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), HEIGHT);
		
		g2.translate(camera.getX(), camera.getY()); //begin of cam
		
		levels[0].render(g);
		handler.render(g);
		
		g2.translate(-camera.getX(), -camera.getY()); //end of camera
		
		//FPS drawer
		Font f = new Font("Arial",Font.BOLD,14);
		g.setFont(f);
		g.setColor(Color.RED);
		g.drawString("Side Scroller Indev", 10, 20);
		g.drawString("FPS: "+currentFPS, 10, 40);
		
		
		
		//END DRAWING

		g.dispose();
		bs.show();
	}

	public static float clamp(float var, float min, float max){
		if(var >= max){
			return var = max;
		}
		else if(var <= min){
			return var = min;
		}
		else
			return var;
	}
	private void addLevels(){
		this.levels[0] = new Level("img/backgrounds/l1.png",this, handler);
	}
	
	private void addEnemies(){
		this.levels[0].add(new BasicEnemy(300,624, this,handler,player));
	}

	public static void main(String[] args) {

		new Game();
	}

}

