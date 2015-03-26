package com.mirre.heist.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mirre.heist.objects.interfaces.CustomShapeDrawer;
import com.mirre.heist.objects.interfaces.Moveable;
import com.mirre.heist.objects.interfaces.Textured;

public class LevelRenderer {

	private Level level;
	private Stage stage;
	private FPSLogger fpsLogger = new FPSLogger();
	private Vector3 lerpTarget = new Vector3();
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	public LevelRenderer(Level level, Stage stage){
		this.level = level;
		this.stage = stage;
		stage.getCamera().position.set(level.getCircle().getBounds().getPosition(new Vector2()), 0);
	}
	
	public void render(float deltaTime){
		fpsLogger.log();
		
		if(getLevel().getCache() == null){
			return;
		}
		getStage().getCamera().position.lerp(getLerpTarget().set(getLevel().getCircle().getBounds().x, getLevel().getCircle().getBounds().y, 0), 3F * deltaTime);
		getStage().getCamera().update();
		
		getLevel().getCache().setProjectionMatrix(getStage().getCamera().combined);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		getLevel().getCache().begin();
		for(int i : getLevel().getCacheIDs()){
			getLevel().getCache().draw(i);
		}
		getLevel().getCache().end();
		getStage().getBatch().setProjectionMatrix(getStage().getCamera().combined);
		getStage().getBatch().begin();
		for(Textured tile : getLevel().getUncachedObjects()){
			if(tile.hasTexture() && !(tile instanceof Moveable)){
				tile.draw(getStage().getBatch());
			}
		}
		for(Moveable m : getLevel().getMovingObjects()){
			m.draw(getStage().getBatch());
		}
		getStage().getBatch().end();
		
		for(CustomShapeDrawer drawer : getLevel().getDrawers()){
			drawer.drawShape(shapeRenderer, stage);
		}
	}
	
	public void dispose(){
		getLevel().getCache().dispose();
		getLevel().setCache(null); //Prevents OpenGLException: Cannot use offsets when Array Buffer Object is disabled
	}
	
	public Level getLevel() {
		return level;
	}

	public Vector3 getLerpTarget() {
		return lerpTarget;
	}

	public Stage getStage() {
		return stage;
	}
}
