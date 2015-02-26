package com.mirre.ball.objects.blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.core.TextureObject;

public class Truck extends TextureObject {
	
	public Truck(int x, int y, ObjectColor color) {
		super(x, y, 5, 3, color);
	}
	
	@Override
	public TextureRegion getTexture() {
		if(texture == null)
			texture = new TextureRegion(getType().getTexture(0), 0, 0, 652, 322);
		return texture;
	}

	@Override
	public boolean canCache(){
		return false;
	}
	
	@Override
	public void onObjectCreation(Level level) {
		level.setStartLocation(this);
		Gdx.app.log("Create", "Truck");
	}
	
}
