package com.mirre.heist.objects.blocks;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.objects.core.TextureObject;
import com.mirre.heist.objects.interfaces.Collideable;

@TextureLocations(value = { "data/stairs.png" })
public class Stair extends TextureObject implements Collideable {

	public Stair(){}
	
	public Stair(int x, int y) {
		super(x, y, 1, 1);
	}

	@Override
	public Color getColor() {
		return Color.MAGENTA;
	}
	
	@Override
	public boolean passThroughAble() {
		return true;
	}

}
