package com.sidescroller.main;

import java.awt.Graphics;
import java.util.ArrayList;

import com.sidescroller.blocks.Block;
public class BlockHandler {

	private ArrayList<GameObject> blocks;

	public static Game game;

	public BlockHandler(Game game){
		this.game = game;
		this.blocks = new ArrayList<GameObject>();
	}

	public void tick(){
		for(int i = 0; i<blocks.size(); i++){
			blocks.get(i).tick();
		}
	}

	public void render(Graphics g){
		for (int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(g);
		}
	}
	
	public void add(Block b){
		this.blocks.add(b);
	}
	
	public ArrayList<GameObject> getBlocks(){
		return blocks;
	}
}
