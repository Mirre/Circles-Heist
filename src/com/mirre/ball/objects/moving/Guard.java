package com.mirre.ball.objects.moving;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mirre.ball.enums.CircleState;
import com.mirre.ball.enums.Direction;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.core.SimpleMovingObject;
import com.mirre.ball.objects.interfaces.Collideable;
import com.mirre.ball.objects.interfaces.LevelObject;
import com.mirre.ball.utils.BiValue;

public class Guard extends SimpleMovingObject {

	private TextureRegion textureLeft = null;
	private TextureRegion textureRight = null;
	private float directionDelay = 0;
	private float maxVelocity;
	
	private Rectangle sightRadius = new Rectangle(0,0,7,3);

	public Guard(int x, int y, ObjectColor color) {
		super(x, y, 1, 1, color);
		setDirection(Direction.LEFT);
		int i = new Random().nextInt(2);
		setMaxVelocity(4 +(2 * i));
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
			if(!xCollided && !(Level.getCurrentInstance().getPixelObjects().get(new BiValue<Integer,Integer>(x,y)) instanceof Collideable)){
				setDirection(getDirection().getReverse());
				setDirectionDelay(2);
			}
		}
	}
	
	@Override
	public void onCollideXY(LevelObject collideX, LevelObject collideY) {
		if(getDirectionDelay() <= 0){
			setDirection(getDirection().getReverse());
			setDirectionDelay(2);
		}
	}
	
	@Override
	public void onNoCollide() {
		
	}
	
	@Override
	public void update(float deltaTime) {
		Circle b = Level.getCurrentInstance().getCircle();
		
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
	public TextureRegion getTexture() {
		if(textureRight == null || textureLeft == null){
			textureRight = new TextureRegion(getType().getTexture(0), 0, 0, 66, 78);
			textureLeft = new TextureRegion(getType().getTexture(1), 0, 0, 66, 78);
		}
		if(getDirection() == Direction.RIGHT)
			return textureRight;
		else
			return textureLeft;
	}
	
	public float getDirectionDelay() {
		return directionDelay;
	}
	
	public void setDirectionDelay(float directionDelay) {
		this.directionDelay = directionDelay;
	}

	@Override
	public void changeMovement() { 
		getAcceleration().x = (getStandardAcceleration() * getDirection().getDir());
	}

	@Override
	public float getGravity() {
		return 20f;
	}

	@Override
	public float getStandardAcceleration() {
		return 30f;
	}

	@Override
	public float getMaxVelocity() {
		return maxVelocity;
	}

	@Override
	public float getDampening() {
		return 0.90f;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	
	public Rectangle getSightRadius() {
		return sightRadius;
	}

}
