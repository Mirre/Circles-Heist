package com.mirre.heist.objects.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mirre.heist.enums.Direction;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.interfaces.Collideable;
import com.mirre.heist.objects.interfaces.LevelObject;
import com.mirre.heist.utils.BiValue;

public abstract class MovingObject extends TextureObject {

	private Direction direction;
	private List<LevelObject> boundaries = new ArrayList<LevelObject>();
	private boolean onGround = true;
	
	public MovingObject(){}
	
	public MovingObject(int x, int y, float width, float height) {
		super(x, y, width, height);
	}

	
	public LevelObject getClosest(){
		LevelObject closest = null;
		for(LevelObject p : getBoundaries()){
			if(p.getBounds().overlaps(getBounds())){
				if(closest == null)
					closest = p;
				else{
					float distance1 = Math.abs(getBounds().getCenter(new Vector2()).x - p.getBounds().getCenter(new Vector2()).x);
					float distance2 = Math.abs(getBounds().getCenter(new Vector2()).x - closest.getBounds().getCenter(new Vector2()).x);
					if(distance1 < distance2){ //Is less than distance2
						closest = p;
					}
				}
			}
		}
		return closest;
	}
	
	public void fetchBoundaries() {
		int bottomLeftX = (int)getBounds().getX(); //Left side of Ball, Checks below Ball also
		int bottomLeftY = (int)Math.floor(getBounds().getY()); //Left side of Ball, Checks below Ball also
		int bottomRightX = (int)(getBounds().getX() + getBounds().getWidth()); //Right side of Ball, Checks below Ball also
		int bottomRightY = (int)Math.floor(getBounds().getY()); //Right side of Ball Checks below Ball also
		int topRightX = (int)(getBounds().getX() + getBounds().getWidth()); //Right side of Ball, Checks above Ball also
		int topRightY = (int)(getBounds().getY() + getBounds().getHeight()); //Right side of Ball, Checks above Ball also
		int topLeftX = (int)getBounds().getX(); //Left side of Ball, Checks above Ball also
		int topLeftY = (int)(getBounds().getY() + getBounds().getHeight()); //Left side of Ball, Checks above Ball also
		
		HashMap<BiValue<Integer,Integer>,LevelObject> tiles = Level.active().getPixelObjects();
		
		LevelObject bottomLeft = tiles.get(new BiValue<Integer,Integer>(bottomLeftX, bottomLeftY));
		LevelObject bottomRight = tiles.get(new BiValue<Integer,Integer>(bottomRightX, bottomRightY));
		LevelObject topRight = tiles.get(new BiValue<Integer,Integer>(topRightX, topRightY));
		LevelObject topLeft = tiles.get(new BiValue<Integer,Integer>(topLeftX, topLeftY));
		
		LevelObject[] tileArray = new LevelObject[]{ bottomRight, bottomLeft, topRight, topLeft };
		
		//Boundaries Add
		clearBoundaries();
		for(LevelObject p : tileArray){
			if(p != null){
				if(p.isCollideable() && p.hasTexture()){
					addBoundary(p);
				}
			}
		}
			
		//OnGround Listener.
		if(bottomLeft != null && bottomRight != null)
			setOnGround(bottomLeft.isCollideable() && bottomRight.isCollideable() && !((Collideable)bottomLeft).passThroughAble() && !((Collideable)bottomRight).passThroughAble() );
		else
			setOnGround(false);

	}
	
	
	public void clearBoundaries() {
		boundaries.clear();
	}	
	
	public List<LevelObject> getBoundaries() {
		return boundaries;
	}

	public void addBoundary(LevelObject pixel) {
		boundaries.add(pixel);
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}
	
	@Override
	public boolean canCache() {
		return false;
	}
	
	public boolean isAbove(Rectangle target){
		return (target.getY() < getBounds().getY());
	}
	
	public boolean isBelow(Rectangle target){
		return (target.getY() > getBounds().getY());
	}
	
	public boolean isLeftOf(Rectangle target){
		return (target.getX() < getBounds().getX());
	}
	
	public boolean isRightOf(Rectangle target){
		return (target.getX() > getBounds().getX());
	}
	
	public boolean isFacing(MovingObject moving){
		if(isRightOf(moving.getBounds()) && getDirection() == Direction.RIGHT)
			return true;
		else if(isLeftOf(moving.getBounds()) && getDirection() == Direction.LEFT)
			return true;
		return false;
	}

}
