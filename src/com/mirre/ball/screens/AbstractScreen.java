package com.mirre.ball.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

abstract class AbstractScreen implements Screen {
	
	private Game game;

	AbstractScreen(Game game){
		setGame(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}


	@Override
	public void pause() {
		Gdx.app.log("Pause", "Test");
	}

	@Override
	public void resume() {
		Gdx.app.log("Resume", "Test");
	}

	@Override
	public void dispose() {
		Gdx.app.log("Dispose", "Test");
	}

}
