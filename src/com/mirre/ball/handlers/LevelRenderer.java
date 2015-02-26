package com.mirre.ball.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.objects.interfaces.Moveable;
import com.mirre.ball.objects.interfaces.Textured;
import com.mirre.ball.objects.moving.Circle;
import com.mirre.ball.objects.moving.Guard;

public class LevelRenderer {

	private Level level;
	private Stage stage;
	private FPSLogger fpsLogger = new FPSLogger();
	private float stateTime = 0;
	private Vector3 lerpTarget = new Vector3();
	
	private ShapeRenderer lineRenderer = new ShapeRenderer();
	
	
	public LevelRenderer(Level level, Stage stage){
		this.level = level;
		this.stage = stage;
		stage.getCamera().position.set(level.getCircle().getBounds().getPosition(new Vector2()), 0);
	}
	
	public void render(float deltaTime) {
		
		if(getLevel().getCache() == null)
			return;
		getStage().getCamera().position.lerp(getLerpTarget().set(getLevel().getCircle().getPosition().x, getLevel().getCircle().getPosition().y, 0), 3F * deltaTime);
		getStage().getCamera().update();

		
		getLevel().getCache().setProjectionMatrix(getStage().getCamera().combined);
		Gdx.gl.glDisable(GL20.GL_BLEND);
		getLevel().getCache().begin();
		for(int i : getLevel().getCacheIDs()){
			getLevel().getCache().draw(i);
		}
		getLevel().getCache().end();
		this.stateTime += deltaTime;
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

		
		//ShapeRenderers has to be drawn outside of the stage's batch.
		for(Moveable m : getLevel().getMovingObjects()){
			if(m instanceof Guard){
				Circle b = getLevel().getCircle();
				Guard g = (Guard) m;
				if(g.getSightRadius().overlaps(b.getBounds()) && g.isFacing(b)){
					getStage().getCamera().update();
					lineRenderer.setProjectionMatrix(getStage().getBatch().getProjectionMatrix());
					lineRenderer.begin(ShapeType.Line);
					lineRenderer.setColor(Color.RED);
					lineRenderer.line(g.getBounds().getX() + g.getBounds().getWidth()/2, g.getBounds().getY() + g.getBounds().getHeight()/2 , b.getBounds().getX() + b.getBounds().getWidth()/2, b.getBounds().getY() + b.getBounds().getHeight()/2);
					lineRenderer.end();
				}
			}
		}
		
		fpsLogger.log();
	}	
	
	public void dispose() {
		getLevel().getCache().dispose();
		getLevel().setCache(null); //Prevents OpenGLException: Cannot use offsets when Array Buffer Object is disabled
		ObjectColor.dispose();
	}
	
	public Level getLevel() {
		return level;
	}

	public float getStateTime() {
		return stateTime;
	}

	public Vector3 getLerpTarget() {
		return lerpTarget;
	}

	public Stage getStage() {
		return stage;
	}

}
