package com.mirre.heist.objects.blocks;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.handlers.Level;


@TextureLocations(value = { "data/bounceBlock.png" })
public class Bounceable extends CollideableTile {

	public Bounceable(){}
	
	public Bounceable(int x, int y) {
		super(x, y);
	}

	@Override
	public Color getColor() {
		return Color.YELLOW;
	}
	
	@Override
	public void onObjectCreation(Level level){
		level.addCollideTile(this);
	}
	
}
