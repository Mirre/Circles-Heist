package com.mirre.heist.objects.core;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.interfaces.Textured;

public abstract class TextureObject extends PixelObject implements Textured {

	protected TextureRegion texture = null;
	private HashMap<String, TextureRegion> textures = new HashMap<String, TextureRegion>();
	
	public TextureObject(){}
	
	public TextureObject(int x, int y, float width, float height) {
		super(x, y, width, height);
		HashMap<String, Texture> test = Level.active().getHandler().getTextures(getColor());
		for(Entry<String, Texture> t : test.entrySet()){
			textures.put(t.getKey(), new TextureRegion(t.getValue()));
		}
	}	

	@Override
	public TextureRegion getTexture() {
		if(texture == null){
			texture = textures.values().iterator().next(); //Default Texture
		}
		return texture;
	}
	
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

	public HashMap<String, TextureRegion> getTextures() {
		return textures;
	}
	
}
