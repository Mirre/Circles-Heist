package com.mirre.heist.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mirre.heist.CircleHeist;

public class TextureHolder {

	private String textureLocation;
	
	public TextureHolder(String textureLocation){
		this.textureLocation = textureLocation;
	}

	public Texture get(){
		CircleHeist game = (CircleHeist) Gdx.app.getApplicationListener();
		return game.getHandler().getTexture(textureLocation);
	}
	
}
