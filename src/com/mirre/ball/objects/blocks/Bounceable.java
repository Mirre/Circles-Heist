package com.mirre.ball.objects.blocks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.core.TextureObject;
import com.mirre.ball.objects.interfaces.Collideable;

public class Bounceable extends TextureObject implements Collideable {

	
	
	public Bounceable(int x, int y, ObjectColor color) {
		super(x, y, 1F, 1F, color);
	}

	@Override
	public void onObjectCreation(Level level){
		level.addCollideTile(this);
	}
	
	@Override
	public TextureRegion getTexture() {
		if(texture == null)
			texture = new TextureRegion(getType().getTexture(0), 0, 0, 200, 200);
		return texture;
	}

	@Override
	public boolean passThroughAble() {
		return false;
	}


}
