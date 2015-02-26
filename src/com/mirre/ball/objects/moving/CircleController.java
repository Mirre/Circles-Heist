package com.mirre.ball.objects.moving;

import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.mirre.ball.enums.CircleState;
import com.mirre.ball.enums.Direction;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.blocks.CollideableTile;
import com.mirre.ball.objects.interfaces.LevelObject;
import com.mirre.ball.screens.GameScreen;
import com.mirre.ball.utils.BiValue;

public abstract class CircleController extends CircleData {

	public static float movementButtonX, movementButtonY = 0;
	
	CircleController(int x, int y, float width, float height, ObjectColor color) {
		super(x, y, width, height, color);
	}
	
	private void direction(Direction dir){
		if(Direction.UP == dir){
			getVelocity().y = 10f;
			setOnGround(false);
		}else{
			getAcceleration().x = (getStandardAcceleration() * dir.getDir());
			if(getDirection() != dir)
				setDirection(dir);
		}
	}
	
	@Override
	public void changeMovement() {
		
		if (getState() == CircleState.WON || getState() == CircleState.LOSS)
			return;
		
		boolean jump = false;
		boolean climbDown = false;
		boolean left = false;
		boolean right = false;
		boolean stealth = false;
		
		if(Gdx.app.getType() == ApplicationType.Android){
			GameScreen screen = ((GameScreen)Level.getCurrentInstance().getGame().getScreen());
			Button b = screen.getMoveButton();
			if(b.isPressed()){
				if(movementButtonY <= 1.5) //ClimbDown
					climbDown = true;
				if(movementButtonX >= 3.5) //Right
					right = true;
				else if(movementButtonX <= 1.5) //Left
					left = true;
			}
			b = screen.getStealthButton();
			if(b.isPressed()){
				stealth = true;
			}
			b = screen.getJumpButton();
			if(b.isPressed()){
				jump = true;
			}
		}
		
		//Q-Press ; Stealth
		if(((Gdx.input.isKeyPressed(Keys.Q))|| stealth) && isOnGround() && getState() == CircleState.NOTHING && getStealthMeter() == 10){
			texture = getDirection() == Direction.LEFT ? textureStealthLeft : textureStealthRight; 
			setStealth(true);
		}else if(isStealth() && (Gdx.app.getType() == ApplicationType.Android ? !stealth : !Gdx.input.isKeyPressed(Keys.Q))){
			texture = getDirection() == Direction.LEFT ? textureLeft : textureRight;
			setStealth(false);
		}
		
		//W-Press ; Jump
		if((Gdx.input.isKeyPressed(Keys.W)) || jump){
			texture = textureRight;
			setStealth(false);
			if(isOnStairs()){
				if(stairCheckAbove(getBounds().getX(), getBounds().getY()))
					getVelocity().y = 5F;
				
			}else if(isOnGround()){
				setState(CircleState.MOVING);
				direction(Direction.UP);
			}
		
		//S-Press ; Climb down Stairs
		}else if((Gdx.input.isKeyPressed(Keys.S) || climbDown) && isOnStairs() 
				&& stairCheckBelow(getBounds().getX(), getBounds().getY())){
			getVelocity().y = -5F;
		}
		
		//A-Press ; Move Left
		if(Gdx.input.isKeyPressed(Keys.A) || left){
			texture = textureLeft; 
			setStealth(false);
			if(isOnGround()) 
				setState(CircleState.MOVING);
			direction(Direction.LEFT);
		}
		
		//D-Press ; Move Right
		else if(Gdx.input.isKeyPressed(Keys.D) || right){
			texture = textureRight; 
			setStealth(false);
			if(isOnGround()) 
				setState(CircleState.MOVING);
			direction(Direction.RIGHT);
		}
		
		//No A-D key presses. 
		else{
			if(isOnGround()){
				setState(CircleState.NOTHING);
				getAcceleration().x = 0; //Activates dampening at Line 44 AdvancedMovingObject.java
			}else 
				getAcceleration().x = 0; //Activates dampening at Line 44 AdvancedMovingObject.java
		}
	}
	
	public boolean stairCheckBelow(float x, float y){
		HashMap<BiValue<Integer,Integer>, LevelObject> map = Level.getCurrentInstance().getPixelObjects();
		if(!(map.get(new BiValue<Integer,Integer>(((int)x),((int) Math.floor(y - 0.1F)))) instanceof CollideableTile)
				&& !(map.get(new BiValue<Integer,Integer>(((int)(x+getBounds().getWidth())),
				((int) Math.floor(y - 0.1F)))) instanceof CollideableTile)){
			return true;
		}
		return false;
	}
	
	public boolean stairCheckAbove(float x, float y){
		HashMap<BiValue<Integer,Integer>, LevelObject> map = Level.getCurrentInstance().getPixelObjects();
		if(!(map.get(new BiValue<Integer,Integer>(((int)x),(int)(Math.ceil(y + 0.1F)))) instanceof CollideableTile)
				&& !(map.get(new BiValue<Integer,Integer>(((int)(x+getBounds().getWidth())),(int)(Math.ceil(y + 0.1F)))) instanceof CollideableTile)){
			return true;
		}
		return false;
	}
}
