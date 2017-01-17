package com.sidescroller.main;

import java.awt.Graphics;
import java.util.ArrayList;

public class Handler {

	private ArrayList<GameObject> entities;
	public static Game game;

	public Handler(Game game) {
		this.game = game;
		this.entities = new ArrayList<GameObject>();
	}

	public void tick() {
		for (int i = 0; i < this.entities.size(); i++) {
			this.entities.get(i).tick();
		}
	}

	public void render(Graphics g) {
		for (int i = 0; i < this.entities.size(); i++) {
			this.entities.get(i).render(g);
		}
	}

	public ArrayList<GameObject> getEntities() {
		return entities;
	}

	public void add(GameObject obj) {
		this.entities.add(obj);

	}

	/**
	 * The reason we have to have the horizontalTest and verticalTest parameters
	 * in the x-range and y-range methods respectively, is because when we say
	 * it's true it will test one pixel further away, meaning that if I am
	 * testing vertically and I'm one pixel away from this object, I want the
	 * block to be out of the x-range as a potential vertical collision. This
	 * allows us to check if the blocks are within range without mistaking
	 * blocks too far away as close enough
	 * 
	 * @author Brandon Willis
	 */

	public boolean sameX_Range(GameObject obj1, GameObject obj2, boolean horizontalTest) {
		double lowerBound = obj1.getX() + obj1.getVelX();
		double upperBound = lowerBound + obj1.getWidth() + obj1.getVelX();

		// Expand range by one pixel
		if (horizontalTest) {
			lowerBound--;
			upperBound++;
		}
		if (lowerBound <= obj2.getX() + obj2.getVelX() && obj2.getX() + obj2.getVelX() <= upperBound) {
			return true;
		}
		if (lowerBound <= (obj2.getX() + obj2.getVelX() + obj2.getWidth())
				&& (obj2.getX() + obj2.getVelX() + obj2.getWidth()) <= upperBound) {
			return true;
		}
		if (obj2.getX() + obj2.getVelX() <= lowerBound
				&& obj2.getX() + obj2.getWidth() + obj2.getVelX() >= upperBound) {
			return true;
		}

		return false;
	}

	public boolean sameY_Range(GameObject obj1, GameObject obj2, boolean verticalTest) {

		double lowerBound = obj1.getY();
		double upperBound = lowerBound + obj1.getHeight();

		if (verticalTest) {
			lowerBound--;
			upperBound++;
		}

		// 3 scenarios where they would have the same y range
		if (lowerBound <= obj2.getY() && obj2.getY() <= upperBound) {
			return true;
		}
		if (lowerBound <= (obj2.getY() + obj2.getHeight()) && (obj2.getY() + obj2.getHeight()) <= upperBound) {
			return true;
		}
		if (obj2.getY() <= lowerBound && obj2.getY() + obj2.getHeight() >= upperBound) {
			return true;
		}

		return false;
	}
}