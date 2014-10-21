package com.jashlaviu.asteroids;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class AsteroidsScreen extends ScreenAdapter{

	private int nivel;
	private long lastShootTime, nextShootTime;
		
	private ArrayList<Shoot> shoots;
	private ArrayList<Asteroid> asteroids;
	private AsteroidsGame game;
	private Ship ship;
	private Gui gui;
	
	private Texture shipSheet, shootTexture, asteroidSheet;
	
	public AsteroidsScreen(AsteroidsGame game){
		this.game = game;		
	
		shootTexture = new Texture(Gdx.files.internal("shoot2.png"));		
		shipSheet = new Texture(Gdx.files.internal("shipSheet.png"));
		asteroidSheet = new Texture(Gdx.files.internal("asteroidsSheet.png"));
		
		ship = new Ship(shipSheet);		
		shoots = new ArrayList<Shoot>();
		asteroids = new ArrayList<Asteroid>();
		gui = new Gui(this);
		
		lastShootTime = TimeUtils.millis();
		nextShootTime = 200; //In milliseconds
		
		Gdx.input.setInputProcessor(new InputHandler(this));
	}
	
	public void render(float delta){
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();		
		updateShoots(delta, game.batch);
		ship.update(delta, game.batch);
		gui.update(delta, game.batch);
		game.batch.end();		
	}
	
	private void updateShoots(float delta, SpriteBatch batch){
		
		for(Shoot sh : shoots){
			sh.update(delta, batch);
		}
		
		Iterator<Shoot> iter = shoots.iterator();
		while(iter.hasNext()){			
			if(iter.next().isDistanceReach()){
				iter.remove();
			}
		}
	}
	
	public void disparar(){
		if((TimeUtils.timeSinceMillis(getLastShootTime())) > nextShootTime){ //If 300 milliseconds passed since last shoot, shoot again
			shoots.add(new Shoot(ship, shootTexture));
			lastShootTime = TimeUtils.millis();
		}
	}
	
	public void dispose(){
		shipSheet.dispose();
		shootTexture.dispose();
		asteroidSheet.dispose();
		gui.dispose();		
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
	
}
