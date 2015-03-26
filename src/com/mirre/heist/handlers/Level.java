package com.mirre.heist.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.math.Rectangle;
import com.mirre.heist.CircleHeist;
import com.mirre.heist.objects.blocks.Gold;
import com.mirre.heist.objects.blocks.Truck;
import com.mirre.heist.objects.core.PixelObject;
import com.mirre.heist.objects.core.TextureObject;
import com.mirre.heist.objects.interfaces.CustomShapeDrawer;
import com.mirre.heist.objects.interfaces.LevelObject;
import com.mirre.heist.objects.interfaces.Moveable;
import com.mirre.heist.objects.interfaces.Textured;
import com.mirre.heist.objects.moving.Circle;
import com.mirre.heist.utils.BiValue;

public class Level {

	private static Level INSTANCE = null;
	
	private int levelID;
	private int height;
	private int width;
	
	private CircleHeist game;
	private ObjectHandler handler;
	private SpriteCache cache;
	
	private Circle circle;
	private Truck truck;
	
	private HashMap<BiValue<Integer,Integer>, LevelObject> pixelObjects = new HashMap<BiValue<Integer,Integer>, LevelObject>();
	private HashMap<BiValue<Integer,Integer>, LevelObject> collideTiles = new HashMap<BiValue<Integer,Integer>, LevelObject>();
	private List<Moveable> movingObjects = new ArrayList<Moveable>();
	private List<CustomShapeDrawer> drawers = new ArrayList<CustomShapeDrawer>();
	private List<Integer> cacheIDs = new ArrayList<Integer>();
	private List<Textured> uncachedObjects = new ArrayList<Textured>();

	
	public Level(CircleHeist game, int levelID){
		this.handler = new ObjectHandler(game.getHandler());
		this.game = game;
		this.levelID = levelID;
		INSTANCE = this;
		Gold.clearAmountOfGold();
		loadLevel(levelID);
	}
	
	public void loadLevel(int levelID){
		Pixmap pixmap = new Pixmap(Gdx.files.internal("levels/level" + levelID + ".png"));
		this.height = pixmap.getHeight();
		this.width = pixmap.getWidth();
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				int pix = pixmap.getPixel(x, y);
				Color c = new Color();
				Color.rgba8888ToColor(c, pix);
				PixelObject pixelObject = handler.colorToObject(c, x, pixmap.getHeight()-y);
				if(pixelObject != null)
					pixelObject.onLevelCreation(this);
			}
		}
		createTiles();
	}
	
	public void createTiles(){
		
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
			if(i >= getHeight() * (getWidth()/4) && isCaching){
				addCachedID(getCache().endCache());
				i = 0;
				isCaching = false;
			}
		}
		if(isCaching)
			addCachedID(getCache().endCache());
	}
	
	public void update(float deltaTime) {
		for(Moveable m : getMovingObjects()){
			m.update(deltaTime);
		}
	}

	public static Level active() {
		if(INSTANCE == null)
			throw new NullPointerException();
		return INSTANCE;
	}
	
	public Game getGame() {
		return game;
	}

	public int getLevelID() {
		return levelID;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public ObjectHandler getHandler() {
		return handler;
	}
	
	public HashMap<BiValue<Integer,Integer>, LevelObject> getPixelObjects() {
		return pixelObjects;
	}

	public void addPixelObject(LevelObject object) {
		pixelObjects.put(new BiValue<Integer,Integer>((int)object.getBounds().getX(),(int)object.getBounds().getY()), object);
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

	public void addCachedID(int i){
		cacheIDs.add(i);
	}

	public List<Textured> getUncachedObjects() {
		return uncachedObjects;
	}

	public void addUncachedObject(TextureObject p) {
		uncachedObjects.add(p);
	}
	
	public SpriteCache getCache() {
		return cache;
	}

	public void setCache(SpriteCache cache) {
		this.cache = cache;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	public Truck getTruck() {
		return truck;
	}

	public void setTruck(Truck truck) {
		this.truck = truck;
	}

	public List<CustomShapeDrawer> getDrawers() {
		return drawers;
	}

	public void addDrawer(CustomShapeDrawer drawer) {
		this.drawers.add(drawer);
	}
}
