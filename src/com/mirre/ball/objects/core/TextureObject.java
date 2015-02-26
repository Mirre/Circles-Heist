package com.mirre.ball.objects.core;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.objects.interfaces.Textured;

public abstract class TextureObject extends PixelObject implements Textured {

	protected TextureRegion texture = null;
	
	public TextureObject(int x, int y, ObjectColor color) {
		super(x, y, color);
	}	
	
	public TextureObject(int x, int y, float width, float height, ObjectColor color) {
		super(x, y, width, height, color);
	}
	
	//Not used but maybe in the future.
	//@Override
	//public void dispose() {
	//	texture = null;
	//}
	
	@Override
	public void draw(Batch batch){
		batch.draw(getTexture().getTexture(), getBounds().getX(), getBounds().getY(), getTextureWidth(), getTextureHeight());
	}

	@Override
	public boolean hasTexture() {
		return true;
	}
	
	@Override
	public float getTextureHeight(){
		return getBounds().getHeight();
	}
	
	@Override
	public float getTextureWidth(){
		return getBounds().getWidth();
	}
	
}
