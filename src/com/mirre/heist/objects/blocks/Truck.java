package com.mirre.heist.objects.blocks;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.core.TextureObject;
import com.mirre.heist.objects.interfaces.Collideable;

@TextureLocations(value = { "data/truck.png" })
public class Truck extends TextureObject implements Collideable {

	public Truck(){}
	
	public Truck(int x, int y) {
		super(x, y, 5, 3);
	}

	@Override
	public Color getColor() {
		return Color.GREEN;
	}

	@Override
	public boolean passThroughAble() {
		return true;
	}
	
	@Override
	public boolean canCache() {
		return false;
	}

	@Override
	public void onObjectCreation(Level level) {
		level.setTruck(this);
	}
}
