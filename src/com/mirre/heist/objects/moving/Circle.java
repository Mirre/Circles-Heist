package com.mirre.heist.objects.moving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.mirre.heist.CircleHeist;
import com.mirre.heist.annonations.TextureLocations;
import com.mirre.heist.enums.CircleState;
import com.mirre.heist.enums.Direction;
import com.mirre.heist.handlers.Level;
import com.mirre.heist.objects.blocks.Bounceable;
import com.mirre.heist.objects.blocks.CollideableTile;
import com.mirre.heist.objects.blocks.Gold;
import com.mirre.heist.objects.blocks.Lava;
import com.mirre.heist.objects.blocks.Stair;
import com.mirre.heist.objects.interfaces.LevelObject;
import com.mirre.heist.objects.moving.circle.CircleController;
import com.mirre.heist.screens.LevelEndScreen;

@TextureLocations(value = { 
		"data/BallStealthRight.png",
		"data/BallStealthLeft.png",
		"data/BallLeft.png",
		"data/BallRight.png"
})
public class Circle extends CircleController {
	
	public Circle(){}
	
	public Circle(int x, int y){
		super(x, y);
		setDirection(Direction.RIGHT);
		setState(CircleState.NOTHING);
	}

	@Override
	public Color getColor() {
		return Color.RED;
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		
		//If you fall out of the map because of some bug or another reason the game will end and you lose.
		if(getBounds().y < -Level.active().getHeight()){
			setState(CircleState.LOSS);
		}
		
		if(getState() == CircleState.WON || getState() == CircleState.LOSS){
			getAcceleration().x = 0;
			setEndDelay(getEndDelay() - 0.05F);
			if(getEndDelay() <= 0){
				CircleHeist game = ((CircleHeist) Level.active().getGame());
				if(getState() != CircleState.LOSS && game.getCompletedLevels() < Level.active().getLevelID() + 1)
					game.setCompletedLevels(Level.active().getLevelID() + 1);
				game.setScreen(new LevelEndScreen(game, getState() == CircleState.WON, Level.active().getLevelID()));
			}
			return;
		}
		
		if(getStealthMeter() <= 0 && isStealth()){
			texture = getDirection() == Direction.LEFT ? getTextures().get("data/BallLeft.png") : getTextures().get("data/BallRight.png");
			setStealth(false);
		}else if(getStealthMeter() < 10 && !isStealth()){
			setStealthMeter(getStealthMeter() + 0.05F);
			
		}else if(getStealthMeter() >= 0 && isStealth()){
			setStealthMeter(getStealthMeter() - 0.05F);
		}
		
		
		if(getEscapeZone().getBounds().contains(getBounds()) && getGoldCollected() >= Gold.getAmountOfGold())
			setState(CircleState.WON);
		
	}

	@Override
	public void onCollideXY(LevelObject collideX, LevelObject collideY) {
		//Stairs
		if(collideX instanceof Stair || collideY instanceof Stair){
			setOnStairs(true);
			setOnGround(true);
		}else if(isOnStairs())
			setOnStairs(false);
	}

	@Override
	public void onCollideX(LevelObject collideX, boolean yCollided) {
		//Lava
		if(collideX instanceof Lava){
			setState(CircleState.LOSS);
			return;
		}
				
		//Gold
		if(collideX instanceof Gold && !((Gold)collideX).isCollected()){
			addGold();
			((Gold)collideX).setCollected(true);
		}
				
		//Bounceable
		else if(collideX instanceof Bounceable){
			if(!hasBounced()){
				setBounced(true);	
				getVelocity().x = getBounds().getX() > collideX.getBounds().x ? 6 : -6;
				getVelocity().y += 4F;
			}
			getAcceleration().x = getDirection().getReverse().getDir();
		}
	}

	@Override
	public void onCollideY(LevelObject collideY, boolean xCollided) {
		
		//Lava
		if(collideY instanceof Lava){
			setState(CircleState.LOSS);
			getAcceleration().x = 0;
			getVelocity().y = -0.01F;
			return;
		}
		
		//Gold
		if(collideY instanceof Gold && !((Gold)collideY).isCollected()){
			addGold();
			((Gold)collideY).setCollected(true);
		}
				
		//Bounceable
		else if(collideY instanceof Bounceable && isAbove(collideY.getBounds())){
			if(!hasBounced()){
				setBounced(true);
				setOnGround(false);
				getVelocity().y = 13F;  			
			}
		}
				
		//Normal Tile aka Wall. Prevents getting stuck in the ceiling.
		else if(collideY instanceof CollideableTile && isBelow(collideY.getBounds()) && getVelocity().y > 0){
			getVelocity().y = 0F;
			Gdx.app.log("Tile", "is above");
		}else if(collideY instanceof CollideableTile && isAbove(collideY.getBounds()) && getVelocity().y < 0){
			getVelocity().y = 0F;
		}
	}

	@Override
	public void onNoCollide() {
		//Stairs
		if(isOnStairs()){
			setOnStairs(false);
		}if(hasBounced()){
			setBounced(false);
		}
	}
	
	@Override
	public void onObjectCreation(Level level) {
		super.onObjectCreation(level);
		level.setCircle(this);
	}
}
