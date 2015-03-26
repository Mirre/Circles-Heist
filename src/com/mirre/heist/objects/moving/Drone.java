package com.mirre.heist.objects.moving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.enums.CircleState;
import com.mirre.heist.enums.Direction;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.blocks.Stair;
import com.mirre.heist.objects.core.SimpleMovingObject;
import com.mirre.heist.objects.interfaces.LevelObject;

@TextureLocations(value = { 
		"data/helicopterDrone0.png", 
		"data/helicopterDrone1.png",
		"data/helicopterDrone2.png"
})
public class Drone extends SimpleMovingObject {

	private boolean chasing = false;
	private boolean caught = false;
	
	private Animation animation = null;
	private float directionDelay = 0;
	private float stateTime = 0;
	
	private Rectangle sightBox = new Rectangle(0,0,15,8);
	
	public Drone(){}
	
	public Drone(int x, int y) {
		super(x, y, 1, 1);
		setDirection(null);
	}
	
	@Override
	public Color getColor() {
		return Color.PINK;
	}

	@Override
	public void update(float deltaTime) {
		
		Circle b = Level.active().getCircle();
		
		Rectangle r = new Rectangle(getBounds()).setSize(0.4F, 0.4F);
		if(b.getBounds().overlaps(r) && b.getState() != CircleState.WON){
			b.setState(CircleState.LOSS);
			setCaught(true);	
		}
		
		sightBox.setCenter(getBounds().getCenter(new Vector2()));
		
		if(inSight(b, sightBox, false) && !isChasing()){
			setDirection(null);
			setChasing(true);
		}else if(isChasing() && getDirectionDelay() <= 0){ //Is chasing and Direction not changed within a short duration..
			setChasing(false);
		}
		
		
		
		if(getDirectionDelay() > 0){
			setDirectionDelay(getDirectionDelay() - 0.05F);
		}
		
		super.update(deltaTime);
	}
	
	@Override
	public void changeMovement() {
		if(isChasing() && !hasCaught()){
			Circle b = Level.active().getCircle();
			Rectangle r = new Rectangle(getBounds());
			Vector2 guard = r.getPosition(new Vector2());
			Vector2 ball = b.getBounds().getPosition(new Vector2());
			guard.sub(ball);
			guard.nor();
			guard.y = guard.y * 10;
			guard.x = (guard.x * getStandardAcceleration());
			if(isLeftOf(b.getBounds())){
				if(getDirection() != Direction.LEFT){
					setDirection(Direction.LEFT);
					setDirectionDelay(5);
				}
			}else if(isRightOf(b.getBounds())){
				if(getDirection() != Direction.RIGHT){
					setDirection(Direction.RIGHT);
					setDirectionDelay(5);
				}
			}
			getAcceleration().sub(guard);
		}
		else if(hasCaught()){
			Circle b = Level.active().getCircle();
			Rectangle r = new Rectangle(b.getBounds());
			r.y = r.y + r.height;
			setBounds(r);
		}
	}
	
	@Override
	public void move(){
		if(hasCaught())
			return;
		getAcceleration().scl(0.02F);
		getVelocity().add(getAcceleration());
		
		if(getAcceleration().x == 0) 
			getVelocity().x *= getDampening();
		if(getVelocity().x > getMaxVelocity())
			getVelocity().x = getMaxVelocity();
		if(getVelocity().x < -getMaxVelocity()) 
			getVelocity().x = -getMaxVelocity();
		
		if(getAcceleration().y == 0) 
			getVelocity().y *= getDampening();
		if(getVelocity().y > getMaxVelocity()/2)
			getVelocity().y = getMaxVelocity()/2;
		if(getVelocity().y < -getMaxVelocity()/2) 
			getVelocity().y = -getMaxVelocity()/2;
		
		
		getVelocity().scl(0.02F);
		attemptMove();
		getVelocity().scl(1.0F / 0.02F);
	}

	@Override
	public TextureRegion getTexture() {
		if(getAnimation() == null){
			animation = new Animation(0.05F, getTextures().values().toArray(new TextureRegion[getTextures().values().size()])); 
			animation.setPlayMode(PlayMode.LOOP);
		}
		setStateTime(getStateTime() + Gdx.graphics.getDeltaTime());
		return getAnimation().getKeyFrame(getStateTime());
	}
	
	@Override
	public float getGravity() {
		return 0;
	}

	@Override
	public float getStandardAcceleration() {
		return 30F;
	}

	@Override
	public float getMaxVelocity() {
		return 9F;
	}

	@Override
	public float getDampening() {
		return 0.90F;
	}

	@Override
	public void onCollideXY(LevelObject collideX, LevelObject collideY) {
		
	}

	@Override
	public void onCollideX(LevelObject collideX, boolean yCollided) {
		if(isChasing() && !hasCaught() && getDirectionDelay() <= 0){
			setChasing(false);
		}
		if(collideX instanceof Stair){
			setChasing(false);
		}
	}

	@Override
	public void onCollideY(LevelObject collideY, boolean xCollided) {
		if(collideY.isCollideable())
			getVelocity().y = 0;
	}

	@Override
	public void onNoCollide() {
		
	}
	
	public boolean isChasing() {
		return chasing;
	}

	public void setChasing(boolean chasing) {
		this.chasing = chasing;
	}

	public float getDirectionDelay() {
		return directionDelay;
	}

	public void setDirectionDelay(float directionDelay) {
		this.directionDelay = directionDelay;
	}

	public Animation getAnimation() {
		return animation;
	}

	public float getStateTime() {
		return stateTime;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}

	public boolean hasCaught() {
		return caught;
	}

	public void setCaught(boolean caught) {
		this.caught = caught;
	}

}
