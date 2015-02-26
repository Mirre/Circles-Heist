package com.mirre.ball.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mirre.ball.utils.ChainedTextButton;

public class LevelEndScreen extends  AbstractScreen {

	private Stage stage;
	private Table table;
	
	private boolean won;
	private int levelID;
	
	public LevelEndScreen(Game game, final boolean won, final int levelID){
		super(game);
		this.levelID = levelID;
		this.won = won;
	}

	@Override
	public void show() {
		this.stage = new Stage();
		this.table = new Table();
		
		Gdx.input.setInputProcessor(getStage());
		
		boolean b = Gdx.app.getType() == ApplicationType.Android;
		
		getTable().setFillParent(true);
		TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal(isWon() ? "data/winScreen.png" : "data/failScreen.png")));
		TextureRegionDrawable drawable = new TextureRegionDrawable(region); 
		getTable().setBackground(drawable);
		getStage().addActor(getTable());
		
		TextButton nextLevel = new ChainedTextButton(isWon() ? "Next Level" : "Retry")
		.addFont(b ? getStage().getWidth()/400 : 1.3F, b ? getStage().getHeight()/200 : 1.3F, Color.WHITE).styleUp(Color.DARK_GRAY)
		.styleDown(Color.DARK_GRAY).styleChecked(Color.DARK_GRAY)
		.styleOver(Color.RED).create();
		nextLevel.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				if(isWon()){
					if(Gdx.files.internal("data/level" + (getLevelID() + 1) + ".png").exists())
						getGame().setScreen(new GameScreen(getGame(), "" + (getLevelID() + 1)));
				}else
					getGame().setScreen(new GameScreen(getGame(), "" + getLevelID()));
			}
		});
		
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
			getTable().add(nextLevel).size(getStage().getWidth()/5, getStage().getHeight()/7).padTop(150).row();
			getTable().add(mainMenu).size(getStage().getWidth()/5, getStage().getHeight()/7).space(30);
		}else{
			getTable().add(nextLevel).size(100, 50).row();
			getTable().add(mainMenu).size(100, 50).space(10);
		}
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1); //Changes background color.
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		getStage().act(delta);
		getStage().draw();
	}

	@Override
	public void resize(int width, int height) {
		getStage().getViewport().update(width, height, true);
	}

	@Override
	public void hide () {
		getStage().dispose();
	}

	public Stage getStage() {
		return stage;
	}

	public Table getTable() {
		return table;
	}

	public boolean isWon() {
		return won;
	}

	public int getLevelID() {
		return levelID;
	}
	
}
