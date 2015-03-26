package com.mirre.heist.objects.moving.circle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.heist.CircleHeist;
import com.mirre.heist.enums.CircleState;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.core.PixelObject;
import com.mirre.heist.objects.core.SimpleMovingObject;
import com.mirre.heist.screens.GameScreen;

public abstract class CircleData extends SimpleMovingObject {

	private CircleState state;
	private float endDelay = 3;
	private int goldCollected = 0;
	private float stealthMeter = 10;
	private boolean stealth = false;
	private boolean onStairs = false;
	private boolean bounced = false;
		
	public CircleData(){}
	
	public CircleData(int x, int y) {
		super(x, y, 1F, 1F);
	}
	
	@Override
	public TextureRegion getTexture() {
		if(texture == null){
			texture = getTextures().get("data/BallRight.png");
		}
		return texture;
	}
	
	@Override
	public float getGravity() {
		return 19f;
	}

	@Override
	public float getStandardAcceleration() {
		return 20f;
	}


	@Override
	public float getMaxVelocity() {
		return 8f;
	}

	@Override
	public float getDampening() {
		if(isStealth())
			return 0.8F;
		return 0.95f;
	}	
	
	public int getGoldCollected() {
		return goldCollected;
	}

	public void addGold() {
		goldCollected++;
	}

	public CircleState getState() {
		return state;
	}

	public void setState(CircleState state) {
		this.state = state;
	}

	public float getEndDelay() {
		return endDelay;
	}

	public void setEndDelay(float endDelay) {
		this.endDelay = endDelay;
	}

	public boolean isStealth() {
		return stealth;
	}

	public void setStealth(boolean stealth) {
		this.stealth = stealth;
	}

	public boolean isOnStairs() {
		return onStairs;
	}

	public void setOnStairs(boolean onStairs) {
		this.onStairs = onStairs;
	}
	
	
	@Override
	public void draw(Batch batch){
		batch.draw(getTexture(), getBounds().x, getBounds().y, 1, 1);
	}
	
	public PixelObject getEscapeZone() {
		return Level.active().getTruck();
	}

	public float getStealthMeter() {
		return stealthMeter;
	}

	public void setStealthMeter(float stealthMeter) {
		this.stealthMeter = stealthMeter >= 10 ? 10 : stealthMeter;
		CircleHeist game = ((CircleHeist)Gdx.app.getApplicationListener());
		GameScreen screen = (GameScreen) game.getScreen();
		screen.getProgressBar().setProgress(stealthMeter);
	}

	public boolean hasBounced() {
		return bounced;
	}

	public void setBounced(boolean bounced) {
		this.bounced = bounced;
	}
}
