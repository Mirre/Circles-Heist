package com.mirre.ball.enums;

public enum Direction {
	
	LEFT(-1),
	RIGHT(1),
	UP(1),
	CLIMBDOWN(-1),
	CLIMBUP(1);
	
	private int dir;
	
	Direction(int i){
		setDir(i);
	}

	public int getDir() {
		return dir;
	}

	public void setDir(int dir) {
		this.dir = dir;
	}

	
	public Direction getReverse(){
		switch(this){
			case CLIMBDOWN:
				return CLIMBUP;
			case CLIMBUP:
				return CLIMBDOWN;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			case UP:
				return UP;
			default:
				break;
		}
		return null;
	}
}
