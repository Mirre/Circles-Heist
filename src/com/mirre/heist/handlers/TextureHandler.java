package com.mirre.heist.handlers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class TextureHandler {

	private HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private boolean disposed = false;
	
	public TextureHandler(){}
	
	
	public HashMap<String, Texture> getTextures() {
		return textures;
	}
	
	public void load(String textureLocation){
		if(disposed) disposed = false;
		if(!textures.containsKey(textureLocation))
			textures.put(textureLocation, new Texture(Gdx.files.internal(textureLocation)));
	}
	
	public void dispose(){
		for(Texture t : textures.values())
			t.dispose();
		textures.clear();
		disposed = true;
	}
	
	public Texture getTexture(String key) {
		if(disposed)
			throw new NullPointerException();
		return textures.containsKey(key) ? textures.get(key) : null;
	}
	
}
