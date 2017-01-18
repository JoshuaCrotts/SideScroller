package com.sidescroller.visuals;

import com.sidescroller.characters.Player;
import com.sidescroller.game.Game;

public class Camera {

	//Only for translation
	private float translationX, translationY;
	private int x, y;

	// Things will not render unless within the view.
	public final int RENDERPAD = 10;
	private int renderMinX, renderMaxX;
	private int renderMinY, renderMaxY;

	public Camera(float x, float y) {
		this.translationX = x;
		this.translationY = y;
		
		this.x = 0;
		this.y = 0;
	}

	public void tick() {
		Player p = Game.player;
		
		//Camera doesn't shift because player is back
		if (p.getX() <= Game.WIDTH / 2)
			this.x = 0;
		
		//Camera doesn't shift because player is at the end
		else if (p.getX() >= Game.levels[Game.currentLevelInt].WIDTH - Game.WIDTH / 2)
			this.x = Game.levels[Game.currentLevelInt].WIDTH - Game.WIDTH;
		
		else
			this.x = p.getX() - Game.WIDTH/2;
		
		renderMinX = (int) this.x - RENDERPAD;
		renderMaxX = (int) this.x + Game.WIDTH + RENDERPAD;

		renderMinY = (int) this.y - RENDERPAD;
		renderMaxY = (int) this.y + Game.HEIGHT + RENDERPAD;

		if (p.getX() <= Game.WIDTH / 2
				|| p.getX() >= Game.levels[Game.currentLevelInt].WIDTH - Game.WIDTH / 2)
			return;
		else
			translationX = Game.WIDTH / 2 - Game.player.getX();
	}

	public int getRenderMinX() {
		return this.renderMinX;
	}

	public int getRenderMaxX() {
		return this.renderMaxX;
	}

	public int getRenderMinY() {
		return this.renderMinY;
	}

	public int getRenderMaxY() {
		return this.renderMaxY;
	}

	public float getTranslationX() {
		return translationX;
	}

	public void setTranslationX(float x) {
		this.translationX = x;
	}

	public float getTranslationY() {
		return translationY;
	}

	public void setTranslationY(float y) {
		this.translationY = y;
	}
}