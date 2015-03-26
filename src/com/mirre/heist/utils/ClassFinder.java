package com.mirre.heist.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;

public class ClassFinder {

	//Simple but limited class name finder method.
	//Not used while the game/program is running normally only when reloading classNames is enabled.
	//When reloading classNames the game automaticly creates a file filled with the needed classes name
	//And exits the game after since it cannot be used normally. 
	//This is a workaround because of issues with doing this normally.
	public static List<String> findClassesInPackage(String packagePath){
		Gdx.app.log("Class", packagePath);
		List<String> clazzes = new ArrayList<String>();
		
		URL url = ClassLoader.getSystemClassLoader().getResource(packagePath);
		
		File root = new File(url.getPath());
		String truePath = packagePath.replace('/', '.');
		
		for(File file : root.listFiles()){
			if(!file.isDirectory()) {
				clazzes.add(truePath + "." + file.getName().substring(0, file.getName().length() - 6));
			}
		}
		return clazzes;
	}
	
}
