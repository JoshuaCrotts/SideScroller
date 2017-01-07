package com.joshuacrotts.sidescroller.main;

import java.awt.Graphics;
import java.util.ArrayList;

public class Handler {

	private ArrayList<GameObject> entities;
	
	private Game game;
	
	public Handler(Game game){
		this.game = game;
		this.entities = new ArrayList<GameObject>();
	}
	
	public void tick(){
		for(int i = 0; i<entities.size(); i++){
			entities.get(i).tick();
		}
	}
	
	public void render(Graphics g){
		for(int i = 0; i<entities.size(); i++){
			entities.get(i).render(g);
		}
	}
	
	public void add(GameObject o){
		this.entities.add(o);
	}
	
	public ArrayList<GameObject> getEntities(){
		return entities;
	}
}
