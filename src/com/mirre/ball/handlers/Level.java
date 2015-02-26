package com.mirre.ball.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Rectangle;
import com.mirre.ball.enums.ObjectColor;
import com.mirre.ball.objects.blocks.Gold;
import com.mirre.ball.objects.blocks.Truck;
import com.mirre.ball.objects.core.PixelObject;
import com.mirre.ball.objects.core.TextureObject;
import com.mirre.ball.objects.interfaces.LevelObject;
import com.mirre.ball.objects.interfaces.Moveable;
import com.mirre.ball.objects.interfaces.Textured;
import com.mirre.ball.objects.moving.Circle;
import com.mirre.ball.utils.BiValue;

public class Level {
	
	private static Level instance = null;
	
	private Circle circle;
	private Truck startLocation = null;
	private int height;
	private int width;
	private int levelID;
	private Game game;
	
	private HashMap<BiValue<Integer,Integer>, LevelObject> pixelObjects = new HashMap<BiValue<Integer,Integer>, LevelObject>();
	private HashMap<BiValue<Integer,Integer>, LevelObject> collideTiles = new HashMap<BiValue<Integer,Integer>, LevelObject>();
	private List<Moveable> movingObjects = new ArrayList<Moveable>();
	private List<Integer> cacheIDs = new ArrayList<Integer>();
	private List<Textured> uncachedObjects = new ArrayList<Textured>();
	private SpriteCache cache;
	
	
	public Level(Game game, int i){
		Gold.clearAmountOfGold();
		this.game = game;
		this.levelID = i;
		Pixmap pixmap = new Pixmap(Gdx.files.internal("data/level" + i + ".png"));
		this.height = pixmap.getHeight();
		this.width = pixmap.getWidth();
		for (int y = 0; y < pixmap.getHeight(); y++) {
			for (int x = 0; x < pixmap.getWidth(); x++) {
				int pix = pixmap.getPixel(x, y);
				PixelObject pixelObject = PixelObject.colorToPixelObject(pix, x, pixmap.getHeight() - y);
				pixelObject.onLevelCreation(this);
			}
		}
		ObjectColor.load();
		createTiles();
		Level.instance = this;
	}
	
	private void createTiles(){
		
		setCache(new SpriteCache(getHeight() * getWidth(), false));
		Iterator<Entry<BiValue<Integer, Integer>, LevelObject>> iterator = getPixelObjects().entrySet().iterator();
		
		int i = 0;
		boolean isCaching = false;
		while(iterator.hasNext()){
			Entry<BiValue<Integer, Integer>, LevelObject> entry = iterator.next();
			int x = entry.getKey().getFirst();
			int y = entry.getKey().getSecond();
			LevelObject tile = entry.getValue();
			if(i == 0 && !isCaching){
				getCache().beginCache();
				isCaching = true;
			}
			if(tile.canCache() && tile.hasTexture()){
				getCache().add(((TextureObject)tile).getTexture(), x, y, (((TextureObject)tile).getTextureWidth()), (((TextureObject)tile).getTextureHeight()));
				i++;
			}else if(tile.hasTexture()){
				addUncachedObject((TextureObject)tile);
			}
			if(i >= getHeight() && isCaching){
				addCachedID(getCache().endCache());
				i = 0;
				isCaching = false;
			}
		}
		if(isCaching)
			addCachedID(getCache().endCache());
	}
	
	public Truck getStartLocation() {
		return startLocation;
	}
	
	public void setStartLocation(Truck startLocation) {
		this.startLocation = startLocation;
	}
	
	public Circle getCircle() {
		return circle;
	}
	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	
	public void update(float deltaTime) {
		for(Moveable m : getMovingObjects()){
			m.update(deltaTime);
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public HashMap<BiValue<Integer,Integer>, LevelObject> getPixelObjects() {
		return pixelObjects;
	}

	public void addPixelObject(LevelObject object) {
		pixelObjects.put(new BiValue<Integer,Integer>((int)object.getBounds().getX(),(int)object.getBounds().getY()), object);
	}


	public int getLevelID() {
		return levelID;
	}

	public Game getGame() {
		return game;
	}

	public static Level getCurrentInstance() {
		if(instance == null)
			throw new NullPointerException();
		return instance;
	}
	
	public List<Moveable> getMovingObjects() {
		return movingObjects;
	}

	public void addMovingObject(Moveable movingObject) {
		movingObjects.add(movingObject);
	}


	public HashMap<BiValue<Integer,Integer>, LevelObject> getCollideTiles() {
		return collideTiles;
	}
	
	public LevelObject getCollideTile(int x, int y) {
		BiValue<Integer,Integer> key = new BiValue<Integer,Integer>(x,y);
		if(collideTiles.containsKey(key))
			return collideTiles.get(key);
		return null;
	}

	public void addCollideTile(PixelObject tile) {
		Rectangle r = tile.getBounds();
		collideTiles.put(new BiValue<Integer, Integer>((int)r.x, (int)r.y), tile);
	}
	
	public List<Integer> getCacheIDs() {
		return cacheIDs;
	}

	private void addCachedID(int i){
		cacheIDs.add(i);
	}

	public List<Textured> getUncachedObjects() {
		return uncachedObjects;
	}

	private void addUncachedObject(TextureObject p) {
		uncachedObjects.add(p);
	}
	
	public SpriteCache getCache() {
		return cache;
	}

	public void setCache(SpriteCache cache) {
		this.cache = cache;
	}
}
