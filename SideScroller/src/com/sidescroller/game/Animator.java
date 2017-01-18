package com.sidescroller.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.sidescroller.characters.GameObject;

public class Animator{

	private ArrayList<BufferedImage> images;

	private double counter;
	private long delay = 0;
	private int frame = 0;
	private GameObject object;
	
	//public static int attackCounter = 0;

	private boolean animating = false;

	public Animator(ArrayList<BufferedImage> images, long delay, GameObject o){
		this.images = images;
		this.delay = delay;
		this.object = o;
	}
	
	public void animate(){
		this.animating = true;
		for(int i = 0; i<images.size(); i++){
			counter++;
			if(counter > delay){			
				object.currentSprite = this.images.get(i);
				counter = 0;
			}
		}
		this.animating = false;
	}

	public ArrayList<BufferedImage> getImages() {
		return images;
	}

	public void setImages(ArrayList<BufferedImage> images) {
		this.images = images;
	}

	public double getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public boolean isAnimating() {
		return animating;
	}

	public void setAnimating(boolean animating) {
		this.animating = animating;
	}
}