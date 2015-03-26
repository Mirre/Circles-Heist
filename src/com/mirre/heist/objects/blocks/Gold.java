package com.mirre.heist.objects.blocks;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.objects.core.TextureObject;
import com.mirre.heist.objects.interfaces.Collideable;

@TextureLocations(value ={ 
  "data/gold1.png",
  "data/gold2.png" 
})
public class Gold extends TextureObject implements Collideable {

	private boolean collected = false;
	private static int amountOfGold = 0;
	private int typeOfGold;
	
	public Gold(){}
	
	public Gold(int x, int y) {
		super(x, y, 1, 1);
		Gold.amountOfGold++;
		setTypeOfGold(new Random().nextInt(2));
		texture = getTextures().get("data/gold"+ getTypeOfGold() + ".png");
	}
	
	@Override
	public Color getColor() {
		return Color.BLUE;
	}

	@Override
	public boolean passThroughAble() {
		return true;
	}
	
	@Override
	public boolean hasTexture(){
		return !isCollected();
	}
	
	@Override
	public boolean canCache() {
		return false;
	}

	public boolean isCollected() {
		return collected;
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
}
