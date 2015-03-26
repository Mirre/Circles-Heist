package com.mirre.heist.objects.moving.circle;

import java.util.HashMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.mirre.heist.enums.CircleState;
import com.mirre.heist.enums.Direction;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.blocks.CollideableTile;
import com.mirre.heist.objects.interfaces.LevelObject;
import com.mirre.heist.screens.GameScreen;
import com.mirre.heist.utils.BiValue;

public abstract class CircleController extends CircleData {

	public static float movementButtonX, movementButtonY = 0;
	
	public CircleController(){}
	
	public CircleController(int x, int y) {
		super(x, y);
	}
	
	public void direction(Direction dir){
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
		
		//Android Touch Checks.
		if(Gdx.app.getType() == ApplicationType.Android){
			GameScreen screen = ((GameScreen)Level.active().getGame().getScreen());
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
			texture = getDirection() == Direction.LEFT ? getTextures().get("data/BallStealthLeft.png") : getTextures().get("data/BallStealthRight.png"); 
			setStealth(true);
		}else if(isStealth() && (Gdx.app.getType() == ApplicationType.Android ? !stealth : !Gdx.input.isKeyPressed(Keys.Q))){
			texture = getDirection() == Direction.LEFT ? getTextures().get("data/BallLeft.png") : getTextures().get("data/BallRight.png");
			setStealth(false);
		}
		
		//W-Press ; Jump
		if((Gdx.input.isKeyPressed(Keys.W)) || jump){
			setStealth(false);
			if(isOnStairs()){
				if(stairCheckAbove(getBounds().getX(), getBounds().getY())){
					getAcceleration().y = 20F;
				}else
					getVelocity().y = 0;
			}else if(isOnGround()){
				setState(CircleState.MOVING);
				direction(Direction.UP);
			}
		}
		
		//S-Press ; Climb down Stairs
		else if((Gdx.input.isKeyPressed(Keys.S) || climbDown) && isOnStairs() 
			&& stairCheckBelow(getBounds().getX(), getBounds().getY())){
				getAcceleration().y = -20F;
		}
		
		else if(isOnStairs()){
			getVelocity().y = 0F;
		}
		
		
		//A-Press ; Move Left
		if(Gdx.input.isKeyPressed(Keys.A) || left){
			texture = getTextures().get("data/BallLeft.png"); 
			setStealth(false);
			if(isOnGround()) 
				setState(CircleState.MOVING);
			direction(Direction.LEFT);
		}
				
		//D-Press ; Move Right
		else if(Gdx.input.isKeyPressed(Keys.D) || right){
			texture = getTextures().get("data/BallRight.png"); 
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
	
	@Override
	public void move(){
		if(!isOnGround() && !isOnStairs())
			getAcceleration().y += -getGravity();
		else if(!isOnStairs())
			getAcceleration().y = 0;
		getAcceleration().scl(0.02F);
		getVelocity().add(getAcceleration());
		
		if(getAcceleration().x == 0) 
			getVelocity().x *= getDampening();
		if(getVelocity().x > getMaxVelocity())
			getVelocity().x = getMaxVelocity();
		if(getVelocity().x < -getMaxVelocity()) 
			getVelocity().x = -getMaxVelocity();
		
		if(isOnStairs()){
			if(getVelocity().y > 6F)
				getVelocity().y = 6F;
			if(getVelocity().y < -6F)
				getVelocity().y = -6F;
		}
		
		getVelocity().scl(0.02F);
		attemptMove();
		getVelocity().scl(1.0F / 0.02F);
	}
	
	
	public boolean stairCheckBelow(float x, float y){
		HashMap<BiValue<Integer,Integer>, LevelObject> map = Level.active().getPixelObjects();
		if(!(map.get(new BiValue<Integer,Integer>(((int)x),((int) Math.floor(y - 0.15F)))) instanceof CollideableTile)
				&& !(map.get(new BiValue<Integer,Integer>(((int)(x+getBounds().getWidth())),
				((int) Math.floor(y - 0.15F)))) instanceof CollideableTile)){
			return true;
		}
		return false;
	}
	
	public boolean stairCheckAbove(float x, float y){
		HashMap<BiValue<Integer,Integer>, LevelObject> map = Level.active().getPixelObjects();
		if(!(map.get(new BiValue<Integer,Integer>(((int)x),(int)(Math.ceil(y + 0.15F)))) instanceof CollideableTile)
			&& !(map.get(new BiValue<Integer,Integer>(((int)(x+getBounds().getWidth())),(int)(Math.ceil(y + 0.15F)))) instanceof CollideableTile)){
			return true;
		}
		Gdx.app.log("LEL", "SVAG");
		return false;
	}
}
