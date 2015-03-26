package com.mirre.heist.objects.core;

import com.badlogic.gdx.math.Rectangle;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.interfaces.Collideable;
import com.mirre.heist.objects.interfaces.LevelObject;

public abstract class PixelObject implements LevelObject {

	private Rectangle bounds;

	protected PixelObject(){}
	
	public PixelObject(int x, int y){
		setBounds(new Rectangle(x,y,1,1));
	}
	
	public PixelObject(int x, int y, float width, float height){
		setBounds(new Rectangle(x,y,width,height));
	}
	
	//Default is can cache.
	@Override
	public boolean canCache() {
		return true;
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	@Override
	public boolean hasTexture() {
		return false;
	}

	@Override
	public boolean isCollideable() {
		return this instanceof Collideable;
	}

	public void onLevelCreation(Level level){
		level.addPixelObject(this);
		onObjectCreation(level);
	}
	
	public void onObjectCreation(Level level){
		
	}
	
}
