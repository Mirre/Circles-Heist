package com.mirre.ball.objects.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.interfaces.Collideable;
import com.mirre.ball.objects.interfaces.LevelObject;

public class PixelObject implements LevelObject {

	private Rectangle bounds;
	private ObjectColor type;
	
	public PixelObject(int x, int y, ObjectColor color){
		setBounds(new Rectangle(x,y,1,1));
		type = color;
	}
	
	PixelObject(int x, int y, float width, float height, ObjectColor color){
		setBounds(new Rectangle(x,y,width,height));
		type = color;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	@Override
	public boolean canCache() {
		return true;
	}

	@Override
	public boolean hasTexture() {
		return false;
	}

	public boolean isCollideable() {
		return this instanceof Collideable;
	}

	public static PixelObject colorToPixelObject(int pixColor, int x, int y){
		for(ObjectColor ob : ObjectColor.values()){
			if(Color.rgba8888(ob.getColor()) == pixColor)
				return ob.getObject(x, y);
		}
		return new PixelObject(x,y, ObjectColor.PIXEL);
			
	}

	public void onLevelCreation(Level level) {
		level.addPixelObject(this);
		onObjectCreation(level);
	}

	public void onObjectCreation(Level level) {
		
	}

	public ObjectColor getType() {
		return type;
	}
	
}
