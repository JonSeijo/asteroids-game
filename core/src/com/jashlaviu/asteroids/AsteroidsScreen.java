package com.jashlaviu.asteroids;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

public class AsteroidsScreen extends ScreenAdapter{

	private int level;
	private long lastShootTime, nextShootTime;
		
	private ArrayList<Shoot> shoots;
	private ArrayList<Asteroid> asteroids;
	private AsteroidsGame game;
	private Ship ship;
	private Gui gui;
	
	private Texture shipSheet, respAnimationSheet, shootTexture, asteroidsSheet;
	private TextureRegion[] asteroidsFrames;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
	
		shootTexture = new Texture(Gdx.files.internal("shoot2.png"));		
		shipSheet = new Texture(Gdx.files.internal("shipSheet.png"));
		asteroidsSheet = new Texture(Gdx.files.internal("asteroidsSheet.png"));	
		respAnimationSheet = new Texture(Gdx.files.internal("shipSheetResp.png"));
		
		createAsteroidFrames();		
		
		ship = new Ship(shipSheet);
		ship.setRespawnAnimationSheet(respAnimationSheet);
		shoots = new ArrayList<Shoot>();
		asteroids = new ArrayList<Asteroid>();
		gui = new Gui(this);
		
		lastShootTime = TimeUtils.millis();
		nextShootTime = 200; //In milliseconds
		
		Gdx.input.setInputProcessor(new InputHandler(this));
		
		level = 0;
		nextLevel();
		//newGame();
	}
	
	public void render(float delta){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();		
		updateAsteroids(delta, game.batch);
		updateShoots(delta, game.batch);
		ship.update(delta, game.batch);
		gui.update(delta, game.batch);
		game.batch.end();		
		
		checkGameOver();
		checkLevelComplete();

	}
	
	private void updateAsteroids(float delta, SpriteBatch batch) {
		for(Asteroid ast : asteroids){
			ast.update(delta, batch);
		}		
	}
	
	
	public void createAsteroids(int amount){
		for(int i = 0; i < amount; i++){
			asteroids.add(new Asteroid(asteroidsFrames, Asteroid.SIZE_BIG));
		}
	}

	private void updateShoots(float delta, SpriteBatch batch){		
		for(Shoot sh : shoots){
			sh.update(delta, batch);
		}
		
		Iterator<Shoot> iter = shoots.iterator();
		while(iter.hasNext()){	
			Shoot shoot = iter.next();
			if(asteroidCollision(shoot)){
				destroyAsteroid(shoot);
				iter.remove();
			}
			if(shoot.isDistanceReach()){
				iter.remove();
			}
		}
	}
	
	public void destroyAsteroid(GameObject object){
		Iterator<Asteroid> asterIter = asteroids.iterator();
		while(asterIter.hasNext()){
			Asteroid ast = asterIter.next();
			if(ast.getBounds().overlaps(object.getBounds())){
				asterIter.remove();
			}
		}
	}
	
	public boolean asteroidCollision(GameObject obj){		
		Iterator<Asteroid> iter = asteroids.iterator();
		while(iter.hasNext()){		// Asteroid rectangle  collides with   object rectangle
			if(iter.next().getBounds().overlaps(obj.getBounds())){
				return true;
			}
		}		
		return false;
	}
	
	public void disparar(){
		if((TimeUtils.timeSinceMillis(getLastShootTime())) > nextShootTime){ //If 300 milliseconds passed since last shoot, shoot again
			shoots.add(new Shoot(ship, shootTexture));
			lastShootTime = TimeUtils.millis();
		}
	}
	
	public void nextLevel(){
		level++;
		createAsteroids(level);
	}
	
	public void gameOver(){
		game.setScreen(new GameOverScreen(this, game));
	}

	
	public void dispose(){
		shipSheet.dispose();
		respAnimationSheet.dispose();
		shootTexture.dispose();
		asteroidsSheet.dispose();
		gui.dispose();		
	}	

	private void createAsteroidFrames() {
		asteroidsFrames = new TextureRegion[4];
		TextureRegion[][] tmp = TextureRegion.split(asteroidsSheet, 64, 64);
		int index = 0;
		for(int i = 0; i < 2; i++)
			for(int j = 0; j < 2; j++)
				asteroidsFrames[index++] = tmp[i][j];
	}
	
	public void newGame(){
		resetObjects();
		
		level = 0;
		nextLevel();
	}
	
	private void resetObjects(){
		asteroids.clear();
		shoots.clear();	
		ship.newGame();
	}
	
	private void checkLevelComplete(){
		if(asteroids.isEmpty()){
			nextLevel();
		}
	}
	
	private void checkGameOver(){
		if(asteroidCollision(ship)){
			if(!ship.isRespawning()){
				ship.lostLive();
				if(ship.getLives() <= 0){
					gameOver();
				}
			}
		}				
	}
	
	public void show(){
		newGame();
	}
	
	public float getShootTimePerc(){
		return (float)TimeUtils.timeSinceMillis(lastShootTime) / nextShootTime;		
	}
	
	public Ship getShip(){
		return ship;
	}

	public long getLastShootTime() {
		return lastShootTime;
	}
	
	public float getNextShootTime(){
		return nextShootTime;
	}
	
	public SpriteBatch getBatch(){
		return game.batch;
	}
}
