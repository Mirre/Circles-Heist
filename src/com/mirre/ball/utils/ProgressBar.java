package com.mirre.ball.utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ProgressBar extends Actor {

	private List<TextureRegion> barTextures = new ArrayList<TextureRegion>();
	private Rectangle barBounds;
	private float progress = 0;
	
	public ProgressBar(int width, int height){
		setBarBounds(new Rectangle(0,0, width, height));
	}
	
	
	public void update(float x, float y){
		getBarBounds().setX(x);
		getBarBounds().setY(y);
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha){
		batch.draw(getBarTexture(getProgress()), getBarBounds().x, getBarBounds().y, getBarBounds().width, getBarBounds().height);
		super.draw(batch, parentAlpha);
	}

	public int getProgress() {
		return (int) progress;
	}

	public ProgressBar setProgress(float progress) {
		this.progress = progress;
		return this;
	}

	public Rectangle getBarBounds() {
		return barBounds;
	}

	public void setBarBounds(Rectangle barBounds) {
		this.barBounds = barBounds;
	}

	public List<TextureRegion> getBarTextures() {
		return barTextures;
	}
	
	private TextureRegion getBarTexture(int i) {
		return barTextures.get(i);
	}

	public ProgressBar addBarTextures(TextureRegion... newBarTextures) {
		for(TextureRegion region : newBarTextures){
			barTextures.add(region);
		}
		return this;
	}
	
}
