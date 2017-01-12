package com.sidescrollerv2.main;

import java.awt.Graphics;
import java.util.ArrayList;

import com.sidescrollerv2.blocks.Block;


	private ArrayList<Block> blocks;

	public static Game game;

	public BlockHandler(Game game){
		this.game = game;
		this.blocks = new ArrayList<Block>();
	}

	public void tick(){
		for(int i = 0; i<blocks.size(); i++){
			blocks.get(i).tick();
		}
	}

	public void render(Graphics g){
		for(int i = 0; i<blocks.size(); i++){
			blocks.get(i).render(g);
		}
	}
	
	public void add(Block b){
		this.blocks.add(b);
	}
	
	public ArrayList<Block> getBlocks(){
		return blocks;
	}
}