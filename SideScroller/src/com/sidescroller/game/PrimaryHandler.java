package com.sidescroller.game;

import java.awt.Graphics;
import java.util.ArrayList;

import com.sidescroller.characters.GameObject;
import com.sidescroller.weapons.Explosion;

public class PrimaryHandler {

	private ArrayList<GameObject> entities;
	public static Game game;

	public PrimaryHandler(Game game) {
		this.game = game;
		this.entities = new ArrayList<GameObject>();
	}

	public void tick() {
		for (int i = 0; i < this.entities.size(); i++) {

			if (this.entities.get(i).getID() == ID.Bullet) {

				for (int j = 0; j < this.entities.size(); j++) {
					if (this.entities.get(j).getID() == ID.Runner) {
						if (this.entities.get(i).getBounds().intersects(this.entities.get(j).getBounds())) {
							this.add(new Explosion((short) (this.entities.get(j).getX()),
									(short) (this.entities.get(j).getY() + 20), com.sidescroller.game.ID.Explosion));
							this.entities.remove(i);
							this.entities.remove(j);
							i--;
							j--;

						}
					}
				}
			}

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
	 * blocks too far away as close enough.
	 * 
	 * Also, there was a bug with using y-velocities to test horizontal
	 * collisions regarding whether or not the y-values were in the same range
	 * or not, so we had to disregard y-values for intersection when testing for
	 * X-values. In the event where you perfectly (oh so perfectly) hit the
	 * corner, which I have not been able to do, I believe the player will just
	 * either jerk upwards or to the right, but definitely not both. Again, I
	 * have not been able to do this as it's literally one pixel in each
	 * direction while moving exactly towards the block.
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
		} else {

			if (lowerBound <= obj2.getX() && obj2.getX() <= upperBound) {
				return true;
			}
			if (lowerBound <= (obj2.getX() + obj2.getWidth()) && (obj2.getX() + obj2.getWidth()) <= upperBound) {
				return true;
			}
			if (obj2.getX() <= lowerBound && obj2.getX() + obj2.getWidth() >= upperBound) {
				return true;
			}
		}
		return false;
	}

	public boolean sameY_Range(GameObject obj1, GameObject obj2, boolean verticalTest) {

		double lowerBound = obj1.getY() + obj1.getVelY();
		double upperBound = lowerBound + obj1.getHeight();

		if (verticalTest) {
			lowerBound--;
			upperBound++;

			// 3 scenarios where they would have the same y range
			if (lowerBound <= obj2.getY() + obj2.getVelY() && obj2.getY() + obj2.getVelY() <= upperBound) {
				return true;
			}
			if (lowerBound <= (obj2.getY() + obj2.getVelY() + obj2.getHeight())
					&& (obj2.getY() + obj2.getVelY() + obj2.getHeight()) <= upperBound) {
				return true;
			}
			if (obj2.getY() + obj2.getVelY() <= lowerBound
					&& obj2.getY() + obj2.getVelY() + obj2.getHeight() >= upperBound) {
				return true;
			}
		} else {
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
		}

		return false;
	}
}