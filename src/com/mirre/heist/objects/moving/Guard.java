package com.mirre.heist.objects.moving;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.enums.CircleState;
import com.mirre.heist.enums.Direction;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.blocks.Gold;
import com.mirre.heist.objects.core.SimpleMovingObject;
import com.mirre.heist.objects.interfaces.Collideable;
import com.mirre.heist.objects.interfaces.CustomShapeDrawer;
import com.mirre.heist.objects.interfaces.LevelObject;
import com.mirre.heist.utils.BiValue;

@TextureLocations(value = { 
		"data/guardRight.png",
		"data/guardLeft.png" 
})
public class Guard extends SimpleMovingObject implements CustomShapeDrawer {

	private float directionDelay = 0;
	private float maxVelocity;
	
	private Rectangle sightRadius = new Rectangle(0,0,7,3);
	
	public Guard(){}
	
	public Guard(int x, int y) {
		super(x, y, 1, 1);
		setDirection(Direction.LEFT);
		int i = new Random().nextInt(2);
		setMaxVelocity(4 +(2 * i));
	}

	
	@Override
	public Color getColor() {
		return Color.CYAN;
	}
	
	@Override
	public void onObjectCreation(Level level) {
		super.onObjectCreation(level);
		level.addDrawer(this);
	}
	
	@Override
	public void drawShape(ShapeRenderer renderer, Stage stage) {
		Circle b = Level.active().getCircle();
		if(getSightRadius().overlaps(b.getBounds()) && isFacing(b)){
			stage.getCamera().update();
			renderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
			renderer.begin(ShapeType.Line);
			renderer.setColor(Color.RED);
			renderer.line(getBounds().getX() + getBounds().getWidth()/2, getBounds().getY() + getBounds().getHeight()/2 , b.getBounds().getX() + b.getBounds().getWidth()/2, b.getBounds().getY() + b.getBounds().getHeight()/2);
			renderer.end();
		}
	}
	
	@Override
	public TextureRegion getTexture() {
		if(getDirection() == Direction.RIGHT)
			texture = getTextures().get("data/guardRight.png");
		else
			texture = getTextures().get("data/guardLeft.png");
		return super.getTexture();
			
	}

	@Override
	public void update(float deltaTime) {
		Circle b = Level.active().getCircle();
		
		sightRadius.setCenter(getBounds().getCenter(new Vector2()));
		
		
		if(isFacing(b) && inSight(b, sightRadius, false) && !b.isStealth()){
			b.setState(CircleState.LOSS);
		}
		
		if(getDirectionDelay() != 0F){
			setDirectionDelay(getDirectionDelay() <= 0 ? 0 : getDirectionDelay()-0.05F);
		}
		super.update(deltaTime);
	}
	
	@Override
	public void changeMovement() {
		getAcceleration().x = (getStandardAcceleration() * getDirection().getDir());
	}

	@Override
	public float getGravity() {
		return 20F;
	}

	@Override
	public float getStandardAcceleration() {
		return 30F;
	}

	@Override
	public float getMaxVelocity() {
		return maxVelocity;
	}

	@Override
	public float getDampening() {
		return 0.90F;
	}

	@Override
	public void onCollideXY(LevelObject collideX, LevelObject collideY) {
		if(getDirectionDelay() <= 0 && !(collideX instanceof Gold)){
			setDirection(getDirection().getReverse());
			setDirectionDelay(2);
		}
	}

	@Override
	public void onCollideX(LevelObject collideX, boolean yCollided) {
		if(collideX.isCollideable()){
			getAcceleration().x = getDirection().getDir();
		}
	}

	@Override
	public void onCollideY(LevelObject collideY, boolean xCollided) {
		if(collideY.isCollideable()){
			getVelocity().y = 0;
			int x = (int) (collideY.getBounds().x + (getDirection().getDir() * 1.2));
			int y = (int) (collideY.getBounds().y);
			if(!xCollided && !(Level.active().getPixelObjects().get(new BiValue<Integer,Integer>(x,y)) instanceof Collideable)){
				setDirection(getDirection().getReverse());
				setDirectionDelay(2);
			}
		}
	}

	@Override
	public void onNoCollide() {
		
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	
	public float getDirectionDelay() {
		return directionDelay;
	}
	
	public void setDirectionDelay(float directionDelay) {
		this.directionDelay = directionDelay;
	}

	public Rectangle getSightRadius() {
		return sightRadius;
	}

}
