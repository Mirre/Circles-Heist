package com.mirre.ball.objects.moving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.ball.CircleHeist;
import com.mirre.ball.enums.CircleState;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.core.PixelObject;
import com.mirre.ball.objects.core.SimpleMovingObject;
import com.mirre.ball.screens.GameScreen;

abstract class CircleData extends SimpleMovingObject {

	private CircleState state;
	private float stateTime = 0;
	private float endDelay = 3;
	private int goldCollected = 0;
	private float stealthMeter = 10;
	private boolean stealth = false;
	private boolean onStairs = false;
	private boolean bounced = false;
	
	TextureRegion textureLeft = null;
	TextureRegion textureRight = null;
	TextureRegion textureStealthLeft = null;
	TextureRegion textureStealthRight = null;
	
	CircleData(int x, int y, float width, float height, ObjectColor color) {
		super(x, y, width, height, color);
	}
	
	@Override
	public float getGravity() {
		return 20f;
	}

	@Override
	public float getStandardAcceleration() {
		return 20f;
	}


	@Override
	public float getMaxVelocity() {
		return 10f;
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

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
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
	public TextureRegion getTexture() {
		if(texture == null){
			textureStealthRight = new TextureRegion(getType().getTexture(0), 0, 0, 200, 200);
			textureStealthLeft = new TextureRegion(getType().getTexture(1), 0, 0, 200, 200);
			textureLeft = new TextureRegion(getType().getTexture(2), 0, 0, 200, 200);
			textureRight = new TextureRegion(getType().getTexture(3), 0, 0, 200, 200);
			texture = textureRight;
		}
		return texture;
	}
	
	@Override
	public void draw(Batch batch){
		batch.draw(getTexture(), getPosition().x, getPosition().y, 1, 1);
	}
	
	public PixelObject getEscapeZone() {
		return Level.getCurrentInstance().getStartLocation();
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
