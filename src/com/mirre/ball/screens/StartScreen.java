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
import com.mirre.ball.CircleHeist;
import com.mirre.ball.utils.ChainedTextButton;

public class StartScreen extends AbstractScreen {

	private Stage stage;
	private Table table;


	public StartScreen(Game game){
		super(game);
	}

	@Override
	public void show() {
		this.stage = new Stage();
		this.table = new Table();
		
		Gdx.input.setInputProcessor(getStage());
		
		boolean b = Gdx.app.getType() == ApplicationType.Android;
		
		getTable().setFillParent(true);
		TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal("data/background.png")));
		TextureRegionDrawable drawable = new TextureRegionDrawable(region); 
		getTable().setBackground(drawable);
		getStage().addActor(getTable());
		
		for(int i = 1 ; Gdx.files.internal("data/level" + i + ".png").exists() ; i++){
			final int level = i;
			TextButton textButton = new ChainedTextButton("" + i)
			.addFont(b ? getStage().getWidth()/400 : 1.3F, b ? getStage().getHeight()/200 : 1.3F, Color.WHITE).styleUp(Color.DARK_GRAY)
			.styleDown(Color.DARK_GRAY).styleChecked(Color.DARK_GRAY)
			.styleOver(Color.RED).create();
			if(b)
				getTable().add(textButton).size(getStage().getWidth()/6, getStage().getHeight()/6).space(30);
			else
				getTable().add(textButton).size(100, 50).space(10);
			textButton.addListener(new ClickListener(){
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button){
					if(((CircleHeist) getGame()).getCompletedLevels() >= level)
						getGame().setScreen(new GameScreen(getGame(), "" + level));
				}
			});
		}
		
		TextButton instructions = new ChainedTextButton("Instructions")
		.addFont(b ? getStage().getWidth()/400 : 1.3F, b ? getStage().getHeight()/200 : 1.3F, Color.WHITE).styleUp(Color.DARK_GRAY)
		.styleDown(Color.DARK_GRAY).styleChecked(Color.DARK_GRAY)
		.styleOver(Color.RED).create();
		instructions.addListener(new ClickListener(){
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button){
				getGame().setScreen(new InstructionScreen(getGame()));
			}
		});
		
		
		if(b){
			getTable().add(instructions).size(getStage().getWidth()/5, getStage().getHeight()/7).space(30);
		}else{
			getTable().add(instructions).size(100, 50).space(10);
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
	
}

