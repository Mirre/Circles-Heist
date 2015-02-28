package com.mirre.ball.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mirre.ball.CircleHeist;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.handlers.LevelRenderer;
import com.mirre.ball.objects.blocks.Gold;
import com.mirre.ball.objects.moving.CircleController;
import com.mirre.ball.utils.ProgressBar;

public class GameScreen extends AbstractScreen {

	private Stage stage;
	private Table table;
	private ProgressBar progressBar;
	private Label labelXYGold;
	private Level level;
	private LevelRenderer renderer;
	private int levelID;
	
	private Button moveButton;
	private Button stealthButton;
	private Button jumpButton;
	
	private List<Texture> progressBars = new ArrayList<Texture>();
	private Texture ui, asdButton, jButton, sButton = null;
	private BitmapFont font;
	
	private boolean paused = false;
	private boolean wasPaused = false;
	
	GameScreen(Game game, String level) {
		super(game);
		this.levelID = Integer.parseInt(level);
	}
	
	private void createTextures(){
		progressBars.clear();
		for(int i = 10 ; i != -1 ; i--){
			Texture region = new Texture(Gdx.files.internal("data/progressbar" + i + ".png"));
			progressBars.add(region);
		}
		ui = new Texture(Gdx.files.internal("data/ui.png"));
		asdButton = new Texture(Gdx.files.internal("data/ASD.png"));
		jButton = new Texture(Gdx.files.internal("data/JumpButton.png"));
		sButton = new Texture(Gdx.files.internal("data/StealthButton.png"));
	}
	
	public void disposeTextures(){
		if(ui != null){
			ui.dispose();
			ui = null;
		}if(asdButton != null){
			asdButton.dispose();
			asdButton = null;
		}if(jButton != null){
			jButton.dispose();
			jButton = null;
		}if(sButton != null){
			sButton.dispose();
			sButton = null;
		}if(font != null){
			font.dispose();
			font = null;
		}
		for(Texture t : progressBars){
			t.dispose();
		}
	}
	
	@Override
	public void show() {
		
		this.stage = new Stage();
		this.table = new Table();
		
		disposeTextures();
		createTextures();
		
		
		boolean b = Gdx.app.getType() == ApplicationType.Android;
		
		Gdx.input.setInputProcessor(getStage());
		
		getStage().setViewport(new ExtendViewport(b ? 16 : 24, b ? 12 : 18, getStage().getCamera())); //24, 18
	
		ProgressBar bar = b ? new ProgressBar(7, 1).setProgress(10) : new ProgressBar(5, 1).setProgress(10);
		for(Texture t : progressBars){
			TextureRegion region = new TextureRegion(t, 100, 50);
			bar.addBarTextures(region);
		}
		this.progressBar = bar;
		
		font = new BitmapFont();
		font.setUseIntegerPositions(false);
		LabelStyle textStyle = new LabelStyle(font, Color.RED);
		

		this.labelXYGold = new Label("X / Y", textStyle);
		getLabelXYGold().setFontScaleX(getLabelXYGold().getFontScaleX()/(b ? 15 : 10));
		getLabelXYGold().setFontScaleY(getLabelXYGold().getFontScaleY()/(b ? 15 : 10));
		
		TextureRegion region = new TextureRegion(ui);
		TextureRegionDrawable drawable = new TextureRegionDrawable(region); 
	
		
		getTable().setBackground(drawable);
		getTable().setFillParent(true);
		getStage().addActor(getTable());
		getStage().addActor(getProgressBar());
		getStage().addActor(getLabelXYGold());
		
		if(b)
			androidUI();
		
		
		this.level = new Level(getGame(), getLevelID());
		this.renderer = new LevelRenderer(getLevel(), getStage());
	}
	

	@Override
	public void render(float delta) {
		

		//0.06F = 0.06 Seconds Between Frames = 60FPS
		delta = 0.017F;
		Gdx.app.log("Delta3", "" + delta);
		if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
			getGame().setScreen(new StartScreen(getGame()));
			return;
		}
		
	
		if(!isPaused())
			getLevel().update(delta);
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		getRenderer().render(delta);
		
		getTable().setBounds(getStage().getCamera().position.x - getStage().getCamera().viewportWidth/2, getStage().getCamera().position.y - getStage().getCamera().viewportHeight/2, getStage().getCamera().viewportWidth, getStage().getCamera().viewportHeight);
		
		getLabelXYGold().setX(getStage().getCamera().position.x + getStage().getCamera().viewportWidth/3);
		getLabelXYGold().setY(getStage().getCamera().position.y - getStage().getCamera().viewportHeight/ 2.5F);
		getLabelXYGold().setText(getLevel().getCircle().getGoldCollected() + " / " + (Gold.getAmountOfGold()));
				
	

		if(Gdx.app.getType() == ApplicationType.Android){
			getLabelXYGold().setY(getStage().getCamera().position.y - getStage().getCamera().viewportHeight/ 2.5F);
			getProgressBar().update(getStage().getCamera().position.x - getStage().getCamera().viewportWidth/4, getStage().getCamera().position.y - getStage().getCamera().viewportHeight/2);
			getMoveButton().setBounds(getStage().getCamera().position.x - getStage().getCamera().viewportWidth/2, getStage().getCamera().position.y - getStage().getCamera().viewportHeight/2, 5, 5);
			getStealthButton().setBounds(getStage().getCamera().position.x + getStage().getCamera().viewportWidth/3 - 0.5F, getStage().getCamera().position.y - getStage().getCamera().viewportHeight/2, 2, 2);
			getJumpButton().setBounds(getStage().getCamera().position.x + getStage().getCamera().viewportWidth/6 - 0.5F, getStage().getCamera().position.y - getStage().getCamera().viewportHeight/2, 2, 2);
		}else{
			getLabelXYGold().setY(getStage().getCamera().position.y - getStage().getCamera().viewportHeight/8.18F);
			getProgressBar().update(getStage().getCamera().position.x - getProgressBar().getBarBounds().getWidth()/2, getStage().getCamera().position.y - getStage().getCamera().viewportHeight/2);
		}
		
		getStage().act(delta);
		getStage().draw();
		
		
		//Android Home Button Quick Fix
		//Did: Black screen after clicking home button and opening up again.
		//Does: Instead of resuming the game from where you were it restarts the level to prevent the black screen.
		if(wasPaused()){
			setWasPaused(false);
			show();
		}
	}

	
	@Override
	public void resize(int width, int height) {
		getStage().getViewport().update(width, height);
	}
	
	@Override
	public void hide() {
		//disposeTextures();
		getRenderer().dispose();
		getStage().dispose();
		((CircleHeist)getGame()).save();
	}

	@Override
	public void pause() {
		setPaused(true);
		((CircleHeist)getGame()).save();
	}

	@Override
	public void resume() {
		setPaused(false);
		if(Gdx.app.getType() == ApplicationType.Android)
			setWasPaused(true);
	}
	
	private void androidUI(){
		TextureRegion asd = new TextureRegion(asdButton);
		TextureRegionDrawable asdDrawable = new TextureRegionDrawable(asd); 
		this.moveButton = new Button(asdDrawable);
		getMoveButton().addListener(new ClickListener(){
			@Override
			public void touchDragged(InputEvent event, float x, float y, int pointer){
				super.touchDragged(event, x, y, pointer);
				CircleController.movementButtonX = x;
				CircleController.movementButtonY = y;
			}
		});
		
		TextureRegion stealth = new TextureRegion(sButton);
		TextureRegionDrawable stealthDrawable = new TextureRegionDrawable(stealth); 
		this.stealthButton = new Button(stealthDrawable);
		getStealthButton().addListener(new ClickListener());
		
		TextureRegion jump = new TextureRegion(jButton);
		TextureRegionDrawable jumpDrawable = new TextureRegionDrawable(jump); 
		this.jumpButton = new Button(jumpDrawable);
		getJumpButton().addListener(new ClickListener());
		
		
		getStage().addActor(getMoveButton());
		getStage().addActor(getStealthButton());
		getStage().addActor(getJumpButton());
	}
	
	public Level getLevel() {
		return level;
	}

	public LevelRenderer getRenderer() {
		return renderer;
	}

	public int getLevelID() {
		return levelID;
	}

	public Stage getStage() {
		return stage;
	}

	public Table getTable() {
		return table;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public Label getLabelXYGold() {
		return labelXYGold;
	}

	public Button getMoveButton() {
		return moveButton;
	}

	public Button getStealthButton() {
		return stealthButton;
	}

	public Button getJumpButton() {
		return jumpButton;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	private boolean wasPaused() {
		return wasPaused;
	}

	public void setWasPaused(boolean wasPaused) {
		this.wasPaused = wasPaused;
	}

}
