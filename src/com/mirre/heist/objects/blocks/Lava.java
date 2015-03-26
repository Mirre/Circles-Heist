package com.mirre.heist.objects.blocks;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.objects.core.TextureObject;
import com.mirre.heist.objects.interfaces.Collideable;

@TextureLocations(value = { "data/Lava.png" })
public class Lava extends TextureObject implements Collideable {

	public Lava(){}
	
	public Lava(int x, int y) {
		super(x, y, 1F, 0.7F);
	}

	@Override
	public Color getColor() {
		return Color.PURPLE;
	}
	
	@Override
	public boolean passThroughAble() {
		return true;
	}

	@Override
	public float getTextureHeight(){
		return 1;
	}
	
}
