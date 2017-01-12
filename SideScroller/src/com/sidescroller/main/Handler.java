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

	public boolean sameX_Range(GameObject obj1, GameObject obj2) {
		double lowerBound = obj1.getX() + obj1.getVelX();
		double upperBound = lowerBound + obj1.getWidth();

		if (lowerBound <= obj2.getX() && obj2.getX() <= upperBound) {
			return true;
		}
		if (lowerBound <= (obj2.getX() + obj2.getWidth()) && (obj2.getX() + obj2.getWidth()) <= upperBound) {
			return true;
		}
		if (obj2.getX() <= lowerBound && obj2.getX() + obj2.getWidth() >= upperBound) {
			return true;
		}

		return false;
	}

	public boolean sameY_Range(GameObject obj1, GameObject obj2, boolean belowCollisionTest) {

		double lowerBound = obj1.getY() + obj1.getVelY();
		double upperBound = lowerBound + obj1.getHeight();

		if (belowCollisionTest) {
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
