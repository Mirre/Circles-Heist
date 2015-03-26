package com.mirre.heist.objects.blocks;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.core.TextureObject;
import com.mirre.heist.objects.interfaces.Collideable;


@TextureLocations(value = { "data/tile.png" })
public class CollideableTile extends TextureObject implements Collideable {

	public CollideableTile(){}
	
	public CollideableTile(int x, int y) {
		super(x, y, 1, 1);
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}
	
	@Override
	public void onObjectCreation(Level level){
		level.addCollideTile(this);
	}
	
	@Override
	public boolean passThroughAble() {
		return false;
	}

}
