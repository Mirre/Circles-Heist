package com.mirre.ball.objects.blocks;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.objects.core.TextureObject;
import com.mirre.ball.objects.interfaces.Collideable;

public class Gold extends TextureObject implements Collideable {

	private boolean collected = false;
	private static int amountOfGold = 0;
	private int typeOfGold;
	
	public Gold(int x, int y, ObjectColor color) {
		super(x, y, color);
		Gold.amountOfGold++;
		setTypeOfGold(new Random().nextInt(2));
	}
	
	@Override
	public TextureRegion getTexture() {
		if(texture == null){
			texture = new TextureRegion(getType().getTexture(getTypeOfGold()), 0, 0, 250, 250);
		}
		return texture;
	}

	@Override
	public boolean hasTexture(){
		return !isCollected();
	}

	public boolean isCollected() {
		return collected;
	}
	
	@Override
	public boolean canCache() {
		return false;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	public static int getAmountOfGold() {
		return amountOfGold;
	}
	
	public static void clearAmountOfGold(){
		Gold.amountOfGold = 0;
	}

	public int getTypeOfGold() {
		return typeOfGold;
	}

	public void setTypeOfGold(int typeOfGold) {
		this.typeOfGold = typeOfGold;
	}

	@Override
	public boolean passThroughAble() {
		return true;
	}

}
