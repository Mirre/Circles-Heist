package com.mirre.ball.objects.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.objects.core.TextureObject;
import com.mirre.ball.objects.interfaces.Collideable;

public class Stair extends TextureObject implements Collideable {
	
	public Stair(int x, int y, ObjectColor color) {
		super(x, y, 1, 1, color);
	}
	
	@Override
	public boolean passThroughAble() {
		return true;
	}

	@Override
	public TextureRegion getTexture() {
		if(texture == null)
			texture = new TextureRegion(getType().getTexture(0), 0, 0, 100, 100);
		return texture;
	}

}
