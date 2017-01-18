package com.sidescroller.blocks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sidescroller.game.Game;

/**A Block that has NO COLLISION; purely aesthetic*/
public class NCBlock extends Block{

	public NCBlock(short x, short y, String fileLocation) {
		super(x, y, fileLocation);
		
		this.setID(com.sidescroller.game.ID.Block);

		try {
			super.currentSprite = ImageIO.read(new File(fileLocation));
		} catch (IOException e) {
			System.err.println("Error! Could not load in Block Image");
		}
		
		this.setWidth((byte) 32);
		this.setHeight((byte) 32);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		if (!withinRenderField())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(super.currentSprite, super.getX(), super.getY(), null);
		
		if (Game.borders) {
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.YELLOW);
			g2.draw(getBounds());
		}

			
	}
	
	public Rectangle getBounds() {
		return new Rectangle(super.getX(), super.getY(), 32, 32);
	}
	
}
