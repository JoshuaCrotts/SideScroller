package com.sidescroller.visuals;


import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.sidescroller.game.Game;

public class Window extends Canvas{

	private JFrame frame;
	private String title;
	
	public Window(int width, int height, String title, Game game){
		this.frame = new JFrame(title);
		this.title = title;
		this.frame.setMaximumSize(new Dimension(width,height));
		this.frame.setMinimumSize(new Dimension(width,height));
		this.frame.setPreferredSize(new Dimension(width,height));
		
		this.frame.setResizable(false);
		this.frame.setDefaultCloseOperation(3);
		this.frame.setLocationRelativeTo(null);
		
		this.frame.add(game);
		this.frame.setVisible(true);
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String t){
		this.frame.setTitle(title+t);
	}
}
