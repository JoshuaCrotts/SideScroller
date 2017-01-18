package com.sidescroller.game;

import java.awt.Graphics;
import java.util.ArrayList;

import com.sidescroller.blocks.Block;
import com.sidescroller.blocks.NCBlock;

public class BlockHandler {

	private ArrayList<Block> blocks;

	public static Game game;
	public static PrimaryHandler handler;
	
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
		for (int i = 0; i < blocks.size(); i++){
			blocks.get(i).render(g);
		}
	}
	
	public void add(Block b){
		this.blocks.add(b);
	}
	
	public void add(NCBlock b){
		this.blocks.add(b);
	}
	
	public ArrayList<Block> getBlocks(){
		return blocks;
	}
}