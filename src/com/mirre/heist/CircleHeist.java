package com.mirre.heist;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.handlers.TextureHandler;
import com.mirre.heist.screens.StartScreen;
import com.mirre.heist.utils.ClassFinder;

/**
 * @author Mirrepirre aka Marcus Lundmark
 */

public class CircleHeist extends Game {

	private int completedLevels;
	private TextureHandler handler = new TextureHandler();
	private boolean reloadClassNames = false;
	private List<String> classNames = new ArrayList<String>();
	
	public List<String> getClassNames() {
		return classNames;
	}

	@Override
	public void create() {
				
		//Pull saved data.
		Preferences pref = Gdx.app.getPreferences("LevelInfo");
		setCompletedLevels(pref.getInteger("LevelsCompleted") != 0 ? pref.getInteger("LevelsCompleted") : 1);
		loadClassNames();
		
		//Change to Start screen.
		setScreen(new StartScreen(this));
		
	}
	
	@Override
	public void dispose(){
		super.dispose();
		handler.dispose();
		save();
	}
	
	/** Saves the Level Info. 
	 * Currently only the Levels Completed is saved.
	 */
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

	public TextureHandler getHandler() {
		return handler;
	}
	
	public void loadClassNames(){
		if(reloadClassNames){
			
			List<String> list = ClassFinder.findClassesInPackage("com/mirre/heist/objects/blocks");
			for(String s : ClassFinder.findClassesInPackage("com/mirre/heist/objects/moving"))
				list.add(s);
			
			FileHandle file = Gdx.files.local("classes/ClassNames.txt");
			file.writeString("", false);
			
			for(String s: list){
				file.writeString(s + "<>", true);
			}
			
			Gdx.app.exit();
			return;
		}
		
		FileHandle file = Gdx.files.internal("classes/ClassNames.txt");
		String names =  file.readString();
		for(String s : names.split("<>"))
			classNames.add(s);
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
