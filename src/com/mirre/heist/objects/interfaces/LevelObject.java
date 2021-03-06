package com.mirre.heist.objects.interfaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

public interface LevelObject {

	public Rectangle getBounds();
	public boolean canCache();
	public boolean hasTexture();
	public boolean isCollideable();
	public Color getColor();
	
}
