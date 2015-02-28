package com.mirre.ball.objects.moving;

import com.mirre.ball.CircleHeist;
import com.mirre.ball.enums.CircleState;
import com.mirre.ball.enums.Direction;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.handlers.Level;
import com.mirre.ball.objects.blocks.Bounceable;
import com.mirre.ball.objects.blocks.CollideableTile;
import com.mirre.ball.objects.blocks.Gold;
import com.mirre.ball.objects.blocks.Lava;
import com.mirre.ball.objects.blocks.Stair;
import com.mirre.ball.objects.interfaces.LevelObject;
import com.mirre.ball.screens.LevelEndScreen;

public class Circle extends CircleController {
	
	public Circle(int x, int y, ObjectColor color){
		super(x, y, 1F, 1F, color);
		setStateTime(0);
		setDirection(Direction.RIGHT);
		setState(CircleState.NOTHING);
	}
	
	@Override
	public void update(float deltaTime){
		super.update(deltaTime);
		
		//If you fall out of the map because of some bug or another reason the game will end and you lose.
		if(getBounds().y < -Level.getCurrentInstance().getHeight()){
			setState(CircleState.LOSS);
		}
		
		if(getState() == CircleState.WON || getState() == CircleState.LOSS){
			setEndDelay(getEndDelay() - 0.05F);
			if(getEndDelay() <= 0){
				CircleHeist game = ((CircleHeist) Level.getCurrentInstance().getGame());
				if(getState() != CircleState.LOSS && game.getCompletedLevels() < Level.getCurrentInstance().getLevelID() + 1)
					game.setCompletedLevels(Level.getCurrentInstance().getLevelID() + 1);
				game.setScreen(new LevelEndScreen(game, getState() == CircleState.WON, Level.getCurrentInstance().getLevelID()));
			}
			return;
		}
		
		if(getStealthMeter() <= 0 && isStealth()){
			texture = getDirection() == Direction.LEFT ? textureLeft : textureRight;
			setStealth(false);
		}else if(getStealthMeter() < 10 && !isStealth()){
			setStealthMeter(getStealthMeter() + 0.05F);
			
		}else if(getStealthMeter() >= 0 && isStealth()){
			setStealthMeter(getStealthMeter() - 0.05F);
		}
		
		
		//Gold needed to Win = TotalGold - floored rot of TotalGold
		// 2 = 3 - 1
		if(getEscapeZone().getBounds().contains(getBounds()) && getGoldCollected() >= Gold.getAmountOfGold())
			setState(CircleState.WON);

		setStateTime(getStateTime() + deltaTime);
	}
	
	@Override
	public void onCollideX(LevelObject collideX, boolean yCollided) {
		
		//Lava
		if(collideX instanceof Lava){
			setState(CircleState.LOSS);
		}
		
		//Stairs

		
		//Gold
		if(collideX instanceof Gold && !((Gold)collideX).isCollected()){
			addGold();
			((Gold)collideX).setCollected(true);
		}
		
		//Bounceable
		else if(collideX instanceof Bounceable){
			if(!hasBounced()){
				setBounced(true);
				
				getVelocity().x = getBounds().getX() > collideX.getBounds().x ? 1 : -1;
				getVelocity().y += 0.07F;
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
		//Stairs

		
		//Gold
		if(collideY instanceof Gold && !((Gold)collideY).isCollected()){
			addGold();
			((Gold)collideY).setCollected(true);
		}
		
		//Bounceable
		else if(collideY instanceof Bounceable){
			if(getBounds().getY() <= collideY.getBounds().getY()){
				getVelocity().y = 0;
			}else if(!hasBounced()){
				setBounced(true);
				setOnGround(false);
				
				float vY = 0.21F; 
				getVelocity().y = vY;  
					
				
			}
		}
		
		//Normal Tile aka Wall. Prevents getting stuck in the ceiling.
		else if(collideY instanceof CollideableTile)
			getVelocity().y = 0;
	}
	
	@Override
	public void onCollideXY(LevelObject collideX, LevelObject collideY) {
		
		//Stairs
		if(collideX instanceof Stair || collideY instanceof Stair){
			setOnStairs(true);
			setOnGround(true);
			getVelocity().y = 0;
		}else if(isOnStairs())
			setOnStairs(false);
		
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
