package com.mirre.heist.utils;

import com.mirre.heist.objects.interfaces.LevelObject;

public class CollideEvent {

	public enum CollideType {
		BOTH, X, Y, NO;
	}
	
	private LevelObject first = null, second = null;
	private CollideType type;
	private Boolean xyCollided = null;
	
	public CollideEvent(LevelObject x, LevelObject y){
		this.first = x;
		this.second = y;
		this.type = CollideType.BOTH;
	}
	
	public CollideEvent(LevelObject xy, CollideType type, boolean xyCollided){
		this.first = xy;
		this.xyCollided = xyCollided;
		this.type = type;
	}
	
	public CollideEvent(){
		this.type = CollideType.NO;
	}

	public LevelObject getFirst() {
		if(type == CollideType.NO)
			throw new NullPointerException();
		return first;
	}

	public LevelObject getSecond() {
		if(type != CollideType.BOTH)
			throw new NullPointerException();
		return second;
	}

	public CollideType getType() {
		return type;
	}

	public Boolean getXYCollided() {
		if(type != CollideType.X && type != CollideType.Y)
			throw new NullPointerException();
		return xyCollided;
	}

}
