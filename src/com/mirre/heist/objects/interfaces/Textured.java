package com.mirre.heist.objects.interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface Textured extends LevelObject {

	public TextureRegion getTexture();
	public void draw(Batch batch);
	public float getTextureHeight();
	public float getTextureWidth();
	
}
