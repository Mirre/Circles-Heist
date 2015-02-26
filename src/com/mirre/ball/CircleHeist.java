package com.mirre.ball;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.mirre.ball.screens.StartScreen;

public class CircleHeist extends Game {
	
	private int completedLevels;
	
	@Override
	public void create(){
		setScreen(new StartScreen(this));
		Preferences pref = Gdx.app.getPreferences("LevelInfo");
		setCompletedLevels(pref.getInteger("LevelsCompleted") != 0 ? pref.getInteger("LevelsCompleted") : 1);
	}
	
	@Override
	public void dispose(){
		super.dispose();
		
		save();
	}
	
	public void save(){
		Preferences pref = Gdx.app.getPreferences("LevelInfo");
		pref.putInteger("LevelsCompleted", getCompletedLevels());
		pref.flush();
	}

	public int getCompletedLevels() {
		return completedLevels;
	}

	public void setCompletedLevels(int completedLevels) {
		this.completedLevels = completedLevels;
	}
	
	public static void rgbaToRGB(Color color){
		int r = (int) (((((1 - color.a) * color.r) + (color.a * color.r)) * 255));
		int g = (int) (((((1 - color.a) * color.g) + (color.a * color.g)) * 255));
		int b = (int) (((((1 - color.a) * color.b) + (color.a * color.b)) * 255));
		Gdx.app.log("Color", color.toString() + " is " + r + " " + g + " " + b);
	}
	
	public static void rgbaToRGB(float r, float g, float b, float a){
		int r1 = (int) (((((1 - a) * r) + (a * r)) * 255));
		int g2 = (int) (((((1 - a) * g) + (a * g)) * 255));
		int b3 = (int) (((((1 - a) * b) + (a * b)) * 255));
		Gdx.app.log("Color", "Blabla" + " is " + r1 + " " + g2 + " " + b3);
	}

}
