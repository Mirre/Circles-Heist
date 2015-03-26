package com.mirre.heist.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mirre.heist.CircleHeist;

public abstract class AbstractScreen implements Screen {

	private Stage stage;
	private Table table;
	private CircleHeist game;
	
	public AbstractScreen(CircleHeist game){
		this.game = game;
	}
	
	@Override
	public void show() {
		setStage(new Stage());
		setTable(new Table());
		
		Gdx.input.setInputProcessor(getStage());
		getTable().setFillParent(true);
		showScreen(Gdx.app.getType() == ApplicationType.Android);
	}
	
	public abstract void showScreen(boolean onAndroid);
	
	@Override
	public void resize(int width, int height) {
		getStage().getViewport().update(width, height);
	}
	
	@Override
	public void hide () {
		getStage().dispose();
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	public Stage getStage() {
		return stage;
	}

	protected void setStage(Stage stage) {
		this.stage = stage;
	}

	public CircleHeist getGame() {
		return game;
	}

	public Table getTable() {
		return table;
	}

	protected void setTable(Table table) {
		this.table = table;
	}

}
