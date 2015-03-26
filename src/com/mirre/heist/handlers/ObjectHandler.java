package com.mirre.heist.handlers;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.mirre.heist.CircleHeist;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.objects.core.PixelObject;

public class ObjectHandler {

	private HashMap<Color, String[]> textureLocations = new HashMap<Color, String[]>();
	private HashMap<Color, Class<?>> colorClasses = new HashMap<Color, Class<?>>();
	
	private TextureHandler handler;
	
	public ObjectHandler(TextureHandler handler){
		this.handler = handler;
		CircleHeist game = (CircleHeist) Gdx.app.getApplicationListener();
		for(String s : game.getClassNames()){
			try {
				register(Class.forName(s));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getTextureLocations(Color type) {
		return textureLocations.get(type);
	}
	
	public void register(Color type, String... textureLocations) {
		if(!this.textureLocations.containsKey(type)){
			this.textureLocations.put(type, textureLocations);
			for(String s : textureLocations)
				handler.load(s);
		}
	}
	
	public HashMap<String, Texture> getTextures(Color c) {
		HashMap<String, Texture> textures = new HashMap<String, Texture>();
		for(String s : getTextureLocations(c)){
			textures.put(s, textureHandler().getTexture(s));
		}
		return textures;
	}
	
	public void register(Class<?> clazz) {
		Color c = null;
		try {
			Constructor<?> con = clazz.getDeclaredConstructor();
			Object o = con.newInstance();
			c = ((PixelObject) o).getColor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(c != null){
			colorClasses.put(c, clazz);
			if(clazz.isAnnotationPresent(TextureLocations.class)){
				register(c, clazz.getAnnotation(TextureLocations.class).value());
			}
		}
	}
	
	public TextureHandler textureHandler() {
		return handler;
	}
	
	public Class<?> colorToClass(Color c){
		//For some random reason Pink and Purple doesn't work
		//but it works like this lol. 
		if(c.equals(Color.PINK)){
			//Gdx.app.log("PINKPLEASE", "" + colorClasses.get(c)); Returns NULL wtf
			return colorClasses.get(Color.PINK);
		}if(c.equals(Color.PURPLE))
			return colorClasses.get(Color.PURPLE);
		
		return colorClasses.containsKey(c) ? colorClasses.get(c) : null;
	}
	
	public PixelObject colorToObject(Color c, int x, int y){
		Class<?> clazz = colorToClass(c);
		if(clazz != null){
			try{
				Constructor<?> con = clazz.getDeclaredConstructor(int.class, int.class);
				return (PixelObject) con.newInstance(x,y);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
