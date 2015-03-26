package com.mirre.heist.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mirre.heist.CircleHeist;
import com.mirre.heist.utils.ChainedTextButton;

public class InstructionScreen extends AbstractScreen {

	public InstructionScreen(CircleHeist game){
		super(game);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1); //Changes background color.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		getStage().act(delta);
		getStage().draw();
	}

	@Override
	public void showScreen(boolean onAndroid) {
		TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal("data/instructions.png")));
		TextureRegionDrawable drawable = new TextureRegionDrawable(region); 
		getTable().setBackground(drawable);
		getStage().addActor(getTable());
		
		generateStartScreenButton(onAndroid);
	}

	public void generateStartScreenButton(boolean b){
		TextButton mainMenu = new ChainedTextButton("Main Menu")
		.addFont(b ? getStage().getWidth()/400 : 1.3F, b ? getStage().getHeight()/200 : 1.3F, Color.WHITE).styleUp(Color.DARK_GRAY)
		.styleDown(Color.DARK_GRAY).styleChecked(Color.DARK_GRAY)
		.styleOver(Color.RED).create();
		mainMenu.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				getGame().setScreen(new StartScreen(getGame()));
			}
		});
		
		
		if(b){
			getTable().add(mainMenu).size(getStage().getWidth()/5, getStage().getHeight()/7).padTop(getStage().getHeight() - getStage().getHeight()/7).padLeft(getStage().getWidth() - ((getStage().getWidth()/5)*1.5F));
		}else{
			getTable().add(mainMenu).size(100, 50).padTop(getStage().getHeight() - 50).padLeft(getStage().getWidth() - 150);
		}
	}
}
