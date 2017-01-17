package com.sidescroller.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sidescroller.main.Game.State;

public class TitleFrame implements KeyListener, MouseListener {

	private BufferedImage image;
	private BufferedImage selectionImage;

	private static Font customFont;
	private static byte selection = 0;

	public TitleFrame() {
		try {
			this.image = ImageIO.read(new File("resources/img/titlecards/contratitle.png"));
		} catch (IOException e) {
			System.err.println("Error! Could not load Contra Title Image");
			e.printStackTrace();
		}

		try {
			this.selectionImage = ImageIO.read(new File("resources/img/titlecards/selection.png"));
		} catch (IOException e) {
			System.err.println("Error! Could not load selection img");
			e.printStackTrace();
		}

		setFont();
	}

	public void tick() {

	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(this.image, 0, 0, null);
		g2.setFont(customFont);

		g2.setColor(Color.WHITE);

		if (selection == 0) {
			g2.drawImage(this.selectionImage, 420, 420, null);
			g2.drawRoundRect(180, 390, 225, 80, 20, 20);
		}

		if (selection == 1) {
			g2.drawImage(this.selectionImage, 420, 520, null);
			g2.drawRoundRect(180, 490, 225, 80, 20, 20);
		}

		g2.drawString("Play", 200, 450);
		g2.drawString("Exit", 200, 550);

	}

	public static void setFont() {
		customFont = null;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/oldschool.ttf")).deriveFont(48f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/oldschool.ttf")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();

		//System.out.println(selection);

		if (keyCode == KeyEvent.VK_UP) {

			if (selection == 0) {
				selection = 1;
				return;
			}
			selection--;
		}

		if (keyCode == KeyEvent.VK_DOWN) {

			if (selection == 1) {
				selection = 0;
				return;
			}
			selection++;

		}

		if (keyCode == KeyEvent.VK_ENTER) {
			if (selection == 0) {
				Game.gameState = State.Game;
			}
			if (selection == 1) {
				System.exit(0);
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}
