package com.mirre.heist.objects.core;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.interfaces.Collideable;
import com.mirre.heist.objects.interfaces.LevelObject;
import com.mirre.heist.objects.interfaces.Moveable;
import com.mirre.heist.utils.CollideEvent;
import com.mirre.heist.utils.CollideEvent.CollideType;

public abstract class SimpleMovingObject extends MovingObject implements Moveable {
	
	private Vector2 acceleration = new Vector2();
	private Vector2 velocity = new Vector2();
	private List<CollideEvent> collides = new ArrayList<CollideEvent>();
	
	public SimpleMovingObject(){}
	
	public SimpleMovingObject(int x, int y, float width, float height) {
		super(x, y, width, height);
	}
	
	public abstract void changeMovement();
	public abstract float getGravity();
	public abstract float getStandardAcceleration();
	public abstract float getMaxVelocity();
	public abstract float getDampening();
	public abstract void onCollideXY(LevelObject collideX, LevelObject collideY);
	public abstract void onCollideX(LevelObject collideX, boolean yCollided);
	public abstract void onCollideY(LevelObject collideY, boolean xCollided);
	public abstract void onNoCollide();
	
	@Override
	public void update(float deltaTime) {
		move();
		changeMovement();
		for(CollideEvent event : collides){
			switch(event.getType()){
				case BOTH:
					onCollideXY(event.getFirst(), event.getSecond());
					break;
				case NO:
					onNoCollide();
					break;
				case X:
					onCollideX(event.getFirst(), event.getXYCollided());
					break;
				case Y:
					onCollideY(event.getFirst(), event.getXYCollided());
					break;
				default:
					break;
			
			}
		}
		collides.clear();
	}
	
	public void move(){
		getAcceleration().y = !isOnGround() ? -getGravity() : 0;
		getAcceleration().scl(0.02F);
		getVelocity().add(getAcceleration());
		
		if(getAcceleration().x == 0) 
			getVelocity().x *= getDampening();
		if(getVelocity().x > getMaxVelocity())
			getVelocity().x = getMaxVelocity();
		if(getVelocity().x < -getMaxVelocity()) 
			getVelocity().x = -getMaxVelocity();
		
		getVelocity().scl(0.02F);
		attemptMove();
		getVelocity().scl(1.0F / 0.02F);
	}
	
	public void attemptMove() {
		getBounds().x += getVelocity().x;
		fetchBoundaries();
		LevelObject pixX = getClosest();
		boolean x = pixX != null;
		for(LevelObject pix : getBoundaries()){
			if(pix.getBounds().overlaps(getBounds())){
				Collideable coll = (Collideable) pix;
				if(getVelocity().x < 0 && !coll.passThroughAble())
					getBounds().x = pix.getBounds().x + pix.getBounds().width + 0.01f;
				else if(!coll.passThroughAble())
					getBounds().x = pix.getBounds().x - getBounds().width - 0.01f;
				if(!coll.passThroughAble())
					getVelocity().x = 0;
			}
		}
			
		getBounds().y += getVelocity().y;
		fetchBoundaries();
		LevelObject pixY = getClosest();
		boolean y = pixY != null;
		if(y){
			Collideable coll = (Collideable) pixY;
			if(getVelocity().y < 0 && !coll.passThroughAble()) {
				getBounds().setY(pixY.getBounds().y + pixY.getBounds().height + 0.01f);
				setOnGround(true);
			}else if(!coll.passThroughAble())
				getBounds().setY(pixY.getBounds().y - getBounds().height - 0.01f);
		}
		
		if(x && y)
			collides.add(new CollideEvent(pixX, pixY));
		else if(!x && !y)
			collides.add(new CollideEvent());
		if(x)
			collides.add(new CollideEvent(pixX, CollideType.X, y));
		if(y)
			collides.add(new CollideEvent(pixY, CollideType.Y, x));
	}	
	
	public boolean inSight(SimpleMovingObject target, Rectangle sightBox, boolean facingCheck){
		
		if(sightBox.overlaps(target.getBounds())){
			
			Vector2 point1 = target.getBounds().getCenter(new Vector2());
			Vector2 point2 = getBounds().getCenter(new Vector2());
			
			Vector2 vectorPointer = point2.cpy().sub(point1).nor().clamp(-0.4F, 0.4F);
			Rectangle r = new Rectangle(getBounds());
			
			int i = 0;
			while(r.overlaps(sightBox)){
				r.setPosition(r.getPosition(new Vector2()).sub(vectorPointer));
				Vector2 center = r.getCenter(new Vector2());
				if(Level.active().getCollideTile((int)center.x, (int)center.y) != null && ((target.isRightOf(r) && isLeftOf(target.getBounds())) || (target.isLeftOf(r) && isRightOf(target.getBounds())))){
					return false;
				}if(i >= 100){
					return false;
				}
				i++;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void onObjectCreation(Level level) {
		level.addMovingObject(this);
	}

	public Vector2 getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration = acceleration;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}
	
}
